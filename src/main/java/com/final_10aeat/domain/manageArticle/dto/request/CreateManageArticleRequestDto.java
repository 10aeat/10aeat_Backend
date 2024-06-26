package com.final_10aeat.domain.manageArticle.dto.request;

import com.final_10aeat.common.enumclass.ManagePeriod;
import com.final_10aeat.common.dto.validator.ListNotEmpty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateManageArticleRequestDto(
    @NotNull
    ManagePeriod period,
    @NotNull
    Integer periodCount,
    @NotBlank
    String title,
    String legalBasis,
    String target,
    String responsibility,
    String note,
    @Valid @ListNotEmpty(message = "일정은 1개 이상 존재해야 합니다")
    List<CreateScheduleRequestDto> schedule
) {

}
