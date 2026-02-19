package com.gdg_team9.SafePlate.avoid.controller;

import com.gdg_team9.SafePlate.api.ApiResponse;
import com.gdg_team9.SafePlate.avoid.dto.AvoidItemRequest;
import com.gdg_team9.SafePlate.avoid.dto.AvoidItemResponse;
import com.gdg_team9.SafePlate.avoid.service.AvoidItemService;
import com.gdg_team9.SafePlate.member.domain.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avoid-items")
@RequiredArgsConstructor
public class AvoidItemController {

    private final AvoidItemService avoidItemService;

    @GetMapping("/my")
    public ApiResponse<AvoidItemResponse.MyAvoidResponse> getMyAvoid(
            @AuthenticationPrincipal Member member
    ) {
        return ApiResponse.onSuccess(avoidItemService.getMyAvoid(member));
    }

    @PostMapping("/my/text")
    public ApiResponse<AvoidItemResponse.MyAvoidResponse> saveMyAvoid(
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody AvoidItemRequest.TextRequest request
    ) {
        return ApiResponse.onSuccess(avoidItemService.saveFromText(member, request.getText()));
    }
}
