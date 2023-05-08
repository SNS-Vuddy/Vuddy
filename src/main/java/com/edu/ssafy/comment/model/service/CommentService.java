package com.edu.ssafy.comment.model.service;

import com.edu.ssafy.comment.model.entity.Comments;
import com.edu.ssafy.comment.model.entity.Feed;
import com.edu.ssafy.comment.model.entity.User;
import com.edu.ssafy.comment.model.repository.CommentRepository;
import com.edu.ssafy.comment.model.repository.FeedRepository;
import com.edu.ssafy.comment.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createComment(String nickname, Long feedId, String content) {
        User user = userRepository.findByNickname(nickname);
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 피드입니다."));
        Comments comments = Comments.createComment(user, feed, content);
        commentRepository.save(comments);
    }
}
