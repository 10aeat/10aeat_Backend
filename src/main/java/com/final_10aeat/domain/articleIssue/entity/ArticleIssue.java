package com.final_10aeat.domain.articleIssue.entity;

import com.final_10aeat.domain.manageArticle.entity.ManageArticle;
import com.final_10aeat.domain.manager.entity.Manager;
import com.final_10aeat.domain.repairArticle.entity.RepairArticle;
import com.final_10aeat.global.entity.SoftDeletableBaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder
@Table(name = "article_issue")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleIssue extends SoftDeletableBaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Setter
    private String title;

    @Column(columnDefinition = "TEXT")
    @Setter
    private String content;

    @Setter
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manage_article_id", referencedColumnName = "id")
    private ManageArticle manageArticle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repair_article_id", referencedColumnName = "id")
    private RepairArticle repairArticle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    public ArticleIssue(String title, String content, ManageArticle manageArticle,
        LocalDateTime createdAt, Manager manager) {
        this.title = title;
        this.content = content;
        this.manageArticle = manageArticle;
        this.createdAt = createdAt;
        this.manager = manager;
    }

    public ArticleIssue(String title, String content, RepairArticle repairArticle,
        LocalDateTime createdAt, Manager manager) {
        this.title = title;
        this.content = content;
        this.repairArticle = repairArticle;
        this.createdAt = createdAt;
        this.manager = manager;
    }

    public void delete(LocalDateTime currentTime) {
        super.delete(currentTime);
    }
}
