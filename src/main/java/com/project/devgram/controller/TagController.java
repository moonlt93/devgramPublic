package com.project.devgram.controller;

import com.project.devgram.dto.TagDto;
import com.project.devgram.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    /*
     * 전체 태그 목록 조회
     */
    @GetMapping
    public List<TagDto> getTagList() {
        return tagService.getTagList();
    }

    /*
     * 특정 태그 삭제
     */
    @DeleteMapping
    public String deleteTag(@RequestParam Long tagSeq) {
        return tagService.deleteTag(tagSeq);
    }

    /*
     * 태그 자동 완성
     */
    @GetMapping("/autocomplete")
    public List<TagDto> autocompleteTag(@RequestParam("searchTag") String searchTag) {
        return tagService.autoCompleteTag(searchTag);
    }

    /*
     * 인기 태그 조회(8개, 태그 사용 순)
     */
    @GetMapping("/popular")
    public List<TagDto> getPopularTagList() {
        return tagService.getPopularTagList();
    }
}
