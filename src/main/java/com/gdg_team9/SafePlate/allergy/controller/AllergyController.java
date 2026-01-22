package com.gdg_team9.SafePlate.allergy.controller;

import com.gdg_team9.SafePlate.allergy.dto.AllergyRequest;
import com.gdg_team9.SafePlate.allergy.dto.AllergyResponse;
import com.gdg_team9.SafePlate.allergy.service.AllergyService;
import com.gdg_team9.SafePlate.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
    public ApiResponse<AllergyResponse.AllergyListResponse> getMyAllergies(Principal principal) {
        String email = principal.getName();
        AllergyResponse.AllergyListResponse response = allergyService.getMyAllergies(email);
        return ApiResponse.onSuccess(response);
    }

    @PutMapping("/my")
    public ApiResponse<Void> updateMyAllergies(
            @Valid @RequestBody AllergyRequest.UpdateUserAllergyRequest request,
            Principal principal) {
        String email = principal.getName();
        allergyService.updateMyAllergies(email, request.getAllergyIds());
        return ApiResponse.onSuccess(null);
    }
}
