package com.gdg_team9.SafePlate.avoid.service;

import com.gdg_team9.SafePlate.api.code.status.ErrorStatus;
import com.gdg_team9.SafePlate.avoid.domain.AvoidPreset;
import com.gdg_team9.SafePlate.avoid.domain.AvoidPresetItemTranslation;
import com.gdg_team9.SafePlate.avoid.domain.AvoidPresetTranslation;
import com.gdg_team9.SafePlate.avoid.dto.AvoidPresetResponse;
import com.gdg_team9.SafePlate.avoid.repository.AvoidPresetItemTranslationRepository;
import com.gdg_team9.SafePlate.avoid.repository.AvoidPresetRepository;
import com.gdg_team9.SafePlate.avoid.repository.AvoidPresetTranslationRepository;
import com.gdg_team9.SafePlate.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AvoidPresetService {

    private final AvoidPresetRepository avoidPresetRepository;
    private final AvoidPresetTranslationRepository avoidPresetTranslationRepository;
    private final AvoidPresetItemTranslationRepository avoidPresetItemTranslationRepository;

    /**
     * 모든 Preset 조회 (사용자 언어로 번역된 이름과 항목 반환)
     */
    public AvoidPresetResponse.PresetListResponse getAllPresets(String userLanguage) {
        List<AvoidPresetItemTranslation> itemTranslations =
                avoidPresetItemTranslationRepository.findAllByLanguageOrderByItemOrder(userLanguage);
        List<AvoidPresetTranslation> presetTranslations =
                avoidPresetTranslationRepository.findAllByLanguage(userLanguage);

        Map<Long, List<String>> itemsPerPresetId = itemTranslations.stream()
                .collect(
                        Collectors.groupingBy(
                                item -> item.getAvoidPreset().getId(),
                                Collectors.mapping(
                                        AvoidPresetItemTranslation::getItemName,
                                        Collectors.toList()
                                )
                        )
                );


        List<AvoidPresetResponse.PresetInfoResponse> presetInfos = presetTranslations.stream()
                .map(translation -> AvoidPresetResponse.PresetInfoResponse.builder()
                        .presetId(translation.getAvoidPreset().getId())
                        .presetName(translation.getTranslatedName())
                        .items(itemsPerPresetId.getOrDefault(translation.getAvoidPreset().getId(), List.of()))
                        .createdAt(translation.getAvoidPreset().getCreatedAt())
                        .updatedAt(translation.getAvoidPreset().getUpdatedAt())
                        .build()
                )
                .toList();

        return AvoidPresetResponse.PresetListResponse.builder()
                .presets(presetInfos)
                .build();
    }

    /**
     * 특정 Preset 상세 조회 (사용자 언어로 번역된 이름과 항목 반환)
     */
    public AvoidPresetResponse.PresetInfoResponse getPresetDetail(Long presetId, String userLanguage) {
        AvoidPreset preset = avoidPresetRepository.findById(presetId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PRESET_NOT_FOUND));

        String displayName = getTranslatedPresetName(preset, userLanguage);

        List<String> items = avoidPresetItemTranslationRepository
                .findByAvoidPresetIdAndLanguageOrderByItemOrder(preset.getId(), userLanguage)
                .stream()
                .map(AvoidPresetItemTranslation::getItemName)
                .collect(Collectors.toList());

        return AvoidPresetResponse.PresetInfoResponse.builder()
                .presetId(preset.getId())
                .presetName(displayName)
                .items(items)
                .createdAt(preset.getCreatedAt())
                .updatedAt(preset.getUpdatedAt())
                .build();
    }

    /**
     * Preset 이름의 번역된 버전 조회 (없으면 원본 반환)
     */
    private String getTranslatedPresetName(AvoidPreset preset, String language) {
        return avoidPresetTranslationRepository
                .findByAvoidPresetIdAndLanguage(preset.getId(), language)
                .map(AvoidPresetTranslation::getTranslatedName)
                .orElse(preset.getPresetName());
    }
}
