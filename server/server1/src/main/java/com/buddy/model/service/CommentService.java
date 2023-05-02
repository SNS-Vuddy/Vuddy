package com.buddy.model.service;

import com.buddy.model.entity.Comments;
import com.buddy.model.entity.Feed;
import com.buddy.model.entity.User;
import com.buddy.model.repository.CommentsRepository;
import com.buddy.model.repository.FeedRepository;
import com.buddy.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final CommentsRepository commentsRepository;

    @Transactional
    public void createComment(String nickname, Long feedId, String content) {
        User user = userRepository.findByNickname(nickname);
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 피드입니다."));
        Comments comments = Comments.createComment(user, feed, content);
        commentsRepository.save(comments);
    }
}
