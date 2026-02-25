package com.gdg_team9.SafePlate.avoid.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class AvoidItemResponse {
    @Getter
    @Builder
    public static class ExtractedAvoidResponse {
        private List<String> avoidItems;

        private String confirmQuestion;
    }

    @Getter
    @Builder
    public static class MyAvoidResponse {
        private List<String> avoidItems;
    }
}
