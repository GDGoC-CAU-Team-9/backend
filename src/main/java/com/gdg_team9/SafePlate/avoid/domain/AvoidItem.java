package com.gdg_team9.SafePlate.avoid.domain;

import com.gdg_team9.SafePlate.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "avoid_item")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class AvoidItem {
    @Id
    @Column(name = "member_id")
    private Long memberId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "avoid_text", nullable = false, columnDefinition = "TEXT")
    private String avoidText;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public AvoidItem(Member member, String avoidText) {
        this.member = member;
        this.avoidText = avoidText;
    }

    public void updateAvoidText(String avoidText) {
        this.avoidText = avoidText;
    }
}
