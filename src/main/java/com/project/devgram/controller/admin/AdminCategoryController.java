package com.project.devgram.controller.admin;

import com.project.devgram.dto.CategoryDto;
import com.project.devgram.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping(value="/admin/api/categories")
public class AdminCategoryController {

	private final CategoryService categoryService;


	@GetMapping
	public List<CategoryDto> list(HttpServletRequest request) {

		return categoryService.list();
	}


	@PostMapping
	public boolean add(@RequestBody CategoryDto parameter) {

		return categoryService.add(parameter);
	}

	@PutMapping("/update")
	public boolean update(@RequestBody CategoryDto parameter) {

		return categoryService.update(parameter);
	}

	@DeleteMapping("/delete")
	public boolean del(@RequestBody CategoryDto parameter) {

		return categoryService.del(parameter.getCategory_Seq());

	}
}
