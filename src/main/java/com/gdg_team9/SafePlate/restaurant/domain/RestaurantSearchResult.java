package com.gdg_team9.SafePlate.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantSearchResult {
    @JsonProperty("items_extracted")
    private List<String> itemsExtracted;

    private List<Item> items;

    private Item best;

    @JsonProperty("timings_ms")
    private Timings timingsMs;

    @Getter
    @Setter
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