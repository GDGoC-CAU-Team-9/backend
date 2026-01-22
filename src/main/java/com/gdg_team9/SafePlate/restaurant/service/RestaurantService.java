package com.gdg_team9.SafePlate.restaurant.service;

import com.gdg_team9.SafePlate.api.code.status.ErrorStatus;
import com.gdg_team9.SafePlate.exception.GeneralException;
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

    public AiClientResponse.SearchResponse searchRestaurant(
            String userEmail, // 현재 사용자 이메일
            RestaurantRequest.SearchRequest clientSearchRequest
    ) {
        // TODO: 알러지 정보 가져오기
        AiClientRequest.SearchRequest aiSearchRequest = AiClientRequest.SearchRequest.builder()
                .keyword(clientSearchRequest.getKeyword())
                .lat(clientSearchRequest.getLat())
                .lng(clientSearchRequest.getLng())
                .dislikeIngredients(List.of())
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
