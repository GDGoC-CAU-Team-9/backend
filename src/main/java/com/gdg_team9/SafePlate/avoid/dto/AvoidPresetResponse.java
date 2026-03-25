package com.gdg_team9.SafePlate.avoid.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class AvoidPresetResponse {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "프리셋 상세 정보")
    public static class PresetInfoResponse {
        @Schema(description = "프리셋 ID", example = "1")
        private Long presetId;

        @Schema(description = "프리셋명", example = "비건")
        private String presetName;

        @Schema(description = "음식 항목 목록", example = "[\"동물성 식품\", \"동물 유래 식품 첨가물\", \"동물성 성분\"]")
        private List<String> items;

        @Schema(description = "생성 시간", example = "2026-03-19T03:27:48")
        private LocalDateTime createdAt;

        @Schema(description = "수정 시간", example = "2026-03-19T03:27:48")
        private LocalDateTime updatedAt;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "프리셋 목록 응답")
    public static class PresetListResponse {
        @Schema(description = "프리셋 정보 목록")
        private List<PresetInfoResponse> presets;
    }
}
