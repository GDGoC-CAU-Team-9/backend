package com.gdg_team9.SafePlate.allergy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


public class AllergyResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "알레르기 목록 응답")
    public static class AllergyListResponse {
        @Schema(description = "알레르기 정보 목록")
        private List<AllergyDTO> allergies;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "알레르기 정보")
    public static class AllergyDTO {
        @Schema(description = "알레르기 ID", example = "1")
        private Long id;

        @Schema(description = "알레르기명", example = "우유")
        private String name;
    }
}
