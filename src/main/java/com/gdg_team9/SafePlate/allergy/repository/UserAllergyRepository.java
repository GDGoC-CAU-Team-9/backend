package com.gdg_team9.SafePlate.allergy.repository;

import com.gdg_team9.SafePlate.allergy.domain.UserAllergy;
import com.gdg_team9.SafePlate.member.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAllergyRepository extends JpaRepository<UserAllergy, Long> {
    @EntityGraph(attributePaths = {"allergy"})
    List<UserAllergy> findByMember(Member member);
    void deleteByMember(Member member);
}
