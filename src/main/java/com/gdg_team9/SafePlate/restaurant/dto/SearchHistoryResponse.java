package com.gdg_team9.SafePlate.restaurant.dto;

import com.gdg_team9.SafePlate.restaurant.domain.RestaurantSearchResult;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "검색 이력 페이지 결과")
    public static class PageResult {
        @Schema(description = "검색 이력 목록")
        private List<Item> searchHistory;

        @Schema(description = "전체 페이지 수", example = "1")
        private int totalPages;

        @Schema(description = "전체 결과 수", example = "5")
        private long totalElements;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @Schema(description = "검색 이력 항목")
        public static class Item {
            @Schema(description = "이력 ID", example = "1")
            private long id;

            @Schema(description = "입력 이미지 URL", example = "[\"https://safeplate26.s3.ap-northeast-2.amazonaws.com/...\"]")
            private List<String> imageUrls;

            @Schema(description = "결과 이미지 URL", example = "[\"https://safeplate26.s3.ap-northeast-2.amazonaws.com/...\"]")
            private List<String> resultImageUrls;

            @Schema(description = "검색 결과")
            private RestaurantSearchResult searchResult;

            @Schema(description = "생성 시간", example = "2026-03-22T10:30:00")
            private LocalDateTime createdAt;
        }
    }
}
