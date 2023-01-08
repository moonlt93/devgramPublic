package com.project.devgram.entity;

import com.project.devgram.type.CommentStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentSeq; // 댓글 번호

    private String content; // 댓글 내용
    private Long commentGroup; // 댓글 그룹
    private Long parentCommentSeq; // 부모 댓글 번호
    private String parentCommentCreatedBy; // 부모 댓글 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_seq", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Board board; // 게시글 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_By", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Users createdBy; // 작성자

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Users updatedBy; // 수정자

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private CommentStatus commentStatus; // 댓글 상태
}
