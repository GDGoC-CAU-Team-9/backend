package com.gdg_team9.SafePlate.restaurant.service;

import com.gdg_team9.SafePlate.api.code.status.ErrorStatus;
import com.gdg_team9.SafePlate.exception.GeneralException;
import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.restaurant.domain.AnalysisUsage;
import com.gdg_team9.SafePlate.restaurant.domain.DailyAnalysisUsage;
import com.gdg_team9.SafePlate.restaurant.dto.RestaurantResponse;
import com.gdg_team9.SafePlate.restaurant.repository.AnalysisUsageRepository;
import com.gdg_team9.SafePlate.restaurant.repository.DailyAnalysisUsageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalysisUsageServiceTest {

    @Mock
    private AnalysisUsageRepository analysisUsageRepository;

    @Mock
    private DailyAnalysisUsageRepository dailyAnalysisUsageRepository;

    @Mock
    private Member member;

    private AnalysisUsageService analysisUsageService;

    @BeforeEach
    void setUp() {
        analysisUsageService = new AnalysisUsageService(
                analysisUsageRepository,
                dailyAnalysisUsageRepository
        );
        lenient().when(member.getId()).thenReturn(1L);
    }

    @Test
    void consumeDailyQuota_increasesMemberAndGlobalUsage_whenWithinLimit() {
        AnalysisUsage memberUsage = memberUsage(member, 3);
        DailyAnalysisUsage globalUsage = globalUsage(99);

        when(analysisUsageRepository.findByMemberAndUsageDateForUpdate(eq(member), any(LocalDate.class)))
                .thenReturn(Optional.of(memberUsage));
        when(dailyAnalysisUsageRepository.findByUsageDateForUpdate(any(LocalDate.class)))
                .thenReturn(Optional.of(globalUsage));

        analysisUsageService.consumeDailyQuota(member);

        assertEquals(4, memberUsage.getUsageCount());
        assertEquals(100, globalUsage.getUsageCount());
        verify(analysisUsageRepository).createIfAbsent(eq(1L), any(LocalDate.class));
        verify(dailyAnalysisUsageRepository).createIfAbsent(any(LocalDate.class));
    }

    @Test
    void consumeDailyQuota_throwsDailyLimitExceeded_whenMemberAlreadyAtLimit() {
        AnalysisUsage memberUsage = memberUsage(member, 4);
        DailyAnalysisUsage globalUsage = globalUsage(10);

        when(analysisUsageRepository.findByMemberAndUsageDateForUpdate(eq(member), any(LocalDate.class)))
                .thenReturn(Optional.of(memberUsage));
        when(dailyAnalysisUsageRepository.findByUsageDateForUpdate(any(LocalDate.class)))
                .thenReturn(Optional.of(globalUsage));

        GeneralException exception = assertThrows(
                GeneralException.class,
                () -> analysisUsageService.consumeDailyQuota(member)
        );

        assertSame(ErrorStatus.ANALYSIS_DAILY_LIMIT_EXCEEDED, exception.getCode());
        assertEquals(4, memberUsage.getUsageCount());
        assertEquals(10, globalUsage.getUsageCount());
    }

    @Test
    void consumeDailyQuota_throwsGlobalLimitExceeded_whenGlobalAlreadyAtLimit() {
        AnalysisUsage memberUsage = memberUsage(member, 3);
        DailyAnalysisUsage globalUsage = globalUsage(100);

        when(analysisUsageRepository.findByMemberAndUsageDateForUpdate(eq(member), any(LocalDate.class)))
                .thenReturn(Optional.of(memberUsage));
        when(dailyAnalysisUsageRepository.findByUsageDateForUpdate(any(LocalDate.class)))
                .thenReturn(Optional.of(globalUsage));

        GeneralException exception = assertThrows(
                GeneralException.class,
                () -> analysisUsageService.consumeDailyQuota(member)
        );

        assertSame(ErrorStatus.ANALYSIS_GLOBAL_DAILY_LIMIT_EXCEEDED, exception.getCode());
        assertEquals(3, memberUsage.getUsageCount());
        assertEquals(100, globalUsage.getUsageCount());
    }

    @Test
    void getDailyQuotaStatus_returnsRemainingCounts() {
        AnalysisUsage memberUsage = memberUsage(member, 2);
        DailyAnalysisUsage globalUsage = globalUsage(40);

        when(analysisUsageRepository.findByMemberAndUsageDate(eq(member), any(LocalDate.class)))
                .thenReturn(Optional.of(memberUsage));
        when(dailyAnalysisUsageRepository.findByUsageDate(any(LocalDate.class)))
                .thenReturn(Optional.of(globalUsage));

        RestaurantResponse.AnalysisUsageStatus status = analysisUsageService.getDailyQuotaStatus(member);

        assertNotNull(status);
        assertEquals(4, status.getMemberDailyLimit());
        assertEquals(2, status.getMemberUsed());
        assertEquals(2, status.getMemberRemaining());
        assertEquals(100, status.getGlobalDailyLimit());
        assertEquals(40, status.getGlobalUsed());
        assertEquals(60, status.getGlobalRemaining());
    }

    private static AnalysisUsage memberUsage(Member member, int usageCount) {
        AnalysisUsage usage = AnalysisUsage.initialize(member, LocalDate.of(2026, 3, 26));
        ReflectionTestUtils.setField(usage, "usageCount", usageCount);
        return usage;
    }

    private static DailyAnalysisUsage globalUsage(int usageCount) {
        DailyAnalysisUsage usage = DailyAnalysisUsage.initialize(LocalDate.of(2026, 3, 26));
        ReflectionTestUtils.setField(usage, "usageCount", usageCount);
        return usage;
    }
}
