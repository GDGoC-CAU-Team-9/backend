package com.gdg_team9.SafePlate.restaurant.domain;

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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(
        name = "analysis_usage",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_analysis_usage_member_date",
                columnNames = {"member_id", "usage_date"}
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnalysisUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "usage_date", nullable = false)
    private LocalDate usageDate;

    @Column(name = "usage_count", nullable = false)
    private int usageCount;

    @Builder(access = AccessLevel.PRIVATE)
    private AnalysisUsage(Member member, LocalDate usageDate, int usageCount) {
        this.member = member;
        this.usageDate = usageDate;
        this.usageCount = usageCount;
    }

    public static AnalysisUsage initialize(Member member, LocalDate usageDate) {
        return AnalysisUsage.builder()
                .member(member)
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
