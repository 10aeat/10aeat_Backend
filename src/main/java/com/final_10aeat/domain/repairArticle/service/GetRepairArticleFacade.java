package com.final_10aeat.domain.repairArticle.service;

import com.final_10aeat.common.dto.UserIdAndRole;
import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.common.service.AuthenticationService;
import com.final_10aeat.domain.repairArticle.dto.response.CustomProgressResponseDto;
import com.final_10aeat.domain.repairArticle.dto.response.RepairArticleDetailDto;
import com.final_10aeat.domain.repairArticle.dto.response.RepairArticleSummaryDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetRepairArticleFacade {

    private final ManagerArticleListService managerArticleListService;
    private final OwnerArticleListService ownerArticleListService;
    private final GetRepairArticleService getRepairArticleService;
    private final AuthenticationService authenticationService;

    public List<?> getAllRepairArticles(List<Progress> progresses, ArticleCategory category) {
        UserIdAndRole userIdAndRole = authenticationService.getCurrentUserIdAndRole();
        Long officeId = authenticationService.getUserOfficeId();
        if (userIdAndRole.isManager()) {
            return managerArticleListService.getAllRepairArticles(officeId, progresses, category);
        } else {
            return ownerArticleListService.getAllRepairArticles(officeId, userIdAndRole.id(),
                progresses, category);
        }
    }

    public RepairArticleSummaryDto getRepairArticleSummary() {
        Long officeId = authenticationService.getUserOfficeId();
        UserIdAndRole userIdAndRole = authenticationService.getCurrentUserIdAndRole();
        return getRepairArticleService.getRepairArticleSummary(officeId, userIdAndRole);
    }

    public RepairArticleDetailDto getArticleDetails(Long articleId) {
        UserIdAndRole userIdAndRole = authenticationService.getCurrentUserIdAndRole();
        return getRepairArticleService.getArticleDetails(articleId, userIdAndRole.id(),
            userIdAndRole.isManager());
    }

    public List<CustomProgressResponseDto> getCustomProgressList(Long articleId) {
        return getRepairArticleService.getCustomProgressList(articleId);
    }
}