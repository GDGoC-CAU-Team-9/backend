package com.gdg_team9.SafePlate.restaurant.service;

import com.gdg_team9.SafePlate.api.code.status.ErrorStatus;
import com.gdg_team9.SafePlate.avoid.domain.AvoidItem;
import com.gdg_team9.SafePlate.avoid.repository.AvoidItemRepository;
import com.gdg_team9.SafePlate.exception.GeneralException;
import com.gdg_team9.SafePlate.file.service.FileService;
import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.restaurant.domain.RestaurantSearchResult;
import com.gdg_team9.SafePlate.restaurant.domain.SearchHistory;
import com.gdg_team9.SafePlate.restaurant.dto.AiClientRequest;
import com.gdg_team9.SafePlate.restaurant.dto.RestaurantRequest;
import com.gdg_team9.SafePlate.restaurant.openfeign.AiClient;
import com.gdg_team9.SafePlate.restaurant.repository.SearchHistoryRepository;
import com.gdg_team9.SafePlate.team.domain.TeamMember;
import com.gdg_team9.SafePlate.team.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {
    private final AiClient aiClient;
    private final AvoidItemRepository avoidItemRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final SearchHistoryRepository searchHistoryRepository;

    private final FileService fileService;

    @Transactional
    public RestaurantSearchResult searchRestaurant(
            Member member,
            RestaurantRequest.SearchRequest clientSearchRequest
    ) {
        // 검색 전에는 업로드한 사람 본인의 이미지가 맞는지 확인 시행
        List<String> imageUrls = fileService.getFileUrlsByMemberAndIds(member, clientSearchRequest.getIds());

        if (clientSearchRequest.getIds().size() != imageUrls.size()) {
            throw new GeneralException(ErrorStatus.FILE_NOT_OWNED);
        }

        List<String> userAllergies;

        Long teamMemberId = clientSearchRequest.getTeamMemberId();
        if (teamMemberId == null) {
            userAllergies = avoidItemRepository.findById(member.getId())
                    .map(AvoidItem::getAvoidItems)
                    .orElse(List.of());
        } else {
            TeamMember teamMember =
                    teamMemberRepository.findByIdAndMember(teamMemberId, member)
                            .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));
            List<Member> members = teamMember.getTeam().getTeamMembers().stream()
                    .map(TeamMember::getMember)
                    .toList();

            userAllergies = avoidItemRepository.findAllByMemberIn(members)
                    .stream()
                    .flatMap(avoidItem -> avoidItem.getAvoidItems().stream())
                    .distinct()
                    .toList();
        }

        // TODO 이미지 여러 개 보낼 수 있도록 수정
        AiClientRequest.SearchRequest aiSearchRequest = AiClientRequest.SearchRequest.builder()
                .imageUrl(imageUrls.get(0))
                .avoid(userAllergies)
                .lang(member.getLanguage())
                .build();
        try {
            RestaurantSearchResult searchResult = aiClient.requestSearch(aiSearchRequest).getBody();

            SearchHistory searchHistory = SearchHistory.builder()
                    .member(member)
                    .imageIds(clientSearchRequest.getIds())
                    .searchResult(searchResult)
                    .build();

            searchHistoryRepository.save(searchHistory);
            return searchResult;

        } catch (feign.RetryableException e) {
            throw new GeneralException(ErrorStatus.AI_CONNECT_FAIL);
        } catch (feign.FeignException e) {
            throw new GeneralException(ErrorStatus.AI_SERVER_FAIL);
        }
    }
}
