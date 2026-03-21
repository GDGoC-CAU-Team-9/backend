package com.gdg_team9.SafePlate.file.controller;

import com.gdg_team9.SafePlate.api.CommonResponse;
import com.gdg_team9.SafePlate.config.AuthErrorResponses;
import com.gdg_team9.SafePlate.file.dto.FileRequest;
import com.gdg_team9.SafePlate.file.dto.FileResponse;
import com.gdg_team9.SafePlate.file.service.FileService;
import com.gdg_team9.SafePlate.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = "File", description = "파일 관리 API")
public class FileController {
    private final FileService fileService;

    @GetMapping("/{fileId}")
    @Operation(summary = "파일 URL 조회", description = "파일 ID로 파일의 접근 URL을 조회합니다")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                              "isSuccess": false,
                              "code": "FILE4004",
                              "message": "해당하는 파일이 없습니다.",
                              "success": false
                            }
                            """)))
    @AuthErrorResponses
    public CommonResponse<FileResponse.FileUrlResponse> getFileUrl(
            @AuthenticationPrincipal Member member,
            @PathVariable("fileId") long fileId
    ) {
        String fileUrl = fileService.getFileUrlByMemberAndId(member, fileId);
        return CommonResponse.onSuccess(
                FileResponse.FileUrlResponse.builder()
                        .fileUrl(fileUrl)
                        .build()
        );
    }

    @PostMapping("/presigned-url")
    @Operation(summary = "Pre-signed URL 생성", description = "파일 업로드를 위한 Pre-signed URL을 생성합니다")
    @ApiResponse(responseCode = "200", description = "생성 성공")
    @AuthErrorResponses
    public CommonResponse<FileResponse.PresignedUrlResponse> presignedUrl(
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody FileRequest.PresignedUrlRequest presignedUrlRequest
    ) {
        return CommonResponse.onSuccess(fileService.getPreSignedUrl(member, presignedUrlRequest));
    }

    @PatchMapping("/{fileId}/status")
    @Operation(summary = "파일 상태 업데이트", description = "파일의 상태를 업데이트합니다")
    @ApiResponse(responseCode = "200", description = "업데이트 성공")
    @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                              "isSuccess": false,
                              "code": "FILE4004",
                              "message": "해당하는 파일이 없습니다.",
                              "success": false
                            }
                            """)))
    @AuthErrorResponses
    public CommonResponse<FileResponse.FileUrlResponse> patchStatus(
            @AuthenticationPrincipal Member member,
            @PathVariable("fileId") long fileId,
            @Valid @RequestBody FileRequest.PatchStatusRequest patchStatusRequest
    ) {
        String fileUrl = fileService.patchFileStatus(member, fileId, patchStatusRequest);
        return CommonResponse.onSuccess(
                FileResponse.FileUrlResponse.builder()
                        .fileUrl(fileUrl)
                        .build()
        );
    }
}
