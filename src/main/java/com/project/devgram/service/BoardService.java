package com.project.devgram.service;

import com.project.devgram.dto.BoardDto;
import com.project.devgram.dto.DetailResponse;
import com.project.devgram.dto.RegisterBoard.Request;
import com.project.devgram.dto.SearchBoard.Response;
import com.project.devgram.dto.UpdateBoard;
import com.project.devgram.entity.Board;
import com.project.devgram.entity.Follow;
import com.project.devgram.entity.Users;
import com.project.devgram.exception.DevGramException;
import com.project.devgram.exception.errorcode.BoardErrorCode;
import com.project.devgram.exception.errorcode.UserErrorCode;
import com.project.devgram.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	private final CommentRepository commentRepository;
	private final BoardTagRepository boardTagRepository;
	private final BoardProductRepository boardProductRepository;
	private final UserRepository userRepository;
	private final FollowRepository followRepository;

	public Board registerBoard(Request request, String username) {
		return boardRepository.save(Board
			.builder()
			.content(request.getContent())
			.title(request.getTitle())
			.bestProduct(request.getBestProduct())
			.otherProduct(request.getOtherProduct())
			.recommendReason(request.getRecommendReason())
			.selfIntroduce(request.getSelfIntroduce())
			.precautions(request.getPrecautions())
			.createdBy(username)
			.updatedBy(username)
			.build());
	}

	@Transactional(readOnly = true)
	public Page<Response> searchBoards(Pageable pageable, String sort, List<Long> tagSeqList) {
		Page<Response> responsePage = boardRepository.findBy(pageable, sort, tagSeqList);
		for (Response res : responsePage.getContent()) {
			res.setCommentsCount(commentRepository.countByBoard_BoardSeq(res.getId()));
			res.setTags(boardTagRepository.getTagNameByBoardSeq(res.getId()));
			res.setProducts(boardProductRepository.findByBoardSeq(res.getId()));
		}
		return responsePage;
	}

	@Transactional(readOnly = true)
	public Page<Response> searchFollowingBoards(String username, Pageable pageable, List<Long> tagSeqList) {
		Users user = userRepository.findByUsername(username).orElseThrow(() -> new DevGramException(UserErrorCode.USER_NOT_EXIST));

		List<Follow> followList = followRepository.findByFollower_UserSeqOrderByFollower(user.getUserSeq());
		List<Long> followerList = followList.stream().map(Follow -> Follow.getFollowing().getUserSeq()).collect(Collectors.toList());

		Page<Response> responsePage = boardRepository.findByFollowerUserSeq(pageable, followerList, tagSeqList);
		for (Response res : responsePage.getContent()) {
			res.setCommentsCount(commentRepository.countByBoard_BoardSeq(res.getId()));
			res.setTags(boardTagRepository.getTagNameByBoardSeq(res.getId()));
			res.setProducts(boardProductRepository.findByBoardSeq(res.getId()));
		}

		return responsePage;
	}

	@Transactional
	public BoardDto updateBoard(UpdateBoard.Request request) {
		Board board = getBoard(request.getBoardSeq());

		board.updateTitle(request.getTitle());
		board.updatePrecautions(request.getPrecautions());
		board.updateSelfIntroduce(request.getSelfIntroduce());
		board.updateRecommendReason(request.getRecommendReason());
		board.updateBestProduct(request.getBestProduct());
		board.updateOtherProduct(request.getOtherProduct());
		board.updateContent(request.getContent());

		return BoardDto.fromEntity(board);
	}

	@Transactional
	public void deleteBoard(Long boardSeq) {
		Board board = getBoard(boardSeq);
		board.deleteBoard();
	}

	public Board getBoard(Long boardSeq) {
		return boardRepository.findById(boardSeq).orElseThrow(() ->
			new DevGramException(BoardErrorCode.CANNOT_FIND_BOARD_BY_BOARDSEQ));
	}

	@Transactional(readOnly = true)
	public DetailResponse searchBoardDetail(Long boardSeq) {
		DetailResponse detailResponse = boardRepository.findDetailByBoardSeq(boardSeq);
		detailResponse.setProductUsed(boardProductRepository.findByBoardSeq(boardSeq));
		detailResponse.setTagCount(commentRepository.countByBoard_BoardSeq(boardSeq));
		return detailResponse;
	}
}