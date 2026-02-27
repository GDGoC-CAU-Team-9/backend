package com.gdg_team9.SafePlate.team.controller;

import com.gdg_team9.SafePlate.api.ApiResponse;
import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.team.dto.TeamRequest;
import com.gdg_team9.SafePlate.team.dto.TeamResponse;
import com.gdg_team9.SafePlate.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    /**
     * 팀 생성
     */
    @PostMapping
    public ApiResponse<TeamResponse.TeamInfoWithMembersResponse> createTeam(
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody TeamRequest.TeamNameRequest request
    ) {
        TeamResponse.TeamInfoWithMembersResponse response =
                teamService.createTeam(member, request.getTeamName());
        return ApiResponse.onSuccess(response);
    }

    /**
     * 내 팀 목록 조회 (페이징), 팀 멤버 정보는 포함하지 않음
     */
    @GetMapping
    public ApiResponse<TeamResponse.PageResult> getMyTeams(
            @AuthenticationPrincipal Member member,
            @Valid @ModelAttribute TeamRequest.PageRequest request
    ) {
        TeamResponse.PageResult response =
                teamService.findTeamByMember(member, request.getPageNumber());
        return ApiResponse.onSuccess(response);
    }

    /**
     * 팀 조회 (팀멤버 ID로 조회, 팀 멤버 정보 포함)
     */
    @GetMapping("/{teamMemberId}")
    public ApiResponse<TeamResponse.TeamInfoWithMembersResponse> getTeamByTeamMemberId(
            @AuthenticationPrincipal Member member,
            @PathVariable("teamMemberId") Long teamMemberId
    ) {
        TeamResponse.TeamInfoWithMembersResponse response =
                teamService.findTeamByMemberAndTeamMemberId(member, teamMemberId);
        return ApiResponse.onSuccess(response);
    }

    /**
     * 팀 참여 (팀에 멤버 추가), 테스트용
     */
    @PostMapping("/{teamId}/members")
    public ApiResponse<TeamResponse.TeamInfoWithMembersResponse> joinTeam(
            @AuthenticationPrincipal Member member,
            @PathVariable Long teamId,
            @Valid @RequestBody TeamRequest.TeamNameRequest request
    ) {
        TeamResponse.TeamInfoWithMembersResponse response =
                teamService.joinTeam(member, teamId, request.getTeamName());
        return ApiResponse.onSuccess(response);
    }

    /**
     * 팀명 변경 (특정 팀멤버 수정)
     */
    @PatchMapping("/members/{teamMemberId}")
    public ApiResponse<TeamResponse.TeamInfoWithoutMembersResponse> renameTeam(
            @AuthenticationPrincipal Member member,
            @PathVariable Long teamMemberId,
            @Valid @RequestBody TeamRequest.TeamNameRequest request
    ) {
        TeamResponse.TeamInfoWithoutMembersResponse response =
                teamService.renameTeam(member, teamMemberId, request.getTeamName());
        return ApiResponse.onSuccess(response);
    }

    /**
     * 팀 나가기 (특정 팀멤버 삭제)
     */
    @DeleteMapping("/members/{teamMemberId}")
    public ApiResponse<TeamResponse.TeamInfoSimpleResponse> exitTeam(
            @AuthenticationPrincipal Member member,
            @PathVariable Long teamMemberId
    ) {
        TeamResponse.TeamInfoSimpleResponse response =
                teamService.exitTeam(member, teamMemberId);
        return ApiResponse.onSuccess(response);
    }
}

