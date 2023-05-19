package com.edu.ssafy.comment.controller;

import com.edu.ssafy.comment.model.dto.common.CommonRes;
import com.edu.ssafy.comment.model.dto.request.CommentWriteReq;
import com.edu.ssafy.comment.model.service.CommentService;
import com.edu.ssafy.comment.util.NicknameUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/opened/health")
    public String healthCheck() {
        return "hello world";
    }

    // 댓글 작성
    @PostMapping("/write/{feedId}")
    public ResponseEntity<CommonRes> writeComment(@RequestHeader("x_nickname") String encodedNickname, @PathVariable Long feedId, @RequestBody CommentWriteReq req) {

        String userNickname = NicknameUtil.decodeNickname(encodedNickname);
        commentService.createComment(userNickname, feedId, req.getComment());

        return new ResponseEntity<>(new CommonRes(201, "댓글 작성 완료"), HttpStatus.CREATED);
    }

    @PostMapping("/write/test/{feedId}")
    public ResponseEntity<CommonRes> writeComment(
            @RequestHeader("x_nickname") String encodedNickname,
            @PathVariable Long feedId,
            @RequestBody Object req
    ) {

        System.out.println("req = " + req.toString());
        String userNickname = NicknameUtil.decodeNickname(encodedNickname);
//        commentService.createComment(userNickname, feedId, req.getComment());

        return new ResponseEntity<>(new CommonRes(201, "댓글 작성 완료"), HttpStatus.CREATED);
    }
}
