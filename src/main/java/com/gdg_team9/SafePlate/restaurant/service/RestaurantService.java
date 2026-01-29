package com.gdg_team9.SafePlate.restaurant.service;

import com.gdg_team9.SafePlate.allergy.repository.UserAllergyRepository;
import com.gdg_team9.SafePlate.api.code.status.ErrorStatus;
import com.gdg_team9.SafePlate.exception.GeneralException;
import com.gdg_team9.SafePlate.file.service.FileService;
import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.member.repository.MemberRepository;
import com.gdg_team9.SafePlate.restaurant.dto.AiClientRequest;
import com.gdg_team9.SafePlate.restaurant.dto.AiClientResponse;
import com.gdg_team9.SafePlate.restaurant.dto.RestaurantRequest;
import com.gdg_team9.SafePlate.restaurant.openfeign.AiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {
    private final AiClient aiClient;
    private final MemberRepository memberRepository;
    private final UserAllergyRepository userAllergyRepository;

    private final FileService fileService;

    public AiClientResponse.SearchResponse searchRestaurant(
            String userEmail, // 현재 사용자 이메일
            RestaurantRequest.SearchRequest clientSearchRequest
    ) {
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new GeneralException(ErrorStatus._UNAUTHORIZED));

        List<String> userAllergies = userAllergyRepository.findByMember(member)
                .stream().map(userAllergy -> userAllergy.getAllergy().getName())
                .toList();

        List<String> imageUrls = fileService.getFileUrlsByIds(member, clientSearchRequest.getIds());
        AiClientRequest.SearchRequest aiSearchRequest = AiClientRequest.SearchRequest.builder()
                .image_url(imageUrls.get(0))
                .avoid(userAllergies)
                .build();
        try {
            return aiClient.requestSearch(aiSearchRequest).getBody();
        } catch (feign.RetryableException e) {
            throw new GeneralException(ErrorStatus.AI_CONNECT_FAIL);
        } catch (feign.FeignException e) {
            throw new GeneralException(ErrorStatus.AI_SERVER_FAIL);
        }
    }
}
