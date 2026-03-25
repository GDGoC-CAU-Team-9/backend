package com.gdg_team9.SafePlate.restaurant.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(
        name = "daily_analysis_usage",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_daily_analysis_usage_date",
                columnNames = "usage_date"
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyAnalysisUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "usage_date", nullable = false)
    private LocalDate usageDate;

    @Column(name = "usage_count", nullable = false)
    private int usageCount;

    @Builder(access = AccessLevel.PRIVATE)
    private DailyAnalysisUsage(LocalDate usageDate, int usageCount) {
        this.usageDate = usageDate;
        this.usageCount = usageCount;
    }

    public static DailyAnalysisUsage initialize(LocalDate usageDate) {
        return DailyAnalysisUsage.builder()
                .usageDate(usageDate)
                .usageCount(0)
                .build();
    }

    public boolean hasReachedLimit(int limit) {
        return usageCount >= limit;
    }

    public void increaseUsage() {
        usageCount += 1;
    }
}
