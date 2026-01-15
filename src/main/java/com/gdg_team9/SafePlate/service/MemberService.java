package com.gdg_team9.SafePlate.service;

import com.gdg_team9.SafePlate.domain.Member;
import com.gdg_team9.SafePlate.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void join(String email, String password, String name){
        String encodedPassword = bCryptPasswordEncoder.encode(password);

        Member member = Member.builder()
                .email(email)
                .password(encodedPassword)
                .name(name)
                .build();
        memberRepository.save(member);
    }
}
