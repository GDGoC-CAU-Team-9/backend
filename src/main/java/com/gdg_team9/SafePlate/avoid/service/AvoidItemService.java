package com.gdg_team9.SafePlate.avoid.service;

import com.gdg_team9.SafePlate.avoid.ai.AvoidAiClient;
import com.gdg_team9.SafePlate.avoid.ai.AvoidAiRequest;
import com.gdg_team9.SafePlate.avoid.ai.AvoidAiResponse;
import com.gdg_team9.SafePlate.avoid.domain.AvoidItem;
import com.gdg_team9.SafePlate.avoid.dto.AvoidItemResponse;
import com.gdg_team9.SafePlate.avoid.repository.AvoidItemRepository;
import com.gdg_team9.SafePlate.api.code.status.ErrorStatus;
import com.gdg_team9.SafePlate.exception.GeneralException;
import com.gdg_team9.SafePlate.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AvoidItemService {

    private final AvoidItemRepository avoidItemRepository;
    private final AvoidAiClient avoidAiClient;

    public AvoidItemResponse.MyAvoidResponse getMyAvoid(Member member) {
        String avoidText = avoidItemRepository.findById(member.getId())
                .map(AvoidItem::getAvoidText)
                .orElse("");
        return AvoidItemResponse.MyAvoidResponse.builder()
                .avoidText(avoidText)
                .build();
    }

    @Transactional
    public AvoidItemResponse.MyAvoidResponse saveFromText(Member member, String text) {
        AvoidAiResponse.ExtractResponse aiResponse =
                avoidAiClient.extractAvoid(new AvoidAiRequest.ExtractRequest(text)).getBody();

        if (aiResponse == null || aiResponse.getAvoidText() == null) {
            throw new GeneralException(ErrorStatus.AI_SERVER_FAIL);
        }

        String avoidText = aiResponse.getAvoidText();

        AvoidItem avoidItem = avoidItemRepository.findById(member.getId())
                .orElseGet(() -> new AvoidItem(member, avoidText));

        avoidItem.updateAvoidText(avoidText);
        avoidItemRepository.save(avoidItem);

        return AvoidItemResponse.MyAvoidResponse.builder()
                .avoidText(avoidText)
                .build();
    }
}
