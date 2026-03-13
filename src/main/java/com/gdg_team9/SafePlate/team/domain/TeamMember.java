package com.gdg_team9.SafePlate.team.domain;

import com.gdg_team9.SafePlate.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "team_member",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_team_member_member_team",
                        columnNames = {"member_id", "team_id"}
                )
        }
)
@Getter
@NoArgsConstructor
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(name = "name", length = 96)
    @Setter
    private String name;

    @Builder
    public TeamMember(Member member, Team team, String name) {
        this.member = member;
        this.team = team;
        this.name = name;

        team.getTeamMembers().add(this);
    }

    /**
     * 소속된 팀 삭제 시행, 이 객체 삭제 시에만 시행해야 함
     *
     * @return 기존에 속해 있던 팀
     */
    public Team removeTeam() {
        Team team1 = team;
        team.getTeamMembers().remove(this);
        team = null; // 삭제 대상인 이 객체가 저장되는 상황 방지
        return team1;
    }
}

