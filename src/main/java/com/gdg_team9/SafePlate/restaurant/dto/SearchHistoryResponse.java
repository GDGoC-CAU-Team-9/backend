package com.gdg_team9.SafePlate.restaurant.dto;

import com.gdg_team9.SafePlate.restaurant.domain.RestaurantSearchResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class SearchHistoryResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageResult {
        private List<Item> searchHistory;
        private int totalPages;
        private long totalElements;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Item {
            private long id;
            private List<String> imageUrls;
            private RestaurantSearchResult searchResult;
            private LocalDateTime createdAt;
        }
    }
}
