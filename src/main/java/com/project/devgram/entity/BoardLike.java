package com.project.devgram.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(
	name = "board_like",
	uniqueConstraints = {
		@UniqueConstraint(
			name = "board_user_constratint",
			columnNames = {"board_seq", "user_seq"}
		)
	}
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardLike extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board_like_seq", nullable = false)
	private Long boardLikeSeq;

	@ManyToOne
	@JoinColumn(name = "board_seq")
	private Board board;

	@ManyToOne
	@JoinColumn(name = "user_seq")
	private Users user;

	private String createdBy;
	private String updatedBy;
}