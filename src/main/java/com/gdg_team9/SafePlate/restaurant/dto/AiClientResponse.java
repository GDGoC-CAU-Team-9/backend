package com.gdg_team9.SafePlate.restaurant.dto;

import lombok.*;

public class AiClientResponse {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchResponse {
        private MenuResult[] menus;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class MenuResult {
            private String name;
            private double score;
            private String comment;
        }
    }
}
