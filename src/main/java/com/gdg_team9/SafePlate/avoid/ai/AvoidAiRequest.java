package com.gdg_team9.SafePlate.avoid.ai;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AvoidAiRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExtractRequest{
        private String text;
    }
}
