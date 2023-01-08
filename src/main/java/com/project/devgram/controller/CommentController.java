package com.project.devgram.controller;

import com.project.devgram.dto.CommentAccuseDto;
import com.project.devgram.dto.CommentDto;
import com.project.devgram.dto.CommentResponse.GroupComment;
import com.project.devgram.dto.CommonDto;
import com.project.devgram.oauth2.token.TokenService;
import com.project.devgram.service.CommentService;
import com.project.devgram.util.pagerequest.CommentPageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final TokenService tokenService;

    /*
     * 댓글 등록 api(로그인 필요)
    */
    @PostMapping
    public CommonDto<CommentDto> addComment(@RequestBody @Valid CommentDto commentInput, BindingResult bindingResult, HttpServletRequest request) {
        commentInput.setCreatedBy(tokenService.getUsername(request.getHeader("Authentication")));

        return new CommonDto<>(HttpStatus.OK.value(),commentService.addComment(commentInput));
    }

    /*
     * 댓글 조회(로그인 불필요)
     */
    @GetMapping
    public List<GroupComment> getCommentList(@RequestParam Long boardSeq, CommentPageRequest commentPageRequest) {
        return commentService.getCommentList(boardSeq, commentPageRequest);
    }


    /*
     * 댓글 삭제(로그인 필요)
     */
    @DeleteMapping
    public String deleteComment(@RequestParam Long commentSeq) {
        return commentService.deleteComment(commentSeq);
    }

    /*
     * 댓글 신고(로그인 필요)
     */
    @PostMapping(value="/accuse")
    public CommentAccuseDto accuseComment(@RequestBody CommentAccuseDto commentAccuseDto, HttpServletRequest request) {
        commentAccuseDto.setCreatedBy(tokenService.getUsername(request.getHeader("Authentication")));
        return commentService.accuseComment(commentAccuseDto);
    }

    /*
     * 댓글 내용 업데이트(로그인 필요)
     */
    @PutMapping("/content")
    public CommentDto updateCommentContent(@RequestBody CommentDto commentDto, HttpServletRequest request) {
        commentDto.setUpdatedBy(tokenService.getUsername(request.getHeader("Authentication")));
        return commentService.updateCommentContent(commentDto);
    }
}
