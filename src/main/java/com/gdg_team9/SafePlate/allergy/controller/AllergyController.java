package com.gdg_team9.SafePlate.allergy.controller;

import com.gdg_team9.SafePlate.allergy.dto.AllergyResponse;
import com.gdg_team9.SafePlate.allergy.service.AllergyService;
import com.gdg_team9.SafePlate.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
