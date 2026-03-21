package com.gdg_team9.SafePlate.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class FileResponse {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Pre-signed URL 응답")
    public static class PresignedUrlResponse {
        @Schema(description = "파일 ID", example = "7")
        private long fileId;

        @Schema(description = "Pre-signed URL", example = "https://safeplate26.s3.ap-northeast-2.amazonaws.com/...")
        private String presignedUrl;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "파일 URL 응답")
    public static class FileUrlResponse {
        @Schema(description = "파일 접근 URL", example = "https://safeplate26.s3.ap-northeast-2.amazonaws.com/...")
        private String fileUrl;
    }
}
