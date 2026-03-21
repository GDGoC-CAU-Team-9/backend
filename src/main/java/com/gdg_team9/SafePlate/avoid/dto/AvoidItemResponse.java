package com.gdg_team9.SafePlate.avoid.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class AvoidItemResponse {
    @Getter
    @Builder
    @Schema(description = "추출된 기피 음식 응답")
    public static class ExtractedAvoidResponse {
        @Schema(description = "기피 음식 목록", example = "[\"egg\", \"milk\", \"peanut\"]")
        private List<String> avoidItems;

        @Schema(description = "사용자 확인 질문", example = "이 음식들을 피해야 할까요?")
        private String confirmQuestion;
    }

    @Getter
    @Builder
    @Schema(description = "내 기피 음식 응답")
    public static class MyAvoidResponse {
        @Schema(description = "기피 음식 목록", example = "[\"egg\", \"milk\"]")
        private List<String> avoidItems;
    }
}
