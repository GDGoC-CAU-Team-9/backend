package com.gdg_team9.SafePlate.restaurant.service;

import com.gdg_team9.SafePlate.allergy.repository.UserAllergyRepository;
import com.gdg_team9.SafePlate.api.code.status.ErrorStatus;
import com.gdg_team9.SafePlate.exception.GeneralException;
import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.member.repository.MemberRepository;
import com.gdg_team9.SafePlate.restaurant.dto.AiClientRequest;
import com.gdg_team9.SafePlate.restaurant.dto.AiClientResponse;
import com.gdg_team9.SafePlate.restaurant.dto.RestaurantRequest;
import com.gdg_team9.SafePlate.restaurant.openfeign.AiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final AiClient aiClient;
    private final MemberRepository memberRepository;
    private final UserAllergyRepository userAllergyRepository;

    public AiClientResponse.SearchResponse searchRestaurant(
            String userEmail, // 현재 사용자 이메일
            RestaurantRequest.SearchRequest clientSearchRequest
    ) {
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new GeneralException(ErrorStatus._UNAUTHORIZED));

        List<String> userAllergies = userAllergyRepository.findByMember(member)
                .stream().map(userAllergy -> userAllergy.getAllergy().getName())
                .toList();

        AiClientRequest.SearchRequest aiSearchRequest = AiClientRequest.SearchRequest.builder()
                .keyword(clientSearchRequest.getKeyword())
                .lat(clientSearchRequest.getLat())
                .lng(clientSearchRequest.getLng())
                .dislikeIngredients(userAllergies)
                .build();
        try {
            ResponseEntity<AiClientResponse.SearchResponse> aiSearchResponse =
                    aiClient.requestSearch(aiSearchRequest);
            if (aiSearchResponse.getStatusCode().is2xxSuccessful()) {
                return aiSearchResponse.getBody();
            } else {
                throw new GeneralException(ErrorStatus.AI_SERVER_FAIL);
            }
        } catch (Exception ex) {
            throw new GeneralException(ErrorStatus.AI_CONNECT_FAIL);
        }
    }
}
