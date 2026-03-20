package com.gdg_team9.SafePlate.restaurant.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class RestaurantRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchRequest {
        @NotNull(message = "사진 정보가 올바르지 않습니다.")
        private List<Long> ids; // TODO 변수명 수정

        // nullable
        private Long teamMemberId;

        @NotNull(message = "메뉴판 언어는 필수 입력 항목입니다.")
        private String menuLang;
    }
}
