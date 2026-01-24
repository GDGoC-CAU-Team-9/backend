package com.gdg_team9.SafePlate.restaurant.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class RestaurantRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchRequest {
        @NotNull(message = "키워드 형식이 올바르지 않습니다.")
        private String keyword;

        @Min(value = -90, message = "위도 형식이 올바르지 않습니다.")
        @Max(value = 90, message = "위도 형식이 올바르지 않습니다.")
        private double lat;

        @Min(value = -180, message = "경도 형식이 올바르지 않습니다.")
        @Max(value = 180, message = "경도 형식이 올바르지 않습니다.")
        private double lng;
    }
}
