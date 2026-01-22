package com.gdg_team9.SafePlate.allergy.service;

import com.gdg_team9.SafePlate.allergy.domain.Allergy;
import com.gdg_team9.SafePlate.allergy.domain.UserAllergy;
import com.gdg_team9.SafePlate.allergy.dto.AllergyResponse;
import com.gdg_team9.SafePlate.allergy.repository.AllergyRepository;
import com.gdg_team9.SafePlate.allergy.repository.UserAllergyRepository;
import com.gdg_team9.SafePlate.api.code.status.ErrorStatus;
import com.gdg_team9.SafePlate.exception.GeneralException;
import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AllergyService {
    private final AllergyRepository allergyRepository;
    private final MemberRepository memberRepository;
    private final UserAllergyRepository userAllergyRepository;

    @Transactional(readOnly = true)
    public AllergyResponse.AllergyListResponse getAllergies(){
        List<Allergy> allergies = allergyRepository.findAll();
        List<AllergyResponse.AllergyDTO> allergyDTOs = allergies.stream()
                .map(allergy -> AllergyResponse.AllergyDTO.builder()
                        .id(allergy.getId())
                        .name(allergy.getName())
                        .build())
                .collect(Collectors.toList());
        return AllergyResponse.AllergyListResponse.builder()
                .allergies(allergyDTOs)
                .build();
    }

    @Transactional(readOnly = true)
    public AllergyResponse.AllergyListResponse getMyAllergies(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._UNAUTHORIZED));

        List<UserAllergy> userAllergies = userAllergyRepository.findByMember(member);

        List<AllergyResponse.AllergyDTO> allergyDTOs = userAllergies.stream()
                .map(userAllergy -> AllergyResponse.AllergyDTO.builder()
                .id(userAllergy.getAllergy().getId())
                        .name(userAllergy.getAllergy().getName())
                        .build())
                .collect(Collectors.toList());

        return AllergyResponse.AllergyListResponse.builder()
                .allergies(allergyDTOs)
                .build();
    }

    @Transactional
    public void updateMyAllergies(String email, List<Long> allergyIds){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._UNAUTHORIZED));

        // 기존 알레르기 정보 삭제
        userAllergyRepository.deleteByMember(member);

        // 새로운 알레르기 정보 추가
        List<Allergy> allergies = allergyRepository.findAllById(allergyIds);

        if(allergies.size() != allergyIds.size()) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
        List<UserAllergy> userAllergies = allergies.stream()
                .map(allergy -> UserAllergy.builder()
                        .member(member)
                        .allergy(allergy)
                        .build())
                .collect(Collectors.toList());

        userAllergyRepository.saveAll(userAllergies);
    }
}
