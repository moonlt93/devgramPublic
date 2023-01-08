package com.project.devgram.controller;

import com.project.devgram.dto.ReviewAccuseDto;
import com.project.devgram.dto.ReviewDto;
import com.project.devgram.oauth2.token.TokenService;
import com.project.devgram.service.ReviewAccuseService;
import com.project.devgram.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

	private final ReviewService reviewService;
	private final ReviewAccuseService reviewAccuseService;
	private final TokenService tokenService;
	@PostMapping// 리뷰 등록
	public boolean write(@RequestBody ReviewDto parameter, HttpServletRequest request){
		parameter.setUsername(tokenService.getUsername(request.getHeader("Authentication")));
		return  reviewService.ReviewWrite(parameter);
	}

	@GetMapping("/list")// seq에 따른 목록
	public List<ReviewDto> list(@RequestParam Long productSeq){
		return reviewService.Reviewlist(productSeq);
	}

	@PostMapping("/update") // 리뷰 수정
	public boolean reviewUpdate(@RequestBody ReviewDto parameter){
		return reviewService.updateReview(parameter);
	}

	@PostMapping("/delete") // 리뷰 삭제
	public boolean reviewDelete(@RequestBody ReviewDto parameter){
		return reviewService.deleteReview(parameter);
	}

	@PostMapping("/accuse") // 리뷰 신고
	public ReviewAccuseDto reviewAccuse(@RequestBody ReviewAccuseDto parameter, HttpServletRequest request){
		parameter.setUsername(tokenService.getUsername(request.getHeader("Authentication")));
		return reviewAccuseService.reviewAccuse(parameter);
	}


}
