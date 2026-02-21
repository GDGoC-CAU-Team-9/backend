package com.gdg_team9.SafePlate.restaurant.service;

import com.gdg_team9.SafePlate.allergy.repository.UserAllergyRepository;
import com.gdg_team9.SafePlate.api.code.status.ErrorStatus;
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
    private final UserAllergyRepository userAllergyRepository;
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

        Long teamMemberId = clientSearchRequest.getTeamMemberId();
        if (teamMemberId == null) {
            RestaurantSearchResult searchResult = searchRestaurantForOneMember(member, imageUrls);

            SearchHistory searchHistory = SearchHistory.builder()
                    .member(member)
                    .imageIds(clientSearchRequest.getIds())
                    .searchResult(searchResult)
                    .build();

            searchHistoryRepository.save(searchHistory);
            return searchResult;
        } else {
            TeamMember teamMember =
                    teamMemberRepository.findByIdAndMember(teamMemberId, member)
                            .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

            List<SearchHistory> searchResults = teamMember.getTeam().getTeamMembers().stream()
                    .map(tm -> {
                        // 팀원 각각에 대한 검색
                        RestaurantSearchResult searchResult =
                                searchRestaurantForOneMember(tm.getMember(), imageUrls);

                        // 검색결과를 검색기록 형태로 변경 (검색 결과 + 검색 대상자를 mapping한 것이기도 함)
                        return SearchHistory.builder()
                                .member(tm.getMember())
                                .imageIds(clientSearchRequest.getIds())
                                .searchResult(searchResult)
                                .build();
                    })
                    .toList();

            searchHistoryRepository.saveAll(searchResults);

            // TODO 팀원 각각의 검색 결과 반환

            // 검색한 사람의 검색 결과 반환
            for (SearchHistory searchHistory : searchResults) {
                if (searchHistory.getMember().getId().equals(member.getId())) {
                    return searchHistory.getSearchResult();
                }
            }
            throw new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }

    private RestaurantSearchResult searchRestaurantForOneMember(Member member, List<String> imageUrls) {
        // TODO 팀 검색 시 최적화 필요 (팀원 각각의 알레르기 정보 조회 -> N+1 문제)
        List<String> userAllergies = userAllergyRepository.findByMember(member)
                .stream().map(userAllergy -> userAllergy.getAllergy().getName())
                .toList();

        // TODO 이미지 여러 개 보낼 수 있도록 수정
        AiClientRequest.SearchRequest aiSearchRequest = AiClientRequest.SearchRequest.builder()
                .imageUrl(imageUrls.get(0))
                .avoid(userAllergies)
                .build();
        try {
            return aiClient.requestSearch(aiSearchRequest).getBody();
        } catch (feign.RetryableException e) {
            throw new GeneralException(ErrorStatus.AI_CONNECT_FAIL);
        } catch (feign.FeignException e) {
            throw new GeneralException(ErrorStatus.AI_SERVER_FAIL);
        }
    }
}
