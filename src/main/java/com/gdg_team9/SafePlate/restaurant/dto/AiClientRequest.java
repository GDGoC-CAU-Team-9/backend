package com.gdg_team9.SafePlate.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

public class AiClientRequest {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchRequest {
        @JsonProperty("image_url")
        private String imageUrl;

        @JsonProperty("presigned_url")
        private String presignedUrl;

        private List<String> avoid;

        private String lang;
    }
}
