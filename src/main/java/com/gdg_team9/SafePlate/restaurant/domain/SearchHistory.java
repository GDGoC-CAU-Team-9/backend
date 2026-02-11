package com.gdg_team9.SafePlate.restaurant.domain;


import com.gdg_team9.SafePlate.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "image_ids", nullable = false, columnDefinition = "json")
    private List<Long> imageIds;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "search_result", nullable = false, columnDefinition = "json")
    private RestaurantSearchResult searchResult;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public SearchHistory(Member member, List<Long> imageIds, RestaurantSearchResult searchResult) {
        this.member = member;
        this.imageIds = imageIds;
        this.searchResult = searchResult;
    }
}
