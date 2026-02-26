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

        // TODO 팀 기능 추가에 따라 Set으로의 변경 검토
        List<String> userAllergies;

        Long teamMemberId = clientSearchRequest.getTeamMemberId();
        if (teamMemberId == null) {
            userAllergies = userAllergyRepository.findByMember(member).stream()
                    .map(userAllergy -> userAllergy.getAllergy().getName())
                    .toList();
        } else {
            TeamMember teamMember =
                    teamMemberRepository.findByIdAndMember(teamMemberId, member)
                            .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));

            // TODO 기피 재료 저장 방식 변경에 따른 수정 필요
            // 추후 코드가 완전히 변경됨에 따라, 현재는 N+1 문제가 발생하는 임시 코드로 설정
            userAllergies = teamMember.getTeam().getTeamMembers().stream()
                    .flatMap(
                            tm -> userAllergyRepository.findByMember(tm.getMember()).stream()
                                    .map(userAllergy -> userAllergy.getAllergy().getName())
                    )
                    .toList();
        }

        // TODO 이미지 여러 개 보낼 수 있도록 수정
        AiClientRequest.SearchRequest aiSearchRequest = AiClientRequest.SearchRequest.builder()
                .imageUrl(imageUrls.get(0))
                .avoid(userAllergies)
                .lang("en") // TODO 회원별 언어 가져오기
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
