package com.gdg_team9.SafePlate.restaurant.dto;

import lombok.*;

import java.util.List;

public class AiClientRequest {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchRequest {
        private String keyword;
        private List<String> dislikeIngredients;
        private double lat;
        private double lng;
    }
}
