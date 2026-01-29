package com.gdg_team9.SafePlate.restaurant.dto;

import lombok.*;

import java.util.List;

public class AiClientRequest {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchRequest {
        private String image_url;
        private List<String> avoid;
    }
}
