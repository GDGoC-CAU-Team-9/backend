package com.gdg_team9.SafePlate.allergy.service;

import com.gdg_team9.SafePlate.allergy.domain.Allergy;
import com.gdg_team9.SafePlate.allergy.dto.AllergyResponse;
import com.gdg_team9.SafePlate.allergy.repository.AllergyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AllergyService {
    private final AllergyRepository allergyRepository;

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
}
