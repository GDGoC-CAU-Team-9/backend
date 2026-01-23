package com.gdg_team9.SafePlate.allergy.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class AllergyRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUserAllergyRequest {
        @NotNull(message = "알레르기 ID는 필수 입력 항목입니다.")
        private List<Long> allergyIds;
    }
}
