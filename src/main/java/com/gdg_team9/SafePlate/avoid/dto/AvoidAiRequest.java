package com.gdg_team9.SafePlate.avoid.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AvoidAiRequest {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExtractRequest {
        @JsonProperty("user_text")
        private String userText;

        private String lang;
    }
}
