package com.gdg_team9.SafePlate.avoid.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AvoidItemRequest {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TextRequest {
        @NotBlank(message = "기피 정보는 필수 입력 항목입니다.")
        private String text;
    }
}
