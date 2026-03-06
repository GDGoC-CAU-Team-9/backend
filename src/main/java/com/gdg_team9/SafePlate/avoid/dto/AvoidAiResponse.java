package com.gdg_team9.SafePlate.avoid.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class AvoidAiResponse {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExtractResponse {
        private List<String> candidates;

        @JsonProperty("confirm_question")
        private String confirmQuestion;
    }
}
