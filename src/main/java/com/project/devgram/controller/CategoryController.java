package com.project.devgram.controller;

import com.project.devgram.dto.CategoryDto;
import com.project.devgram.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(value="/api/categories")
public class CategoryController {

	private final CategoryService categoryService;


	@GetMapping
	public List<CategoryDto> getCateList() {

		return categoryService.list();
	}


}
