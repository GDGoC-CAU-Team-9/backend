package com.gdg_team9.SafePlate.restaurant.service;

import com.gdg_team9.SafePlate.api.code.status.ErrorStatus;
import com.gdg_team9.SafePlate.avoid.domain.AvoidItem;
import com.gdg_team9.SafePlate.avoid.repository.AvoidItemRepository;
import com.gdg_team9.SafePlate.exception.GeneralException;
import com.gdg_team9.SafePlate.file.domain.FileStatus;
import com.gdg_team9.SafePlate.file.dto.FileRequest;
import com.gdg_team9.SafePlate.file.dto.FileResponse;
import com.gdg_team9.SafePlate.file.service.FileService;
import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.restaurant.domain.RestaurantSearchResult;
import com.gdg_team9.SafePlate.restaurant.domain.SearchHistory;
import com.gdg_team9.SafePlate.restaurant.dto.AiClientRequest;
import com.gdg_team9.SafePlate.restaurant.dto.RestaurantRequest;
import com.gdg_team9.SafePlate.restaurant.dto.RestaurantResponse;
import com.gdg_team9.SafePlate.restaurant.openfeign.AiClient;
import com.gdg_team9.SafePlate.restaurant.repository.SearchHistoryRepository;
import com.gdg_team9.SafePlate.team.domain.TeamMember;
import com.gdg_team9.SafePlate.team.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantService {
    private final AiClient aiClient;
    private final AvoidItemRepository avoidItemRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final SearchHistoryRepository searchHistoryRepository;

    private final FileService fileService;

    private final TransactionTemplate transactionTemplate;

    public RestaurantResponse.SearchResult searchRestaurant(
            Member member,
            RestaurantRequest.SearchRequest clientSearchRequest
    ) {
        AiClientRequest.SearchPreparedData result = transactionTemplate.execute(status -> {
            // 검색 전에는 업로드한 사람 본인의 이미지가 맞는지 확인 시행
            List<String> imageUrls = fileService.getFileUrlsByMemberAndIds(member, clientSearchRequest.getIds());

            if (clientSearchRequest.getIds().size() != imageUrls.size()) {
                throw new GeneralException(ErrorStatus.FILE_NOT_OWNED);
            }

            List<String> userAllergies = extractUserAllergies(member, clientSearchRequest.getTeamMemberId());

            // TODO 이미지 여러 개 보낼 수 있도록 수정
            FileRequest.PresignedUrlRequest presignedUrlRequest =
                    FileRequest.PresignedUrlRequest.builder()
                            .path("menu_board_response")
                            .fileType("png")
                            .build();
            FileResponse.PresignedUrlResponse preSignedUrl =
                    fileService.getPreSignedUrl(member, presignedUrlRequest);

            // 처리한 3가지 Data를 lambda 밖으로 꺼내기
            return AiClientRequest.SearchPreparedData.builder()
                    .imageUrls(imageUrls)
                    .userAllergies(userAllergies)
                    .preSignedUrl(preSignedUrl)
                    .build();
        });

        AiClientRequest.SearchRequest aiSearchRequest = AiClientRequest.SearchRequest.builder()
                .imageUrl(result.getImageUrls().get(0))
                .menuLang(clientSearchRequest.getMenuLang())
                .avoid(result.getUserAllergies())
                .presignedUrl(result.getPreSignedUrl().getPresignedUrl())
                .lang(member.getLanguage())
                .build();
        try {
            RestaurantSearchResult searchResult = aiClient.requestSearch(aiSearchRequest).getBody();

            FileRequest.PatchStatusRequest statusRequest = FileRequest.PatchStatusRequest.builder()
                    .fileStatus(FileStatus.UPLOADED)
                    .build();

            SearchHistory searchHistory = SearchHistory.builder()
                    .member(member)
                    .imageIds(clientSearchRequest.getIds())
                    .resultImageIds(List.of(result.getPreSignedUrl().getFileId()))
                    .searchResult(searchResult)
                    .build();

            String resultImageUrl = transactionTemplate.execute(status -> {
                searchHistoryRepository.save(searchHistory);
                return fileService.patchFileStatus(member, result.getPreSignedUrl().getFileId(), statusRequest);
            });

            // RestaurantSearchResult -> RestaurantResponse.SearchResult 변환
            return toSearchResultResponse(searchResult, resultImageUrl);

        } catch (feign.RetryableException e) {
            log.error(e.getMessage(), e);
            handleFileError(result.getPreSignedUrl().getFileId(), member);
            throw new GeneralException(ErrorStatus.AI_CONNECT_FAIL);
        } catch (feign.FeignException e) {
            log.error(e.getMessage(), e);
            handleFileError(result.getPreSignedUrl().getFileId(), member);
            throw new GeneralException(ErrorStatus.AI_SERVER_FAIL);
        }
    }

    private void handleFileError(Long fileId, Member member) {
        FileRequest.PatchStatusRequest statusRequest = FileRequest.PatchStatusRequest.builder()
                .fileStatus(FileStatus.ERROR)
                .build();
        fileService.patchFileStatus(member, fileId, statusRequest);
    }

    private RestaurantResponse.SearchResult.Item toItemResponse(RestaurantSearchResult.Item item) {
        return RestaurantResponse.SearchResult.Item.builder()
                .menu(item.getMenu())
                .menuOriginal(item.getMenuOriginal())
                .score(item.getScore())
                .risk(item.getRisk())
                .confidence(item.getConfidence())
                .matchedAvoid(item.getMatchedAvoid())
                .suspectedIngredients(item.getSuspectedIngredients())
                .reason(item.getReason())
                .build();
    }

    private RestaurantResponse.SearchResult toSearchResultResponse(
            RestaurantSearchResult searchResult,
            String resultImageUrl
    ) {
        return RestaurantResponse.SearchResult.builder()
                .itemsExtracted(searchResult.getItemsExtracted())
                .items(searchResult.getItems().stream()
                        .map(this::toItemResponse)
                        .toList()
                )
                .best(toItemResponse(searchResult.getBest()))
                .resultImageUrls(List.of(resultImageUrl))
                .timingsMs(toTimingsResponse(searchResult.getTimingsMs()))
                .build();
    }

    private RestaurantResponse.SearchResult.Timings toTimingsResponse(RestaurantSearchResult.Timings timings) {
        return RestaurantResponse.SearchResult.Timings.builder()
                .imageLoad(timings.getImageLoad())
                .extract(timings.getExtract())
                .riskAssess(timings.getRiskAssess())
                .scorePolicy(timings.getScorePolicy())
                .total(timings.getTotal())
                .build();
    }

    private List<String> extractUserAllergies(Member member, Long teamMemberId) {
        if (teamMemberId == null) {
            return avoidItemRepository.findById(member.getId())
                    .map(AvoidItem::getAvoidItems)
                    .orElse(List.of());
        } else {
            TeamMember teamMember =
                    teamMemberRepository.findByIdAndMember(teamMemberId, member)
                            .orElseThrow(() -> new GeneralException(ErrorStatus.TEAM_NOT_FOUND));
            List<Member> members = teamMember.getTeam().getTeamMembers().stream()
                    .map(TeamMember::getMember)
                    .toList();

            return avoidItemRepository.findAllByMemberIn(members)
                    .stream()
                    .flatMap(avoidItem -> avoidItem.getAvoidItems().stream())
                    .distinct()
                    .toList();
        }
    }
}
