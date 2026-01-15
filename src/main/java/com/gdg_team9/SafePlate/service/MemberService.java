package com.gdg_team9.SafePlate.service;

import com.gdg_team9.SafePlate.domain.Member;
import com.gdg_team9.SafePlate.repository.MemberRepository;
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
    public void join(String email, String password, String name){
        // 데이터베이스 예외 발생 이전에 로직에서 차단
        memberRepository.findByEmail(email).ifPresent(m ->{
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        });
        
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        Member member = Member.builder()
                .email(email)
                .password(encodedPassword)
                .name(name)
                .build();
        memberRepository.save(member);
    }
}
