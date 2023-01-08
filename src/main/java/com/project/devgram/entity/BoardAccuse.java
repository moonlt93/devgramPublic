package com.project.devgram.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(
	name="board_accuse",
	uniqueConstraints={
		@UniqueConstraint(
			name="board_accuse_constraint",
			columnNames={"board_seq", "user_seq"}
		)
	}
)@Builder
public class BoardAccuse extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board_accuse_seq", nullable = false)
	private Long boardAccuseSeq;

	@Column(columnDefinition = "TEXT")
	private String content;

	@ManyToOne
	@JoinColumn(name = "board_seq")
	private Board board;

	@ManyToOne
	@JoinColumn(name = "user_seq")
	private Users user;

	private String createdBy;
	private String updatedBy;
}
