package com.edu.ssafy.feed.model.repository.custom;

import com.edu.ssafy.feed.model.dto.FeedWithTagsDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.edu.ssafy.feed.model.entity.QComments.comments;
import static com.edu.ssafy.feed.model.entity.QFeed.feed;
import static com.edu.ssafy.feed.model.entity.QFeedLikes.feedLikes;
import static com.edu.ssafy.feed.model.entity.QTaggedFriends.taggedFriends;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FeedWithTagsDto> findOneWithTags(Long id) {
        List<FeedWithTagsDto> results = queryFactory
                .select(Projections.constructor(FeedWithTagsDto.class, feed, taggedFriends, feedLikes, comments))
                .from(feed)
                .leftJoin(taggedFriends).on(feed.id.eq(taggedFriends.feed.id))
                .leftJoin(feedLikes).on(feed.id.eq(feedLikes.feed.id))
                .leftJoin(comments).on(feed.id.eq(comments.feed.id))
                .where(feed.id.eq(id).and(feed.isDeleted.eq(false)))
                .fetch();

        if (results.isEmpty()) {
            return null;
        }

        return results;
    }

}
