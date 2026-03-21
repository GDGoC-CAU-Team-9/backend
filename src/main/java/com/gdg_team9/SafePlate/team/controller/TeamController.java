package com.gdg_team9.SafePlate.team.controller;

import com.gdg_team9.SafePlate.api.CommonResponse;
import com.gdg_team9.SafePlate.config.AuthErrorResponses;
import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.team.dto.TeamRequest;
import com.gdg_team9.SafePlate.team.dto.TeamResponse;
import com.gdg_team9.SafePlate.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
@Tag(name = "Team", description = "팀 관리 관련 API")
public class TeamController {
    private final TeamService teamService;

    /**
     * 내 팀 목록 조회 (페이징), 팀 멤버 정보는 포함하지 않음
     */
    @GetMapping
    @Operation(summary = "내 팀 목록 조회", description = "사용자가 참여한 팀 목록을 페이지별로 조회합니다 (팀 멤버 정보 미포함)")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @AuthErrorResponses
    public CommonResponse<TeamResponse.PageResult> getMyTeams(
            @AuthenticationPrincipal Member member,
            @Valid @ModelAttribute TeamRequest.PageRequest request
    ) {
        TeamResponse.PageResult response =
                teamService.findTeamByMember(member, request.getPageNumber());
        return CommonResponse.onSuccess(response);
    }

    /**
     * 팀 생성
     */
    @PostMapping
    @Operation(summary = "팀 생성", description = "새로운 팀을 생성합니다")
    @ApiResponse(responseCode = "200", description = "생성 성공")
    @AuthErrorResponses
    public CommonResponse<TeamResponse.TeamInfoWithMembersResponse> createTeam(
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody TeamRequest.TeamNameRequest request
    ) {
        TeamResponse.TeamInfoWithMembersResponse response =
                teamService.createTeam(member, request.getTeamName());
        return CommonResponse.onSuccess(response);
    }

    /**
     * 팀 나가기 (특정 팀멤버 삭제)
     */
    @DeleteMapping("/members/{teamMemberId}")
    @Operation(summary = "팀 탈퇴", description = "특정 팀에서 탈퇴합니다")
    @ApiResponse(responseCode = "200", description = "탈퇴 성공")
    @ApiResponse(responseCode = "404", description = "팀 멤버를 찾을 수 없음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                              "isSuccess": false,
                              "code": "GROUP4004",
                              "message": "그룹을 찾을 수 없습니다.",
                              "success": false
                            }
                            """)))
    @AuthErrorResponses
    public CommonResponse<TeamResponse.TeamInfoSimpleResponse> exitTeam(
            @AuthenticationPrincipal Member member,
            @PathVariable Long teamMemberId
    ) {
        TeamResponse.TeamInfoSimpleResponse response =
                teamService.exitTeam(member, teamMemberId);
        return CommonResponse.onSuccess(response);
    }

    /**
     * 팀 참여 (팀에 멤버 추가)
     */
    @PostMapping("/join")
    @Operation(summary = "팀 참여", description = "기존 팀에 참여합니다")
    @ApiResponse(responseCode = "200", description = "참여 성공")
    @AuthErrorResponses
    public CommonResponse<TeamResponse.TeamInfoWithMembersResponse> joinTeam(
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody TeamRequest.TeamJoinRequest request
    ) {
        TeamResponse.TeamInfoWithMembersResponse response =
                teamService.joinTeam(member, request);
        return CommonResponse.onSuccess(response);
    }

    /**
     * 팀명 변경 (특정 팀멤버 수정)
     */
    @PatchMapping("/members/{teamMemberId}")
    @Operation(summary = "팀명 변경", description = "팀의 이름을 변경합니다")
    @ApiResponse(responseCode = "200", description = "변경 성공")
    @ApiResponse(responseCode = "404", description = "팀 멤버를 찾을 수 없음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                              "isSuccess": false,
                              "code": "GROUP4004",
                              "message": "그룹을 찾을 수 없습니다.",
                              "success": false
                            }
                            """)))
    @AuthErrorResponses
    public CommonResponse<TeamResponse.TeamInfoWithoutMembersResponse> renameTeam(
            @AuthenticationPrincipal Member member,
            @PathVariable Long teamMemberId,
            @Valid @RequestBody TeamRequest.TeamNameRequest request
    ) {
        TeamResponse.TeamInfoWithoutMembersResponse response =
                teamService.renameTeam(member, teamMemberId, request.getTeamName());
        return CommonResponse.onSuccess(response);
    }

    /**
     * 팀 조회 (팀멤버 ID로 조회, 팀 멤버 정보 포함)
     */
    @GetMapping("/{teamMemberId}")
    @Operation(summary = "팀 상세 조회", description = "팀 멤버 ID로 팀의 상세 정보를 조회합니다 (팀 멤버 정보 포함)")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @ApiResponse(responseCode = "404", description = "팀 멤버를 찾을 수 없음",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                              "isSuccess": false,
                              "code": "GROUP4004",
                              "message": "그룹을 찾을 수 없습니다.",
                              "success": false
                            }
                            """)))
    @AuthErrorResponses
    public CommonResponse<TeamResponse.TeamInfoWithMembersResponse> getTeamByTeamMemberId(
            @AuthenticationPrincipal Member member,
            @PathVariable("teamMemberId") Long teamMemberId
    ) {
        TeamResponse.TeamInfoWithMembersResponse response =
                teamService.findTeamByMemberAndTeamMemberId(member, teamMemberId);
        return CommonResponse.onSuccess(response);
    }
}
