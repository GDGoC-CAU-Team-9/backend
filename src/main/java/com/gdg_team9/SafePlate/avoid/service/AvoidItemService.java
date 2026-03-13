package com.gdg_team9.SafePlate.avoid.service;

import com.gdg_team9.SafePlate.api.code.status.ErrorStatus;
import com.gdg_team9.SafePlate.avoid.domain.AvoidItem;
import com.gdg_team9.SafePlate.avoid.dto.AvoidAiRequest;
import com.gdg_team9.SafePlate.avoid.dto.AvoidAiResponse;
import com.gdg_team9.SafePlate.avoid.dto.AvoidItemResponse;
import com.gdg_team9.SafePlate.avoid.openfeign.AvoidAiClient;
import com.gdg_team9.SafePlate.avoid.repository.AvoidItemRepository;
import com.gdg_team9.SafePlate.exception.GeneralException;
import com.gdg_team9.SafePlate.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AvoidItemService {

    private final AvoidItemRepository avoidItemRepository;
    private final AvoidAiClient avoidAiClient;

    public AvoidItemResponse.ExtractedAvoidResponse extractMyAvoid(Member member, String text) {
        AvoidAiResponse.ExtractResponse aiResponse;

        try {
            AvoidAiRequest.ExtractRequest request = AvoidAiRequest.ExtractRequest.builder()
                    .userText(text)
                    .lang(member.getLanguage())
                    .build();
            aiResponse = avoidAiClient.extractAvoid(request).getBody();
        } catch (feign.RetryableException e) {
            throw new GeneralException(ErrorStatus.AI_CONNECT_FAIL);
        } catch (feign.FeignException e) {
            throw new GeneralException(ErrorStatus.AI_SERVER_FAIL);
        }

        if (aiResponse == null) {
            throw new GeneralException(ErrorStatus.AI_SERVER_FAIL);
        }

        return AvoidItemResponse.ExtractedAvoidResponse.builder()
                .avoidItems(aiResponse.getCandidates())
                .confirmQuestion(aiResponse.getConfirmQuestion())
                .build();
    }

    public AvoidItemResponse.MyAvoidResponse getMyAvoid(Member member) {
        List<String> avoidItem = avoidItemRepository.findById(member.getId())
                .map(AvoidItem::getAvoidItems)
                .orElse(List.of());
        return AvoidItemResponse.MyAvoidResponse.builder()
                .avoidItems(avoidItem)
                .build();
    }

    @Transactional
    public AvoidItemResponse.MyAvoidResponse updateMyAvoid(Member member, List<String> avoidItems) {
        Optional<AvoidItem> avoidItemOptional = avoidItemRepository.findById(member.getId());
        AvoidItem avoidItem;

        if (avoidItemOptional.isPresent()) {
            avoidItem = avoidItemOptional.get();
            avoidItem.setAvoidItems(avoidItems);
        } else {
            avoidItem = AvoidItem.builder()
                    .member(member)
                    .avoidItems(avoidItems)
                    .build();
            avoidItemRepository.save(avoidItem);
        }

        return AvoidItemResponse.MyAvoidResponse.builder()
                .avoidItems(avoidItems)
                .build();
    }
}
