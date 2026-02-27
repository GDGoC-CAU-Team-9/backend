package com.gdg_team9.SafePlate.team.dto;

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
    public static class TeamInfoWithMembersResponse {
        private long teamMemberId;
        private long teamId;
        private String teamName; // 내가 보는 팀의 이름
        private List<String> members;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamInfoWithoutMembersResponse {
        private long teamMemberId;
        private long teamId;
        private String teamName; // 내가 보는 팀의 이름
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamInfoSimpleResponse {
        private long teamMemberId;
        private String teamName; // 내가 보는 팀의 이름
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageResult {
        private List<TeamInfoWithoutMembersResponse> teamMembers;
        private int totalPages;
        private long totalElements;
    }
}

