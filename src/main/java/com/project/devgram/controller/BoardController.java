package com.project.devgram.controller;

import com.project.devgram.dto.CommonDto;
import com.project.devgram.dto.DetailResponse;
import com.project.devgram.dto.RegisterBoard;
import com.project.devgram.dto.SearchBoard.Response;
import com.project.devgram.dto.UpdateBoard;
import com.project.devgram.oauth2.token.TokenService;
import com.project.devgram.service.BoardService;
import com.project.devgram.service.BoardServiceContainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/boards")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;
	private final BoardServiceContainer boardServiceContainer;
	private final TokenService tokenService;
	@PostMapping
	public CommonDto<RegisterBoard.Response> registerBoard(
								   @RequestPart(value = "board") @Valid RegisterBoard.Request request, BindingResult bindingResult,
								   @RequestPart(required = false) MultipartFile file , HttpServletRequest servletRequest) throws IOException {
		String username = tokenService.getUsername(servletRequest.getHeader("Authentication"));
		RegisterBoard.Response res = boardServiceContainer.registerBoard(username, request, file);

		return new CommonDto<>(HttpStatus.OK.value(),res);
	}

	@GetMapping
	public Page<Response> searchBoards(@PageableDefault(page = 0, size = 5) Pageable pageable, String sort,
		@RequestParam(required = false) List<Long> tagSeqList) {
		return boardService.searchBoards(pageable, sort, tagSeqList);
	}

	@GetMapping("/{boardSeq}")
	public DetailResponse detailBoards(@PathVariable Long boardSeq) {
		return boardService.searchBoardDetail(boardSeq);
	}

	@GetMapping("/follow")
	public Page<Response> searchFollowingBoards(@PageableDefault(page = 0, size = 5) Pageable pageable,
		@RequestParam(required = false) List<Long> tagSeqList, HttpServletRequest servletRequest) {
		String username = tokenService.getUsername(servletRequest.getHeader("Authentication"));

		return boardService.searchFollowingBoards(username ,pageable, tagSeqList);
	}

	@PutMapping
	public UpdateBoard.Response updateBoard(@RequestBody UpdateBoard.Request request) {
		return UpdateBoard.Response.of(boardService.updateBoard(request));
	}

	@DeleteMapping("/{boardSeq}")
	public void deleteBoard(@PathVariable Long boardSeq) {
		boardService.deleteBoard(boardSeq);
	}

}
