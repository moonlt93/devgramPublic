package com.project.devgram.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
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
public class CommentAccuse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentAccuseSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_seq", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Users createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    private String accuseReason;
}
