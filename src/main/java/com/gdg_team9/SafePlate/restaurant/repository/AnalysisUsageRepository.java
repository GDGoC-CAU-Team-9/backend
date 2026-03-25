package com.gdg_team9.SafePlate.restaurant.repository;

import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.restaurant.domain.AnalysisUsage;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface AnalysisUsageRepository extends JpaRepository<AnalysisUsage, Long> {
    @Modifying
    @Query(
            value = """
                    INSERT INTO analysis_usage (member_id, usage_date, usage_count)
                    VALUES (:memberId, :usageDate, 0)
                    ON DUPLICATE KEY UPDATE usage_count = usage_count
                    """,
            nativeQuery = true
    )
    void createIfAbsent(
            @Param("memberId") Long memberId,
            @Param("usageDate") LocalDate usageDate
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select usage from AnalysisUsage usage where usage.member = :member and usage.usageDate = :usageDate")
    Optional<AnalysisUsage> findByMemberAndUsageDateForUpdate(
            @Param("member") Member member,
            @Param("usageDate") LocalDate usageDate
    );
}
