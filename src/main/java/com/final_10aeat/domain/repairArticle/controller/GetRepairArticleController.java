package com.final_10aeat.domain.repairArticle.controller;

import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.repairArticle.dto.response.CustomProgressResponseDto;
import com.final_10aeat.domain.repairArticle.dto.response.RepairArticleDetailDto;
import com.final_10aeat.domain.repairArticle.dto.response.RepairArticleSummaryDto;
import com.final_10aeat.domain.repairArticle.service.GetRepairArticleFacade;
import com.final_10aeat.global.util.ResponseDTO;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/repair/articles")
public class GetRepairArticleController {

    private final GetRepairArticleFacade getRepairArticleFacade;

    @GetMapping("/summary")
    public ResponseEntity<ResponseDTO<RepairArticleSummaryDto>> getRepairArticleSummary() {
        RepairArticleSummaryDto summary = getRepairArticleFacade.getRepairArticleSummary();
        return ResponseEntity.ok(ResponseDTO.okWithData(summary));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDTO<List<?>>> getAllRepairArticles(
        @RequestParam(required = false) List<String> progress,
        @RequestParam(required = false) ArticleCategory category) {

        List<Progress> progressList = determineProgressFilter(progress);

        List<?> articles = getRepairArticleFacade.getAllRepairArticles(progressList, category);
        return ResponseEntity.ok(ResponseDTO.okWithData(articles));
    }

    @GetMapping("/{repairArticleId}")
    public ResponseEntity<ResponseDTO<RepairArticleDetailDto>> getRepairArticleDetail(
        @PathVariable Long repairArticleId) {
        RepairArticleDetailDto articleDetails = getRepairArticleFacade.getArticleDetails(
            repairArticleId);

        return ResponseEntity.ok(ResponseDTO.okWithData(articleDetails));
    }

    @GetMapping("/progress/{repairArticleId}")
    public ResponseEntity<ResponseDTO<List<CustomProgressResponseDto>>> getCustomProgressList(
        @PathVariable Long repairArticleId) {
        List<CustomProgressResponseDto> customProgressList = getRepairArticleFacade.getCustomProgressList(
            repairArticleId);
        return ResponseEntity.ok(ResponseDTO.okWithData(customProgressList));
    }

    private List<Progress> determineProgressFilter(List<String> progress) {
        if (progress == null || progress.isEmpty()) {
            return Arrays.asList(Progress.values());
        }
        return progress.stream()
            .map(String::toUpperCase)
            .map(Progress::valueOf)
            .collect(Collectors.toList());
    }
}