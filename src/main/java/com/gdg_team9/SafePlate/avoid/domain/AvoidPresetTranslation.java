package com.gdg_team9.SafePlate.avoid.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "avoid_preset_translation",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_preset_language",
                        columnNames = {"preset_id", "language"}
                )
        }
)
@Getter
@NoArgsConstructor
public class AvoidPresetTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preset_id", nullable = false)
    private AvoidPreset avoidPreset;

    @Column(name = "language", nullable = false, length = 2)
    private String language;

    @Column(name = "translated_name", nullable = false, length = 100)
    private String translatedName;

    @Builder
    public AvoidPresetTranslation(AvoidPreset avoidPreset, String language, String translatedName) {
        this.avoidPreset = avoidPreset;
        this.language = language;
        this.translatedName = translatedName;
    }
}
