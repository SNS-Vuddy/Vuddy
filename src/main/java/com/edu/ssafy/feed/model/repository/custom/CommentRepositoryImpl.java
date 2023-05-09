package com.edu.ssafy.feed.model.repository.custom;

import com.edu.ssafy.feed.model.dto.CommentDto;
import com.edu.ssafy.feed.model.entity.Feed;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.querydsl.core.types.Projections;


import java.util.ArrayList;
import java.util.List;

import static com.edu.ssafy.feed.model.entity.QComments.comments;
import static com.edu.ssafy.feed.model.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentDto> findAllByFeed(Feed feed){
        List<CommentDto> result = queryFactory
                .select(Projections.constructor(CommentDto.class, user.nickname, user.profileImage, comments.content, comments.createdAt))
                .from(comments)
                .join(user).on(comments.user.id.eq(user.id))
                .where(comments.feed.id.eq(feed.getId()))
                .orderBy(comments.createdAt.desc())
                .fetch();

        if (result.isEmpty()) {
            return new ArrayList<>();
        }

        return result;
    }

}
