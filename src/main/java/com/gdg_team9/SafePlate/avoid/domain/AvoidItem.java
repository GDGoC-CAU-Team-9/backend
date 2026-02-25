package com.gdg_team9.SafePlate.avoid.domain;

import com.gdg_team9.SafePlate.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "avoid_text", nullable = false, columnDefinition = "json")
    @Setter
    private List<String> avoidItems;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public AvoidItem(Member member, List<String> avoidItems) {
        this.member = member;
        this.avoidItems = avoidItems;
    }
}
