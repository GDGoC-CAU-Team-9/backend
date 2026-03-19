package com.gdg_team9.SafePlate.restaurant.controller;

import com.gdg_team9.SafePlate.api.ApiResponse;
import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.restaurant.dto.RestaurantRequest;
import com.gdg_team9.SafePlate.restaurant.dto.RestaurantResponse;
import com.gdg_team9.SafePlate.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping("/search")
    public ApiResponse<RestaurantResponse.SearchResult> searchRestaurant(
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody RestaurantRequest.SearchRequest searchRequest
    ) {
        RestaurantResponse.SearchResult searchResponse =
                restaurantService.searchRestaurant(member, searchRequest);
        return ApiResponse.onSuccess(searchResponse);
    }
}
