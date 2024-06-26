package com.final_10aeat.domain.repairArticle.entity;

import com.final_10aeat.common.enumclass.ArticleCategory;
import com.final_10aeat.common.enumclass.Progress;
import com.final_10aeat.domain.articleIssue.entity.ArticleIssue;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.office.entity.Office;
import com.final_10aeat.global.entity.SoftDeletableBaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder
@Table(name = "repair_article")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RepairArticle extends SoftDeletableBaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Progress progress = Progress.INPROGRESS;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArticleCategory category;

    @Setter
    @Column
    private String title;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String content;

    @Setter
    @Column(name = "construction_start")
    private LocalDateTime startConstruction;

    @Setter
    @Column(name = "construction_end")
    private LocalDateTime endConstruction;

    @Setter
    @Column(name = "repair_company")
    private String company;

    @Setter
    @Column(name = "repair_company_website", columnDefinition = "TEXT")
    private String companyWebsite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @Setter
    @OneToMany(mappedBy = "repairArticle", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RepairArticleImage> images = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "repairArticle", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CustomProgress> customProgressSet;

    @Setter
    @Column(name = "view_count", nullable = false)
    private int viewCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "office_id")
    private Office office;

    public void delete(LocalDateTime currentTime) {
        super.delete(currentTime);
    }

    @OneToMany(mappedBy = "repairArticle", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ArticleIssue> issues;

    public boolean hasIssue() {
        return this.issues.stream().anyMatch(ArticleIssue::isEnabled);
    }

    public Optional<Long> getActiveIssueId() {
        return this.issues.stream()
            .filter(ArticleIssue::isEnabled)
            .findFirst()
            .map(ArticleIssue::getId);
    }
}
