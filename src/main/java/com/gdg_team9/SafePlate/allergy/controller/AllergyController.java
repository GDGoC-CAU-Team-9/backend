package com.gdg_team9.SafePlate.allergy.controller;

import com.gdg_team9.SafePlate.allergy.dto.AllergyRequest;
import com.gdg_team9.SafePlate.allergy.dto.AllergyResponse;
import com.gdg_team9.SafePlate.allergy.service.AllergyService;
import com.gdg_team9.SafePlate.api.ApiResponse;
import com.gdg_team9.SafePlate.member.domain.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/allergies")
@RequiredArgsConstructor
public class AllergyController {
    private final AllergyService allergyService;

    @GetMapping
    public ApiResponse<AllergyResponse.AllergyListResponse> getAllergies() {
        AllergyResponse.AllergyListResponse response = allergyService.getAllergies();
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/my")
    public ApiResponse<AllergyResponse.AllergyListResponse> getMyAllergies(
            @AuthenticationPrincipal Member member
    ) {
        String email = member.getEmail();
        AllergyResponse.AllergyListResponse response = allergyService.getMyAllergies(email);
        return ApiResponse.onSuccess(response);
    }

    @PutMapping("/my")
    public ApiResponse<Void> updateMyAllergies(
            @AuthenticationPrincipal Member member,
            @Valid @RequestBody AllergyRequest.UpdateUserAllergyRequest request
            ) {
        String email = member.getEmail();
        allergyService.updateMyAllergies(email, request.getAllergyIds());
        return ApiResponse.onSuccess(null);
    }
}
