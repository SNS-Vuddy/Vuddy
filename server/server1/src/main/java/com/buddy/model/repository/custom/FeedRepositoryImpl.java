package com.buddy.model.repository.custom;

import com.buddy.model.dto.FeedWithTagsDto;
import com.buddy.model.dto.FeedWithTagsListDto;
import com.buddy.model.dto.response.BriefFeedIngoDto;
import com.buddy.model.entity.Feed;
import com.buddy.model.entity.QFeed;
import com.buddy.model.entity.QTaggedFriends;
import com.buddy.model.entity.TaggedFriends;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.buddy.model.entity.QFeed.feed;
import static com.buddy.model.entity.QTaggedFriends.taggedFriends;
import static com.buddy.model.entity.QFeedLikes.feedLikes;
import static com.buddy.model.entity.QComments.comments;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom {

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

    @Override
    public Optional<FeedWithTagsListDto> findFeedWithTagsListById(Long id) {
        List<Tuple> results = queryFactory
                .select(QFeed.feed, QTaggedFriends.taggedFriends)
                .from(QFeed.feed)
                .leftJoin(QTaggedFriends.taggedFriends).on(QFeed.feed.id.eq(QTaggedFriends.taggedFriends.feed.id))
                .where(QFeed.feed.id.eq(id))
                .fetch();

        if (results.isEmpty()) {
            return Optional.empty();
        }

        Feed feed = results.get(0).get(QFeed.feed);
        List<TaggedFriends> taggedFriends = results.stream()
                .map(tuple -> tuple.get(QTaggedFriends.taggedFriends))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        FeedWithTagsListDto feedWithTagsListDto = new FeedWithTagsListDto(feed, taggedFriends);

        return Optional.of(feedWithTagsListDto);
    }

    @Override
    public List<BriefFeedIngoDto> findAllBriefInfoByUserId(Long userId) {
        return queryFactory
                .select(Projections.constructor(BriefFeedIngoDto.class, feed.id, feed.mainImg))
                .from(feed)
                .where(feed.user.id.eq(userId).and(feed.isDeleted.eq(false)))
                .fetch();

    }




}
