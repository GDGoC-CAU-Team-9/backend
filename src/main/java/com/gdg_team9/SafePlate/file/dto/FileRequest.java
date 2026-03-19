package com.gdg_team9.SafePlate.file.dto;

import com.gdg_team9.SafePlate.file.domain.FileStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

public class FileRequest {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PresignedUrlRequest {
        // 경로를 추가할 일이 있다면 그때 수정 예정
        @NotNull(message = "파일 경로는 필수입니다.")
        @Pattern(regexp = "^(menu_board_request)$", message = "올바른 경로가 아닙니다.")
        private String path;

        @NotNull(message = "파일 확장자는 필수입니다.")
        @Pattern(
                regexp = "^(jpe?g|png|gif|webp|svg|bmp|tiff?|ico)$",
                message = "올바른 확장자가 아닙니다."
        )
        private String fileType;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatchStatusRequest {
        @NotNull(message = "파일 상태는 필수입니다.")
        private FileStatus fileStatus;
    }
}
