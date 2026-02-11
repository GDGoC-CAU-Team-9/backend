package com.gdg_team9.SafePlate.restaurant.domain;

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
    private List<String> items_extracted;
    private List<Item> items;
    private Item best;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private String menu;
        private int score;
        private int risk;
        private double confidence;
        private List<String> matched_avoid;
        private List<String> suspected_ingredients;
        private String reason_ko;
    }
}