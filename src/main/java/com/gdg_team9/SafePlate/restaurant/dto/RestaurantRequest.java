package com.gdg_team9.SafePlate.restaurant.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

public class RestaurantRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchRequest {
        @NotNull(message = "사진 정보가 올바르지 않습니다.")
        private List<Long> ids;
    }
}
