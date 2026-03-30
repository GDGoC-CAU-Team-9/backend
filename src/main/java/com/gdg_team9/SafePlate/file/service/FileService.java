package com.gdg_team9.SafePlate.file.service;

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
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    private final S3Presigner s3Presigner;
    private final S3FileRepository s3FileRepository;

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
        if (s3File.getStatus().isAvailable()) {
            return getFileUrl(s3File);
        } else {
            return null;
        }
    }

    /**
     * 파일 소유자를 검증하면서 file url (보기) 발급
     *
     * @param id 파일 id (DB에 저장된 id)
     * @return file url
     */
    public String getFileUrlByMemberAndId(Member member, long id) {
        S3File s3File = s3FileRepository.findByIdAndMember(id, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.FILE_NOT_FOUND));

        return getFileUrl(s3File);
    }

    /**
     * 파일 소유자 검증 없이 file url (보기) 발급
     *
     * @param ids 파일 id (DB에 저장된 id)
     * @return file url
     */
    public List<String> getFileUrlsByIds(Collection<Long> ids) {
        return s3FileRepository.findAllByIdIn(ids)
                .stream()
                .map(this::getFileUrl)
                .toList();
    }

    /**
     * 파일 소유자를 검증하면서 file url (보기) 발급
     *
     * @param ids 파일 id (DB에 저장된 id)
     * @return file url
     */
    public List<String> getFileUrlsByMemberAndIds(Member member, Collection<Long> ids) {
        return s3FileRepository.findAllByMemberAndIdIn(member, ids)
                .stream()
                .map(this::getFileUrl)
                .toList();
    }

    /**
     * presigned url 발급
     *
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

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(s3File.getFullFileName())
                .build();
        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(3))
                .putObjectRequest(putObjectRequest)
                .build();
        URL url = s3Presigner.presignPutObject(putObjectPresignRequest).url();

        return FileResponse.PresignedUrlResponse.builder()
                .fileId(s3File.getId())
                .presignedUrl(url.toString())
                .build();
    }

    private String getFileUrl(S3File s3File) {
        if (!s3File.getStatus().isAvailable()) {
            throw new GeneralException(ErrorStatus.FILE_NOT_AVAILABLE);
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(s3File.getFullFileName())
                .build();

        PresignedGetObjectRequest presignRequest = s3Presigner.presignGetObject(r ->
                r.getObjectRequest(getObjectRequest)
                        .signatureDuration(Duration.ofMinutes(10))
        );  // 10분 만료

        return presignRequest.url().toString();
    }

    /**
     * 파일 고유 ID를 생성
     *
     * @return 36자리의 UUID
     */
    private String createFileId() {
        return UUID.randomUUID().toString();
    }
}
