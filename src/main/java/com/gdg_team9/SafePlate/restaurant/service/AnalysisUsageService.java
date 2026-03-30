package com.gdg_team9.SafePlate.restaurant.service;

import com.gdg_team9.SafePlate.api.code.status.ErrorStatus;
import com.gdg_team9.SafePlate.exception.GeneralException;
import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.restaurant.domain.AnalysisUsage;
import com.gdg_team9.SafePlate.restaurant.domain.DailyAnalysisUsage;
import com.gdg_team9.SafePlate.restaurant.dto.RestaurantResponse;
import com.gdg_team9.SafePlate.restaurant.repository.AnalysisUsageRepository;
import com.gdg_team9.SafePlate.restaurant.repository.DailyAnalysisUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class AnalysisUsageService {
    private static final int MEMBER_DAILY_ANALYSIS_LIMIT = 4;
    private static final int GLOBAL_DAILY_ANALYSIS_LIMIT = 100;
    private static final ZoneId USAGE_ZONE = ZoneId.of("Asia/Seoul");

    private final AnalysisUsageRepository analysisUsageRepository;
    private final DailyAnalysisUsageRepository dailyAnalysisUsageRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void consumeDailyQuota(Member member) {
        LocalDate usageDate = LocalDate.now(USAGE_ZONE);
        consumeUsage(member, usageDate);
    }

    @Transactional(readOnly = true)
    public RestaurantResponse.AnalysisUsageStatus getDailyQuotaStatus(Member member) {
        LocalDate usageDate = LocalDate.now(USAGE_ZONE);

        int memberUsed = analysisUsageRepository.findByMemberAndUsageDate(member, usageDate)
                .map(AnalysisUsage::getUsageCount)
                .orElse(0);
        int globalUsed = dailyAnalysisUsageRepository.findByUsageDate(usageDate)
                .map(DailyAnalysisUsage::getUsageCount)
                .orElse(0);

        return RestaurantResponse.AnalysisUsageStatus.builder()
                .usageDate(usageDate.toString())
                .memberDailyLimit(MEMBER_DAILY_ANALYSIS_LIMIT)
                .memberUsed(memberUsed)
                .memberRemaining(Math.max(MEMBER_DAILY_ANALYSIS_LIMIT - memberUsed, 0))
                .globalDailyLimit(GLOBAL_DAILY_ANALYSIS_LIMIT)
                .globalUsed(globalUsed)
                .globalRemaining(Math.max(GLOBAL_DAILY_ANALYSIS_LIMIT - globalUsed, 0))
                .build();
    }

    private void consumeUsage(Member member, LocalDate usageDate) {
        analysisUsageRepository.createIfAbsent(member.getId(), usageDate);
        dailyAnalysisUsageRepository.createIfAbsent(usageDate);

        AnalysisUsage memberUsage = analysisUsageRepository.findByMemberAndUsageDateForUpdate(member, usageDate)
                .orElseThrow(() -> new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR));
        DailyAnalysisUsage globalUsage = dailyAnalysisUsageRepository.findByUsageDateForUpdate(usageDate)
                .orElseThrow(() -> new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR));

        validateMemberDailyLimit(memberUsage);
        validateGlobalDailyLimit(globalUsage);

        memberUsage.increaseUsage();
        globalUsage.increaseUsage();
    }

    private void validateMemberDailyLimit(AnalysisUsage memberUsage) {
        if (memberUsage.hasReachedLimit(MEMBER_DAILY_ANALYSIS_LIMIT)) {
            throw new GeneralException(ErrorStatus.ANALYSIS_DAILY_LIMIT_EXCEEDED);
        }
    }

    private void validateGlobalDailyLimit(DailyAnalysisUsage globalUsage) {
        if (globalUsage.hasReachedLimit(GLOBAL_DAILY_ANALYSIS_LIMIT)) {
            throw new GeneralException(ErrorStatus.ANALYSIS_GLOBAL_DAILY_LIMIT_EXCEEDED);
        }
    }
}
