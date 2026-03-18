package com.gdg_team9.SafePlate.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

public class RestaurantResponse {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchResult {
        @JsonProperty("items_extracted")
        private List<String> itemsExtracted;

        private List<Item> items;

        private Item best;

        @JsonProperty("result_image_urls")
        private List<String> resultImageUrls;

        @JsonProperty("timings_ms")
        private Timings timingsMs;

        @Getter
        @Setter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Item {
            private String menu;

            @JsonProperty("menu_original")
            private String menuOriginal;

            private int score;

            private int risk;

            private double confidence;

            @JsonProperty("matched_avoid")
            private List<String> matchedAvoid;

            @JsonProperty("suspected_ingredients")
            private List<String> suspectedIngredients;

            private String reason;
        }

        @Getter
        @Setter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Timings {
            @JsonProperty("image_load")
            private int imageLoad;

            private int extract;

            @JsonProperty("risk_assess")
            private int riskAssess;

            @JsonProperty("score_policy")
            private int scorePolicy;

            private int total;
        }
    }
}
