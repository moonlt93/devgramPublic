package com.project.devgram.controller;

import com.project.devgram.dto.ProductDto;
import com.project.devgram.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;

	@PostMapping // 일반회원 글작성
	public boolean write(@RequestPart(value = "product") ProductDto parameter,
						 @RequestPart(value="file",required = false) MultipartFile file)
			throws IOException {
			return productService.write(parameter, file);
	}

	@GetMapping("/list") // 일반 - 제품목록 list(상태 : APPROVE)(paging limit 5 내림차순)
	public List<ProductDto> list(String status,
		@PageableDefault(page = 0, size = 5, direction = Direction.DESC) Pageable pageable) {
		return productService.list(pageable);
	}

	@GetMapping("/lists") // 페이지 없는 전체 목록
	public List<ProductDto> lists(){
		return productService.productList();
	}

	@GetMapping("/popular") // 일반 - 인기제품 list(조회수순 / limit 5 / 상태 : APPROVE)
	public List<ProductDto> popularList() {
		return productService.popularList();
	}

	@GetMapping("/bestLike") // 일반 - 좋아요 Best(좋아요순 / limit 8 / 상태 : APPROVE)
	public List<ProductDto> bestLikeList(){
		return productService.bestLikeList();
	}

	@GetMapping("/{product_Seq}") // 제품 상세 목록 + 조회수 증가
	public ProductDto productDetail(ProductDto parameter) {
		return productService.detail(parameter.getProduct_Seq());
	}



}
