package com.gdg_team9.SafePlate.avoid.repository;

import com.gdg_team9.SafePlate.avoid.domain.AvoidPresetItemTranslation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvoidPresetItemTranslationRepository extends JpaRepository<AvoidPresetItemTranslation, Long> {
    @EntityGraph(attributePaths = {"avoidPreset"})
    List<AvoidPresetItemTranslation> findAllByLanguageOrderByItemOrder(String language);

    List<AvoidPresetItemTranslation> findByAvoidPresetIdAndLanguageOrderByItemOrder(Long presetId, String language);
}
