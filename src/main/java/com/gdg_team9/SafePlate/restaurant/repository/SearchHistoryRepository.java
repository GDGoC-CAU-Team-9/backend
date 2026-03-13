package com.gdg_team9.SafePlate.restaurant.repository;

import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.restaurant.domain.SearchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    Page<SearchHistory> findByMember(Member member, Pageable pageable);
    Optional<SearchHistory> findByIdAndMember(Long id, Member member);
}
