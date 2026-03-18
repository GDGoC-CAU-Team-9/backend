package com.gdg_team9.SafePlate.avoid.repository;

import com.gdg_team9.SafePlate.avoid.domain.AvoidPresetTranslation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AvoidPresetTranslationRepository extends JpaRepository<AvoidPresetTranslation, Long> {
    @EntityGraph(attributePaths = {"avoidPreset"})
    List<AvoidPresetTranslation> findAllByLanguage(String language);

    Optional<AvoidPresetTranslation> findByAvoidPresetIdAndLanguage(Long presetId, String language);
}
