package com.gdg_team9.SafePlate.file.repository;

import com.gdg_team9.SafePlate.file.domain.S3File;
import com.gdg_team9.SafePlate.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface S3FileRepository extends JpaRepository<S3File, Long> {
    Optional<S3File> findByIdAndMember(Long id, Member member);
    List<S3File> findAllByMemberAndIdIn(Member member, Collection<Long> ids);
    List<S3File> findAllByIdIn(Collection<Long> ids);
}
