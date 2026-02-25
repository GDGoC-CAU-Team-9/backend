package com.gdg_team9.SafePlate.avoid.openfeign;

import com.gdg_team9.SafePlate.avoid.dto.AvoidAiRequest;
import com.gdg_team9.SafePlate.avoid.dto.AvoidAiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "AvoidAiClient", url = "${avoid.ai.url}")
public interface AvoidAiClient {
    @PostMapping("/avoid/intake")
    ResponseEntity<AvoidAiResponse.ExtractResponse> extractAvoid(
            @RequestBody AvoidAiRequest.ExtractRequest body
    );
}
