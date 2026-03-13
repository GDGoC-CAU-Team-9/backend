package com.gdg_team9.SafePlate.member.service;

import com.gdg_team9.SafePlate.api.code.status.ErrorStatus;
import com.gdg_team9.SafePlate.exception.GeneralException;
import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void updateLanguage(String email, String language) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._UNAUTHORIZED));

        member.changeLanguage(language);
    }

    @Transactional
    public void join(String email, String password, String language) {
        // 데이터베이스 예외 발생 이전에 로직에서 차단
        if (memberRepository.existsByEmail(email)) {
            throw new GeneralException(ErrorStatus.DUPLICATE_EMAIL);
        }

        String encodedPassword = bCryptPasswordEncoder.encode(password);

        Member member = Member.builder()
                .email(email)
                .password(encodedPassword)
                .language(language)
                .build();
        memberRepository.save(member);
    }
}
