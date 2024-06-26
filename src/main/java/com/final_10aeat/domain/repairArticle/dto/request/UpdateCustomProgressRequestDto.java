package com.final_10aeat.domain.repairArticle.dto.request;

import java.time.LocalDateTime;

public record UpdateCustomProgressRequestDto(
    LocalDateTime startSchedule,
    String title,
    String content,
    Boolean inProgress
) {

}
