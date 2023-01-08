package com.project.devgram.controller.admin;

import com.project.devgram.dto.CommentAccuseDto;
import com.project.devgram.dto.CommentDto;
import com.project.devgram.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/admin/api/comments")
public class AdminCommentController {

    private final CommentService commentService;



    /*
     * 신고 댓글 조회(관리자 권한 필요)
     */
    @GetMapping("/accuse")
    public List<CommentDto> getAccusedCommentList() {
        return commentService.getAccusedCommentList();
    }

    /*
     * 특정 신고 댓글 신고 내역 조회(관리자 권한 필요)
     */
    @GetMapping("/accuse/{commentSeq}")
    public List<CommentAccuseDto> getAccusedCommentDetail(@PathVariable String commentSeq) {
        Long seq = Long.parseLong(commentSeq);
        return commentService.getAccusedCommentDetail(seq);
    }

    /*
     * 댓글 상태 업데이트(관리자 권한 필요)
     */
    @PutMapping("/status")
    public String updateCommentStatus(@RequestBody CommentDto commentDto) {
        return commentService.updateCommentStatus(commentDto.getCommentSeq(), commentDto.getCommentStatus());
    }

}
