package com.gdg_team9.SafePlate.restaurant.controller;

import com.gdg_team9.SafePlate.api.CommonResponse;
import com.gdg_team9.SafePlate.config.AuthErrorResponses;
import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.restaurant.dto.RestaurantRequest;
import com.gdg_team9.SafePlate.restaurant.dto.RestaurantResponse;
import com.gdg_team9.SafePlate.restaurant.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Restaurant", description = "레스토랑 검색 관련 API")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping("/search")
    @Operation(summary = "레스토랑 검색", description = "사용자의 알레르기 정보를 기반으로 음식점을 검색합니다")
    @ApiResponse(responseCode = "200", description = "검색 성공")
    @ApiResponse(responseCode = "429", description = "일일 분석 횟수 초과 (개인 4회 또는 전체 16회)",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                              "isSuccess": false,
                              "code": "ANALYSIS4290",
                              "message": "하루 분석 가능 횟수(4회)를 초과했습니다.",
                              "success": false
                            }
                            """)))
    @ApiResponse(responseCode = "500", description = "AI 서버 에러",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = """
                            {
                              "isSuccess": false,
                              "code": "AI5000",
                              "message": "AI 서버에서 문제가 발생했습니다.",
                              "success": false
                            }
                            """)))
    @AuthErrorResponses
    public CommonResponse<RestaurantResponse.SearchResult> searchRestaurant(
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody RestaurantRequest.SearchRequest searchRequest
    ) {
        RestaurantResponse.SearchResult searchResponse =
                restaurantService.searchRestaurant(member, searchRequest);
        return CommonResponse.onSuccess(searchResponse);
    }
}
