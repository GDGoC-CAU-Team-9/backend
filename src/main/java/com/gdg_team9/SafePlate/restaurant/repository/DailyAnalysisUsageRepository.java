package com.gdg_team9.SafePlate.restaurant.repository;

import com.gdg_team9.SafePlate.restaurant.domain.DailyAnalysisUsage;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyAnalysisUsageRepository extends JpaRepository<DailyAnalysisUsage, Long> {
    @Modifying
    @Query(
            value = """
                    INSERT INTO daily_analysis_usage (usage_date, usage_count)
                    VALUES (:usageDate, 0)
                    ON DUPLICATE KEY UPDATE usage_count = usage_count
                    """,
            nativeQuery = true
    )
    void createIfAbsent(@Param("usageDate") LocalDate usageDate);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select usage from DailyAnalysisUsage usage where usage.usageDate = :usageDate")
    Optional<DailyAnalysisUsage> findByUsageDateForUpdate(@Param("usageDate") LocalDate usageDate);

    Optional<DailyAnalysisUsage> findByUsageDate(LocalDate usageDate);
}
