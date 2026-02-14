package com.gdg_team9.SafePlate.team.service;

import com.gdg_team9.SafePlate.api.code.status.ErrorStatus;
import com.gdg_team9.SafePlate.exception.GeneralException;
import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.team.domain.Team;
import com.gdg_team9.SafePlate.team.domain.TeamMember;
import com.gdg_team9.SafePlate.team.dto.TeamResponse;
import com.gdg_team9.SafePlate.team.repository.TeamMemberRepository;
import com.gdg_team9.SafePlate.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {
    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;

    private static final int pageSize = 10;

    @Transactional
    public TeamResponse.TeamInfoWithMembersResponse createTeam(Member member, String teamName) {
        Team newTeam = Team.builder().build();

        TeamMember teamMember = TeamMember.builder()
                .member(member)
                .team(newTeam)
                .name(teamName)
                .build();

        teamRepository.save(newTeam);
        TeamMember saved = teamMemberRepository.save(teamMember);
        return toTeamInfoWithMembersResponse(saved);
    }

    @Transactional
    public TeamResponse.TeamInfoSimpleResponse exitTeam(Member member, long teamMemberId) {
        TeamMember teamMember = teamMemberRepository.findByIdAndMember(teamMemberId, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

        Team targetTeam = teamMember.removeTeam();
        teamMemberRepository.delete(teamMember);

        if (targetTeam.getTeamMembers().isEmpty()) {
            teamRepository.delete(targetTeam);
        }

        return toTeamInfoSimpleResponse(teamMember);
    }

    public TeamResponse.PageResult findTeamByMember(Member member, int pageNumber) {
        PageRequest page = PageRequest.of(
                pageNumber - 1,
                pageSize
        );

        Page<TeamMember> teamMembers = teamMemberRepository.findByMember(member, page);

        return TeamResponse.PageResult.builder()
                .teamMembers(
                        teamMembers.getContent().stream()
                                .map(this::toTeamInfoWithoutMembersResponse)
                                .toList()
                )
                .totalPages(teamMembers.getTotalPages())
                .totalElements(teamMembers.getTotalElements())
                .build();
    }

    public TeamResponse.TeamInfoWithMembersResponse findTeamByMemberAndTeamMemberId(Member member, long teamMemberId) {
        TeamMember teamMember = teamMemberRepository.findByIdAndMember(teamMemberId, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

        return toTeamInfoWithMembersResponse(teamMember);
    }

    @Transactional
    public TeamResponse.TeamInfoWithMembersResponse joinTeam(Member member, long teamId, String teamName) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

        TeamMember teamMember = TeamMember.builder()
                .member(member)
                .team(team)
                .name(teamName)
                .build();

        TeamMember saved = teamMemberRepository.save(teamMember);
        return toTeamInfoWithMembersResponse(saved);
    }

    @Transactional
    public TeamResponse.TeamInfoWithoutMembersResponse renameTeam(Member member, long teamMemberId, String newTeamName) {
        TeamMember teamMember = teamMemberRepository.findByIdAndMember(teamMemberId, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

        teamMember.setName(newTeamName);
        return toTeamInfoWithoutMembersResponse(teamMember);
    }

    private TeamResponse.TeamInfoSimpleResponse toTeamInfoSimpleResponse(
            TeamMember teamMember
    ) {
        return TeamResponse.TeamInfoSimpleResponse.builder()
                .teamName(teamMember.getName())
                .teamMemberId(teamMember.getId())
                .build();
    }

    private TeamResponse.TeamInfoWithMembersResponse toTeamInfoWithMembersResponse(
            TeamMember teamMember
    ) {
        return TeamResponse.TeamInfoWithMembersResponse.builder()
                .teamId(teamMember.getTeam().getId())
                .teamName(teamMember.getName())
                .teamMemberId(teamMember.getId())
                .createdAt(teamMember.getTeam().getCreatedAt())
                .updatedAt(teamMember.getTeam().getUpdatedAt())
                .members(
                        teamMember.getTeam().getTeamMembers().stream()
                                .map(m -> m.getMember().getName())
                                .toList()
                )
                .build();
    }

    private TeamResponse.TeamInfoWithoutMembersResponse toTeamInfoWithoutMembersResponse(
            TeamMember teamMember
    ) {
        return TeamResponse.TeamInfoWithoutMembersResponse.builder()
                .teamId(teamMember.getTeam().getId())
                .teamName(teamMember.getName())
                .teamMemberId(teamMember.getId())
                .createdAt(teamMember.getTeam().getCreatedAt())
                .updatedAt(teamMember.getTeam().getUpdatedAt())
                .build();
    }
}

