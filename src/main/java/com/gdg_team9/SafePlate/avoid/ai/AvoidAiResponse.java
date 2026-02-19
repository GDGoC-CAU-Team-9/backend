package com.gdg_team9.SafePlate.avoid.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AvoidAiResponse {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExtractResponse {
        @JsonProperty("avoid_text")
        private String avoidText;
    }
}
