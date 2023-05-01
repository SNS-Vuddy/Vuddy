package com.buddy.controller;

import com.buddy.model.dto.common.CommonRes;
import com.buddy.model.dto.request.CommentWriteReq;
import com.buddy.model.entity.User;
import com.buddy.model.service.CommentService;
import com.buddy.model.service.UserService;
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


    @PostMapping("/write")
    @PreAuthorize("hasAuthority('NORMAL_USER') or hasAuthority('KAKAO_USER')")
    public ResponseEntity<CommonRes> writeComment(@RequestHeader("Authorization") String token, @RequestBody CommentWriteReq req) {

        String userNickname = TokenUtil.getUserNicknameFromToken(token);
        commentService.createComment(userNickname, req.getFeedId(), req.getContent());

        return new ResponseEntity<>(new CommonRes(201, "댓글 작성 완료"), HttpStatus.CREATED);
    }
}
