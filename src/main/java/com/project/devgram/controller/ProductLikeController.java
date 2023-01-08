package com.project.devgram.controller;

import com.project.devgram.dto.ProductLikeDto;
import com.project.devgram.oauth2.token.TokenService;
import com.project.devgram.service.ProductLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products/like")
public class ProductLikeController {

	private final ProductLikeService productLikeService;
	private final TokenService tokenService;
	@PostMapping // 좋아요 누르기 + likeCount 증가
	public ResponseEntity<ProductLikeDto> productLike(@RequestBody ProductLikeDto productLikeDto, HttpServletRequest request) {
		productLikeDto.setUsername(tokenService.getUsername(request.getHeader("Authentication")));
		productLikeService.productLike(productLikeDto);
		return new ResponseEntity<>(productLikeDto, HttpStatus.CREATED);

	}

	@DeleteMapping("/delete") // 좋아요 삭제 + likeCount 감소
	public ResponseEntity<ProductLikeDto> productUnLike(@RequestBody ProductLikeDto productLikeDto, HttpServletRequest request) {
		productLikeDto.setUsername(tokenService.getUsername(request.getHeader("Authentication")));
		productLikeService.productUnLike(productLikeDto);
		return new ResponseEntity<>(productLikeDto, HttpStatus.OK);
	}

	@GetMapping("/list") // username 에 따른 목록 반환
	public List<ProductLikeDto> list(HttpServletRequest request){
		String token = request.getHeader("Authentication");
		String username = tokenService.getUsername(token);
		return productLikeService.list(username);
	}

}
