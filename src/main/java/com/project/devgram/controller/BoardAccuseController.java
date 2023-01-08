package com.project.devgram.controller;

import com.project.devgram.dto.RegisterBoardAccuse;
import com.project.devgram.dto.SearchBoardAccuse;
import com.project.devgram.dto.SearchBoardAccuse.Response;
import com.project.devgram.oauth2.token.TokenService;
import com.project.devgram.service.BoardAccuseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/boards/accuse")
@RequiredArgsConstructor
public class BoardAccuseController {

	private final BoardAccuseService boardAccuseService;

	private final TokenService tokenService;

	@GetMapping
	public List<Response> searchBoardAccuse(SearchBoardAccuse.Request request) {
		return Response.listOf(boardAccuseService.searchBoardAccuse(request));
	}


	@PostMapping
	public RegisterBoardAccuse.Response registerBoardAccuse(@RequestBody RegisterBoardAccuse.Request request, HttpServletRequest servletRequest) {
		String username = tokenService.getUsername(servletRequest.getHeader("Authentication"));

		return RegisterBoardAccuse.Response.of(
			boardAccuseService.registerBoardAccuse(username, request.getBoardSeq(), request.getContent()));
	}


}
