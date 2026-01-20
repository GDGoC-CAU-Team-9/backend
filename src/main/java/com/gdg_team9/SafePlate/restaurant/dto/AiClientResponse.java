package com.gdg_team9.SafePlate.restaurant.dto;

import lombok.*;

public class AiClientResponse {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchResponse {
        private RestaurantResult[] restaurants;
        private int count;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class RestaurantResult {
            private String placeId;
            private double score;
            private String comment;
        }
    }
}
