package com.project.devgram.controller.admin;

import com.project.devgram.dto.ReviewAccuseDto;
import com.project.devgram.dto.ReviewDto;
import com.project.devgram.service.ReviewAccuseService;
import com.project.devgram.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/review")
public class AdminReviewController {

	private final ReviewService reviewService;
	private final ReviewAccuseService reviewAccuseService;

	@GetMapping
	public List<ReviewDto> adminList(@PageableDefault(page = 0, size = 5, direction = Direction.DESC) Pageable pageable) {
		return reviewService.adminList(pageable);
	}


	@GetMapping("/accuse/list") // 리뷰 신고 list(관리자)
	public List<ReviewDto> AccuseReviewList(){
		return reviewAccuseService.accuseReviewList();
	}


	@PutMapping("/accuse/statusUpdate") // 리뷰 status 변경(관리자)
	public boolean reviewStatusUpdate(@RequestBody ReviewDto parameter){
		return reviewAccuseService.updateReviewStatus(parameter.getReviewSeq(), parameter);
	}


	@GetMapping("/accuse/detail") // reviewSeq에 따른 신고 목록 조회
	public List<ReviewAccuseDto> AccuseReviewDetail(@RequestParam Long reviewSeq){
		return reviewAccuseService.accuseReviewDetail(reviewSeq);
	}
}
