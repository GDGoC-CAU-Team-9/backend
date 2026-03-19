package com.gdg_team9.SafePlate.avoid.dto;

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
    public static class PresetInfoResponse {
        private Long presetId;
        private String presetName;
        private List<String> items;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PresetListResponse {
        private List<PresetInfoResponse> presets;
    }
}
