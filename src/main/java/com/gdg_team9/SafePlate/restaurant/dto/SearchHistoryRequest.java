package com.gdg_team9.SafePlate.restaurant.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class SearchHistoryRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageRequest {
        @NotNull(message = "페이지는 필수입니다.")
        @Positive(message = "페이지가 올바르지 않습니다.")
        private Integer pageNumber;
    }
}
