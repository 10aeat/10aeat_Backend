package com.final_10aeat.domain.repairArticle.service;

import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.articleIssue.repository.ArticleIssueCheckRepository;
import com.final_10aeat.domain.comment.repository.CommentRepository;
import com.final_10aeat.domain.repairArticle.dto.response.OwnerRepairArticleResponseDto;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.domain.repairArticle.repository.RepairArticleRepository;
import com.final_10aeat.domain.save.repository.ArticleSaveRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OwnerArticleListService {

    private final RepairArticleRepository repairArticleRepository;
    private final CommentRepository commentRepository;
    private final ArticleSaveRepository articleSaveRepository;
    private final ArticleIssueCheckRepository articleIssueCheckRepository;

    public Page<OwnerRepairArticleResponseDto> getAllRepairArticles(Long officeId, Long userId,
        List<Progress> progresses, ArticleCategory category, Pageable pageable) {

        List<Long> checkedIssueIds = getCheckedIssueIdsForCurrentUser(userId);

        Page<RepairArticle> articles = repairArticleRepository.findByOfficeIdAndProgressInAndCategoryOrderByIdDesc(
            officeId, progresses, category, pageable, checkedIssueIds);

        Set<Long> savedArticleIds = findSavedArticleIds(userId, articles);
        Set<Long> checkedIssueIdSet = Set.copyOf(checkedIssueIds);

        return articles.map(article -> mapToDto(article, savedArticleIds, checkedIssueIdSet));
    }

    private OwnerRepairArticleResponseDto mapToDto(RepairArticle article, Set<Long> savedArticleIds,
        Set<Long> checkedIssueIds) {
        int commentCount = (int) commentRepository.countByRepairArticleIdAndDeletedAtIsNull(
            article.getId());
        boolean isSaved = savedArticleIds.contains(article.getId());
        boolean hasIssue = article.hasIssue() && article.getActiveIssueId()
            .map(id -> !checkedIssueIds.contains(id))
            .orElse(false);
        int viewCount = article.getViewCount();
        Long activeIssueId = article.getActiveIssueId().orElse(null);

        return new OwnerRepairArticleResponseDto(
            article.getId(),
            article.getCategory().name(),
            article.getManager().getName(),
            article.getProgress().name(),
            article.getTitle(),
            article.getStartConstruction(),
            article.getEndConstruction(),
            article.getCreatedAt(),
            article.getUpdatedAt(),
            commentCount,
            viewCount,
            isSaved,
            hasIssue,
            article.getImages().isEmpty() ? null
                : article.getImages().iterator().next().getImageUrl(),
            activeIssueId
        );
    }

    public Page<OwnerRepairArticleResponseDto> search(
        Long userOfficeId, Long userId, String keyword, Pageable pageRequest
    ) {
        Page<RepairArticle> articles = repairArticleRepository.searchByTextAnsOfficeId(userOfficeId,
            keyword, pageRequest);

        Set<Long> savedArticleIds = findSavedArticleIds(userId, articles);
        Set<Long> checkedIssueIds = Set.copyOf(getCheckedIssueIdsForCurrentUser(userId));

        return articles.map(article -> mapToDto(article, savedArticleIds, checkedIssueIds));
    }

    public List<Long> getCheckedIssueIdsForCurrentUser(Long userId) {
        return new ArrayList<>(articleIssueCheckRepository.findCheckedIssueIdsByMember(userId));
    }

    private Set<Long> findSavedArticleIds(Long userId, Page<RepairArticle> articles) {
        List<Long> articleIds = articles.getContent().stream().map(RepairArticle::getId)
            .collect(Collectors.toList());
        return Set.copyOf(articleSaveRepository.findSavedArticleIdsByMember(userId, articleIds));
    }
}
