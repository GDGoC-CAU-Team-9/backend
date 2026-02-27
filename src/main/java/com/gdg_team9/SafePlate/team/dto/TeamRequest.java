package com.gdg_team9.SafePlate.team.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class TeamRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageRequest {
        @NotNull(message = "페이지는 필수입니다.")
        @Positive(message = "페이지가 올바르지 않습니다.")
        private Integer pageNumber;
    }

    /**
     * 팀명을 포함하는 공통 요청 DTO
     * (팀 생성, 팀 참여, 팀명 변경에 사용)
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamNameRequest {
        @NotNull(message = "팀명은 필수입니다.")
        @Size(max = 96, message = "팀명은 96자 이하로 입력해주세요.")
        private String teamName;
    }
}

