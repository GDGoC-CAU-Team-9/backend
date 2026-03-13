package com.gdg_team9.SafePlate.file.controller;

import com.gdg_team9.SafePlate.api.ApiResponse;
import com.gdg_team9.SafePlate.file.dto.FileRequest;
import com.gdg_team9.SafePlate.file.dto.FileResponse;
import com.gdg_team9.SafePlate.file.service.FileService;
import com.gdg_team9.SafePlate.member.domain.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/{fileId}")
    public ApiResponse<FileResponse.FileUrlResponse> getFileUrl(
            @AuthenticationPrincipal Member member,
            @PathVariable("fileId") long fileId
    ) {
        String fileUrl = fileService.getFileUrlByMemberAndId(member, fileId);
        return ApiResponse.onSuccess(
                FileResponse.FileUrlResponse.builder()
                        .fileUrl(fileUrl)
                        .build()
        );
    }

    @PostMapping("/presigned-url")
    public ApiResponse<FileResponse.PresignedUrlResponse> presignedUrl(
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody FileRequest.PresignedUrlRequest presignedUrlRequest
    ) {
        return ApiResponse.onSuccess(fileService.getPreSignedUrl(member, presignedUrlRequest));
    }

    @PatchMapping("/{fileId}/status")
    public ApiResponse<FileResponse.FileUrlResponse> patchStatus(
            @AuthenticationPrincipal Member member,
            @PathVariable("fileId") long fileId,
            @Valid @RequestBody FileRequest.PatchStatusRequest patchStatusRequest
    ) {
        String fileUrl = fileService.patchFileStatus(member, fileId, patchStatusRequest);
        return ApiResponse.onSuccess(
                FileResponse.FileUrlResponse.builder()
                        .fileUrl(fileUrl)
                        .build()
        );
    }
}
