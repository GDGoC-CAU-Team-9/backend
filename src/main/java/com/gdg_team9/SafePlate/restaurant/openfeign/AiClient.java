package com.gdg_team9.SafePlate.restaurant.openfeign;

import com.gdg_team9.SafePlate.restaurant.dto.AiClientRequest;
import com.gdg_team9.SafePlate.restaurant.dto.AiClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "AiClient", url = "${ai-server.url}")
public interface AiClient {
    @PostMapping("/rank")
    ResponseEntity<AiClientResponse.SearchResponse> requestSearch
            (@RequestBody AiClientRequest.SearchRequest body);
}
