package com.gdg_team9.SafePlate.avoid.repository;

import com.gdg_team9.SafePlate.avoid.domain.AvoidItem;
import com.gdg_team9.SafePlate.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AvoidItemRepository extends JpaRepository<AvoidItem, Long> {
    List<AvoidItem> findAllByMemberIn(Collection<Member> members);
}
