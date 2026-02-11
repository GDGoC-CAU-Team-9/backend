package com.gdg_team9.SafePlate.restaurant.service;

import com.gdg_team9.SafePlate.file.service.FileService;
import com.gdg_team9.SafePlate.member.domain.Member;
import com.gdg_team9.SafePlate.restaurant.domain.SearchHistory;
import com.gdg_team9.SafePlate.restaurant.dto.SearchHistoryResponse;
import com.gdg_team9.SafePlate.restaurant.repository.SearchHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchHistoryService {
    private final SearchHistoryRepository searchHistoryRepository;

    private final FileService fileService;

    private static final int pageSize = 10;

    public SearchHistoryResponse.PageResult getMemberHistories(Member member, int pageNumber) {
        PageRequest page = PageRequest.of(
                pageNumber - 1,
                pageSize,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<SearchHistory> searchHistories = searchHistoryRepository.findByMember(member, page);

        List<SearchHistoryResponse.PageResult.Item> items = searchHistories.getContent().stream()
                .map(this::toItem)
                .toList();

        return SearchHistoryResponse.PageResult.builder()
                .searchHistory(items)
                .totalPages(searchHistories.getTotalPages())
                .totalElements(searchHistories.getTotalElements())
                .build();
    }

    // 가독성을 위해 분리
    private SearchHistoryResponse.PageResult.Item toItem(SearchHistory searchHistory) {
        return SearchHistoryResponse.PageResult.Item.builder()
                .id(searchHistory.getId())
                // 검색 후에는 이미지 주인 관계 없이 가져오기
                // 추후 친구 기능 추가 시 다른 친구가 올린 이미지 확인이 필요하기 때문
                .imageUrls(fileService.getFileUrlsByIds(searchHistory.getImageIds()))
                .searchResult(searchHistory.getSearchResult())
                .createdAt(searchHistory.getCreatedAt())
                .build();
    }
}
