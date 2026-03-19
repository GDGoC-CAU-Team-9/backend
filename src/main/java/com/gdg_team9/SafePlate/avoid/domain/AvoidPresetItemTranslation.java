package com.gdg_team9.SafePlate.avoid.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "avoid_preset_item_translation",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_preset_item_language_order",
                        columnNames = {"preset_id", "language", "item_order"}
                )
        }
)
@Getter
@NoArgsConstructor
public class AvoidPresetItemTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preset_id", nullable = false)
    private AvoidPreset avoidPreset;

    @Column(name = "language", nullable = false, length = 2)
    private String language;

    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName;

    @Column(name = "item_order", nullable = false)
    private Integer itemOrder;

    @Builder
    public AvoidPresetItemTranslation(AvoidPreset avoidPreset, String language, String itemName, Integer itemOrder) {
        this.avoidPreset = avoidPreset;
        this.language = language;
        this.itemName = itemName;
        this.itemOrder = itemOrder;
    }
}
