package com.gdg_team9.SafePlate.allergy.dto;

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
    public static class AllergyListResponse {
        private List<AllergyDTO> allergies;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AllergyDTO {
        private Long id;
        private String name;
    }
}


