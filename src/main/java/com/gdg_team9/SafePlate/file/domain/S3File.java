package com.gdg_team9.SafePlate.file.domain;

import com.gdg_team9.SafePlate.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "s3_file")
@Getter
@NoArgsConstructor
public class S3File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private FileStatus status;

    @Builder
    public S3File(Member member, String path, String fileName, FileStatus status) {
        this.member = member;
        this.path = path;
        this.fileName = fileName;
        this.status = status;
    }

    public String getFullFileName() {
        return path + '/' + fileName;
    }
}
