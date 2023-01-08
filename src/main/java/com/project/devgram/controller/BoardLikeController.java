package com.project.devgram.controller;


import com.project.devgram.dto.CommonDto;
import com.project.devgram.dto.RegisterBoardLike;
import com.project.devgram.dto.SearchBoardLike;
import com.project.devgram.oauth2.token.TokenService;
import com.project.devgram.service.BoardLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/boards/like")
@RequiredArgsConstructor
public class BoardLikeController {

	private final BoardLikeService boardLikeService;
	private final TokenService tokenService;
	@GetMapping
	public List<SearchBoardLike.Response> searchBoardLike(SearchBoardLike.Request request) {
		return SearchBoardLike.Response.listOf(boardLikeService.searchBoardLike(request));
	}

	@PostMapping
	public CommonDto<RegisterBoardLike.Response> registerBoardLike(@RequestBody @Valid RegisterBoardLike.Request request, BindingResult bindingResult,
																   HttpServletRequest servletRequest) {
		String username = tokenService.getUsername(servletRequest.getHeader("Authentication"));
		return new CommonDto<>(HttpStatus.OK.value(),RegisterBoardLike.Response.of(boardLikeService.registerBoardLike(request.getBoardSeq(),username)));
	}

	@DeleteMapping("{boardLikeSeq}")
	public void deleteBoardLike(@PathVariable Long boardLikeSeq) {
		boardLikeService.deleteBoardLike(boardLikeSeq);
	}

}