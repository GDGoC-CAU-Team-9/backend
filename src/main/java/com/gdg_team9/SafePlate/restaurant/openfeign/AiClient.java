package com.gdg_team9.SafePlate.restaurant.openfeign;

import com.gdg_team9.SafePlate.restaurant.dto.AiClientRequest;
import com.gdg_team9.SafePlate.restaurant.dto.AiClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// TODO: AI 서버 URL 수정 필요
@FeignClient(name = "AiClient", url = "http://localhost:8081")
public interface AiClient {
    @PostMapping("/ai/vi/search")
    ResponseEntity<AiClientResponse.SearchResponse> requestSearch
            (@RequestBody AiClientRequest.SearchRequest body);
}
