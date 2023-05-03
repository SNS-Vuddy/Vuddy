package com.buddy.controller;

import com.buddy.jwt.TokenProvider;
import com.buddy.model.dto.common.CommonRes;
import com.buddy.model.dto.request.CommentWriteReq;
import com.buddy.model.service.CommentService;
import com.buddy.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final TokenProvider tokenProvider;
    private final TokenUtil tokenUtil;

    @PostMapping("/write/{feedId}")
    @PreAuthorize("hasAuthority('NORMAL_USER') or hasAuthority('KAKAO_USER')")
    public ResponseEntity<CommonRes> writeComment(@RequestHeader("Authorization") String token, @PathVariable Long feedId, @RequestBody CommentWriteReq req) {

        String userNickname = tokenProvider.getUserNicknameFromToken(token);
//        String userNickname = tokenUtil.getUserNicknameFromToken(token);
        commentService.createComment(userNickname, feedId, req.getContent());

        return new ResponseEntity<>(new CommonRes(201, "댓글 작성 완료"), HttpStatus.CREATED);
    }
}
