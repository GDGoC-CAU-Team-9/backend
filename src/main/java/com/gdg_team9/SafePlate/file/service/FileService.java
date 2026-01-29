package com.gdg_team9.SafePlate.file.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.gdg_team9.SafePlate.api.code.status.ErrorStatus;
import com.gdg_team9.SafePlate.exception.GeneralException;
import com.gdg_team9.SafePlate.file.domain.FileStatus;
import com.gdg_team9.SafePlate.file.domain.S3File;
import com.gdg_team9.SafePlate.file.dto.FileRequest;
import com.gdg_team9.SafePlate.file.dto.FileResponse;
import com.gdg_team9.SafePlate.file.repository.S3FileRepository;
import com.gdg_team9.SafePlate.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private final S3FileRepository s3FileRepository;

    public String getFileUrlById(Member member, long id) {
        S3File s3File = s3FileRepository.findByIdAndMember(id, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.FILE_NOT_FOUND));

        return getFileUrl(s3File);
    }

    public List<String> getFileUrlsByIds(Member member, Collection<Long> ids) {
        return s3FileRepository.findAllByMemberAndIdIn(member, ids)
                .stream()
                .map(this::getFileUrl)
                .toList();
    }

    @Transactional
    public String patchFileStatus(
            Member member,
            long id,
            FileRequest.PatchStatusRequest patchStatusRequest
    ) {
        S3File s3File = s3FileRepository.findByIdAndMember(id, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.FILE_NOT_FOUND));
        s3File.setStatus(patchStatusRequest.getFileStatus());

        // S3File 반환(URL)
        return getFileUrl(s3File);
    }

    /**
     * presigned url 발급
     * @param presignedUrlRequest 버킷 디렉토리 이름, 클라이언트가 전달한 파일명 파라미터
     * @return presigned url
     */
    @Transactional
    public FileResponse.PresignedUrlResponse getPreSignedUrl(
            Member member,
            FileRequest.PresignedUrlRequest presignedUrlRequest
    ) {
        String fileId = createFileId();
        String path = presignedUrlRequest.getPath();
        String fileName = fileId + '.' + presignedUrlRequest.getFileType();

        S3File s3File = S3File.builder()
                .member(member)
                .path(path)
                .fileName(fileName)
                .status(FileStatus.UPLOADING)
                .build();
        s3FileRepository.save(s3File);

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                getGeneratePreSignedUrlRequest(bucket, s3File.getFullFileName());
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return FileResponse.PresignedUrlResponse.builder()
                .fileId(s3File.getId())
                .presignedUrl(url.toString())
                .build();
    }

    private String getFileUrl(S3File s3File) {
        if (!s3File.getStatus().isAvailable()) {
            throw new GeneralException(ErrorStatus.FILE_NOT_AVAILABLE);
        }
        return amazonS3.getUrl(bucket, s3File.getFullFileName()).toString();
    }

    /**
     * 파일 업로드용(PUT) presigned url 생성
     * @param bucket 버킷 이름
     * @param fileName S3 업로드용 파일 이름
     * @return presigned url
     */
    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(
            String bucket,
            String fileName
    ) {
        return new GeneratePresignedUrlRequest(bucket, fileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(getPreSignedUrlExpiration());
    }

    /**
     * presigned url 유효 기간 설정
     * @return 유효기간
     */
    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    /**
     * 파일 고유 ID를 생성
     * @return 36자리의 UUID
     */
    private String createFileId() {
        return UUID.randomUUID().toString();
    }
}
