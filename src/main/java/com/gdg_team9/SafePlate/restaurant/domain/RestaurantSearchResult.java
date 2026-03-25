package com.gdg_team9.SafePlate.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "레스토랑 검색 결과")
public class RestaurantSearchResult {
    @Schema(description = "추출된 음식 항목", example = "[\"Enchiladas Verdes\", \"Media Porción\"]")
    @JsonProperty("items_extracted")
    private List<String> itemsExtracted;

    @Schema(description = "검색 결과 음식 항목 목록")
    private List<Item> items;

    @Schema(description = "최고 점수 음식")
    private Item best;

    @Schema(description = "응답 시간 정보")
    @JsonProperty("timings_ms")
    private Timings timingsMs;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "음식 항목")
    public static class Item {
        @Schema(description = "메뉴명", example = "Media Porción")
        private String menu;

        @Schema(description = "원본 메뉴명", example = "Media Porción")
        @JsonProperty("menu_original")
        private String menuOriginal;

        @Schema(description = "안전성 점수 (0-100)", example = "62")
        private int score;

        @Schema(description = "위험도 (0-100)", example = "10")
        private int risk;

        @Schema(description = "신뢰도 (0-1)", example = "0.3")
        private double confidence;

        @Schema(description = "일치하는 기피 재료", example = "[]")
        @JsonProperty("matched_avoid")
        private List<String> matchedAvoid;

        @Schema(description = "의심되는 재료", example = "[\"쌀\", \"닭고기\", \"채소\"]")
        @JsonProperty("suspected_ingredients")
        private List<String> suspectedIngredients;

        @Schema(description = "안전/위험 사유", example = "재료가 다양해서 확인이 어려움.")
        private String reason;
    }

    @Getter
    @Setter
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