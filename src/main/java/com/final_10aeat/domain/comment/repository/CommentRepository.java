package com.final_10aeat.domain.comment.repository;

import com.final_10aeat.domain.comment.entity.Comment;
import com.final_10aeat.domain.member.entity.Member;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.deletedAt IS NOT NULL AND c.deletedAt < :cutoffDate")
    List<Comment> findSoftDeletedBefore(@Param("cutoffDate") LocalDateTime cutoffDate);

    List<Comment> findByRepairArticleIdAndDeletedAtIsNull(Long repairArticleId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.repairArticle.id = :repairArticleId AND c.deletedAt IS NULL")
    long countByRepairArticleIdAndDeletedAtIsNull(@Param("repairArticleId") Long repairArticleId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.member WHERE c.member = :member AND c.repairArticle.office.id = :officeId AND c.deletedAt IS NULL")
    List<Comment> findByMemberAndRepairArticleOfficeId(@Param("member") Member member,
        @Param("officeId") Long officeId);
}
