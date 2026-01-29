package com.gdg_team9.SafePlate.restaurant.dto;

import lombok.*;

import java.util.List;

public class AiClientResponse {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchResponse {
        private List<String> items_extracted;
        private List<MenuResult> items;
        private MenuResult best;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class MenuResult {
            private String menu;
            private int score;
            private int risk;
            private double confidence;
            private List<String> matched_avoid;
            private List<String> suspected_ingredients;
            private String reason_ko;
        }
    }
}
