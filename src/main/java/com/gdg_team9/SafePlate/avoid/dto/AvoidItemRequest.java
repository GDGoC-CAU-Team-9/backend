package com.gdg_team9.SafePlate.avoid.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class AvoidItemRequest {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TextRequest {
        @NotBlank(message = "기피 정보는 필수 입력 항목입니다.")
        private String text;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveRequest {
        @NotNull(message = "기피 재료는 필수 입력 항목입니다.")
        private List<String> items;
    }
}
