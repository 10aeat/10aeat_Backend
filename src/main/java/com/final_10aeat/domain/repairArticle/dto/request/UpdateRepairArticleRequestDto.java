package com.final_10aeat.domain.repairArticle.dto.request;

import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import java.time.LocalDateTime;

public record UpdateRepairArticleRequestDto(
    ArticleCategory category,
    Progress progress,
    String title,
    String content,
    LocalDateTime constructionStart,
    LocalDateTime constructionEnd,
    String repairCompany,
    String repairCompanyWebsite
) {

}
