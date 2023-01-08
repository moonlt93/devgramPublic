package com.project.devgram.controller.admin;

import com.project.devgram.dto.ProductDto;
import com.project.devgram.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/products")
public class AdminProductController {

	private final ProductService productService;



	@GetMapping // admin 페이지 - 전체조회 (paging limit 5 내림차순)
	public List<ProductDto> confirm(@PageableDefault(page = 0, size = 5, direction = Direction.DESC) Pageable pageable) {
		return productService.confirm(pageable);
	}


	@PutMapping("/update") // admin - 제품 수정
	public boolean update(@RequestBody ProductDto parameter) {

		return productService.update(parameter);

	}

	@DeleteMapping("/delete") // admin - 제품 삭제
	public boolean delete(@RequestBody ProductDto parameter) {

		return productService.delete(parameter.getProduct_Seq());
	}


}
