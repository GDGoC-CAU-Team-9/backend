package com.gdg_team9.SafePlate.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gdg_team9.SafePlate.file.dto.FileResponse;
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

        @JsonProperty("menu_lang")
        private String menuLang;

        @JsonProperty("presigned_url")
        private String presignedUrl;

        private List<String> avoid;

        private String lang;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchPreparedData {
        private List<String> imageUrls;
        private List<String> userAllergies;
        private FileResponse.PresignedUrlResponse preSignedUrl;
    }
}
