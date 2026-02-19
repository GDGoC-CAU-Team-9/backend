package com.gdg_team9.SafePlate.avoid.dto;

import lombok.Builder;
import lombok.Getter;

public class AvoidItemResponse {
    @Getter
    @Builder
    public static class MyAvoidResponse {
        private String avoidText;
    }
}
