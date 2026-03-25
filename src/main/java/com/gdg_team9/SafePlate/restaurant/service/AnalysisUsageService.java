package com.gdg_team9.SafePlate.restaurant.service;

import com.gdg_team9.SafePlate.api.code.status.ErrorStatus;
import com.gdg_team9.SafePlate.exception.GeneralException;
import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.restaurant.domain.AnalysisUsage;
import com.gdg_team9.SafePlate.restaurant.domain.DailyAnalysisUsage;
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
    private static final int GLOBAL_DAILY_ANALYSIS_LIMIT = 16;
    private static final ZoneId USAGE_ZONE = ZoneId.of("Asia/Seoul");

    private final AnalysisUsageRepository analysisUsageRepository;
    private final DailyAnalysisUsageRepository dailyAnalysisUsageRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void consumeDailyQuota(Member member) {
        LocalDate usageDate = LocalDate.now(USAGE_ZONE);
        consumeUsage(member, usageDate);
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
