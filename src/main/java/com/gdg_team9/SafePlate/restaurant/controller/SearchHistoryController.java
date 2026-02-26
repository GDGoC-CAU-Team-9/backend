package com.gdg_team9.SafePlate.restaurant.controller;

import com.gdg_team9.SafePlate.api.ApiResponse;
import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.restaurant.dto.SearchHistoryRequest;
import com.gdg_team9.SafePlate.restaurant.dto.SearchHistoryResponse;
import com.gdg_team9.SafePlate.restaurant.service.SearchHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/histories")
@RequiredArgsConstructor
public class SearchHistoryController {
    private final SearchHistoryService searchHistoryService;

    @GetMapping
    public ApiResponse<SearchHistoryResponse.PageResult> getMyHistories(
            @AuthenticationPrincipal Member member,
            @Valid @ModelAttribute SearchHistoryRequest.PageRequest pageRequest
            ) {
        SearchHistoryResponse.PageResult memberHistories =
                searchHistoryService.getMemberHistories(member, pageRequest.getPageNumber());
        return ApiResponse.onSuccess(memberHistories);
    }
}
