package com.project.devgram.dto;

import com.project.devgram.entity.Board;
import com.project.devgram.entity.BoardLike;
import com.project.devgram.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardLikeDto {

	private Long boardLikeSeq;
	private Long userSeq;
	private String userName;
	private Long boardSeq;
	private String boardTitle;

	public static BoardLike toEntity(Board board, Users user,String userName) {
		return BoardLike.builder()
			.board(board)
			.user(user)
			.createdBy(userName)
			.updatedBy(userName)
			.build();
	}

	public static BoardLikeDto from(BoardLike boardLike) {
		return BoardLikeDto.builder()
			.boardLikeSeq(boardLike.getBoardLikeSeq())
			.userSeq(boardLike.getUser().getUserSeq())
			.userName(boardLike.getUser().getUsername())
			.boardSeq(boardLike.getBoard().getBoardSeq())
			.boardTitle(boardLike.getBoard().getTitle())
			.build();
	}
}