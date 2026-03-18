package com.gdg_team9.SafePlate.avoid.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "avoid_preset")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class AvoidPreset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * preset 이름(관리자 확인용 및 fallback값)
     */
    @Column(name = "preset_name", nullable = false, length = 100)
    private String presetName;

    @OneToMany(mappedBy = "avoidPreset", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvoidPresetTranslation> translations = new ArrayList<>();

    @OneToMany(mappedBy = "avoidPreset", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvoidPresetItemTranslation> itemTranslations = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public AvoidPreset(String presetName) {
        this.presetName = presetName;
    }
}
