package com.gdg_team9.SafePlate.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class RestaurantResponse {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "레스토랑 검색 결과")
    public static class SearchResult {
        @Schema(description = "추출된 음식 항목", example = "[\"Enchiladas Verdes\", \"Media Porción\", \"Barbacoa\"]")
        @JsonProperty("items_extracted")
        private List<String> itemsExtracted;

        @Schema(description = "검색 결과 목록")
        private List<Item> items;

        @Schema(description = "최고 점수 음식")
        private Item best;

        @Schema(description = "결과 이미지 URL 목록", example = "[\"https://safeplate26.s3.ap-northeast-2.amazonaws.com/...\"]")
        @JsonProperty("result_image_urls")
        private List<String> resultImageUrls;

        @Schema(description = "응답 시간 정보")
        @JsonProperty("timings_ms")
        private Timings timingsMs;

        @Getter
        @Setter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @Schema(description = "음식 항목")
        public static class Item {
            @Schema(description = "메뉴명", example = "Enchiladas Verdes")
            private String menu;

            @Schema(description = "원본 메뉴명", example = "Enchiladas Verdes")
            @JsonProperty("menu_original")
            private String menuOriginal;

            @Schema(description = "안전성 점수 (0-100)", example = "36")
            private int score;

            @Schema(description = "위험도 (0-100)", example = "52")
            private int risk;

            @Schema(description = "신뢰도 (0-1)", example = "0.7")
            private double confidence;

            @Schema(description = "일치하는 기피 재료", example = "[]")
            @JsonProperty("matched_avoid")
            private List<String> matchedAvoid;

            @Schema(description = "의심되는 재료", example = "[\"tortillas\", \"cheese\", \"enchilada sauce\"]")
            @JsonProperty("suspected_ingredients")
            private List<String> suspectedIngredients;

            @Schema(description = "안전/위험 사유", example = "기피 재료 근거 부족")
            private String reason;
        }

        @Getter
        @Setter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @Schema(description = "응답 시간 정보 (밀리초)")
        public static class Timings {
            @Schema(description = "이미지 로드 시간", example = "7230")
            @JsonProperty("image_load")
            private int imageLoad;

            @Schema(description = "추출 시간", example = "3463")
            private int extract;

            @Schema(description = "위험도 평가 시간", example = "10427")
            @JsonProperty("risk_assess")
            private int riskAssess;

            @Schema(description = "점수 정책 적용 시간", example = "3")
            @JsonProperty("score_policy")
            private int scorePolicy;

            @Schema(description = "총 응답 시간", example = "21123")
            private int total;
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "분석 사용량/남은 횟수")
    public static class AnalysisUsageStatus {
        @Schema(description = "기준 날짜(Asia/Seoul)", example = "2026-03-30")
        @JsonProperty("usage_date")
        private String usageDate;

        @Schema(description = "개인 일일 제한", example = "4")
        @JsonProperty("member_daily_limit")
        private int memberDailyLimit;

        @Schema(description = "개인 오늘 사용 횟수", example = "2")
        @JsonProperty("member_used")
        private int memberUsed;

        @Schema(description = "개인 오늘 남은 횟수", example = "2")
        @JsonProperty("member_remaining")
        private int memberRemaining;

        @Schema(description = "전체 일일 제한", example = "100")
        @JsonProperty("global_daily_limit")
        private int globalDailyLimit;

        @Schema(description = "전체 오늘 사용 횟수", example = "61")
        @JsonProperty("global_used")
        private int globalUsed;

        @Schema(description = "전체 오늘 남은 횟수", example = "39")
        @JsonProperty("global_remaining")
        private int globalRemaining;
    }
}
