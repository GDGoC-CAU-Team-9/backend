package com.gdg_team9.SafePlate.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class TeamResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "팀 정보 (멤버 포함)")
    public static class TeamInfoWithMembersResponse {
        @Schema(description = "팀 멤버 ID, 동일한 팀이라도 member별로 ID가 다름", example = "8")
        private long teamMemberId;

        @Schema(description = "팀 ID, 같은 팀이면 이 ID는 동일함", example = "4")
        private long teamId;

        @Schema(description = "팀명", example = "내가보는 팀명")
        private String teamName;

        @Schema(description = "팀 멤버 목록", example = "[\"b@b.com\", \"a@b.com\"]")
        private List<String> members;

        @Schema(description = "생성 시간", example = "2026-03-06T00:04:42.286691")
        private LocalDateTime createdAt;

        @Schema(description = "수정 시간", example = "2026-03-06T00:04:42.286691")
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "팀 정보 (멤버 미포함)")
    public static class TeamInfoWithoutMembersResponse {
        @Schema(description = "팀 멤버 ID, 동일한 팀이라도 member별로 ID가 다름", example = "8")
        private long teamMemberId;

        @Schema(description = "팀 ID, 같은 팀이면 이 ID는 동일함", example = "4")
        private long teamId;

        @Schema(description = "팀명", example = "내가보는 팀명")
        private String teamName;

        @Schema(description = "생성 시간", example = "2026-03-06T00:04:42.286691")
        private LocalDateTime createdAt;

        @Schema(description = "수정 시간", example = "2026-03-06T00:04:42.286691")
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "팀 정보 (간단함)")
    public static class TeamInfoSimpleResponse {
        @Schema(description = "팀 멤버 ID, 동일한 팀이라도 member별로 ID가 다름", example = "8")
        private long teamMemberId;

        @Schema(description = "팀명", example = "내가보는 팀명")
        private String teamName;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "팀 페이지 결과")
    public static class PageResult {
        @Schema(description = "팀 정보 목록")
        private List<TeamInfoWithoutMembersResponse> teamMembers;

        @Schema(description = "전체 페이지 수", example = "1")
        private int totalPages;

        @Schema(description = "전체 결과 수", example = "1")
        private long totalElements;
    }
}
