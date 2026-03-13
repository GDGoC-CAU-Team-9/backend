package com.gdg_team9.SafePlate.team.repository;

import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.team.domain.TeamMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    Optional<TeamMember> findByMemberAndTeamId(Member member, Long teamId);

    boolean existsByMemberAndTeamId(Member member, Long teamId);

    @Query("""
            SELECT tm FROM TeamMember tm
            JOIN FETCH tm.team t
            LEFT JOIN FETCH t.teamMembers tm2
            LEFT JOIN FETCH tm2.member
            WHERE tm.id = :id AND tm.member = :member
            """)
        // entity graph로 fetch join 불가
    Optional<TeamMember> findByIdAndMember(@Param("id") Long id, @Param("member") Member member);

    @EntityGraph(attributePaths = {"team"})
    Optional<TeamMember> findByIdAndMemberEmail(Long id, String memberEmail);

    @EntityGraph(attributePaths = {"team"})
        // paging 시 일대다 fetch join을 할 수 없음
    Page<TeamMember> findByMember(Member member, Pageable pageable);

    @Query("""
            SELECT tm.member.email FROM TeamMember tm
            WHERE tm.team.id = :teamId
            """)
    List<String> findMemberEmailsByTeamId(@Param("teamId") Long teamId);
}
