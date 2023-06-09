package com.edu.ssafy.feed.model.repository.custom;

import com.edu.ssafy.feed.model.dto.AllFeedInfoDto;
import com.edu.ssafy.feed.model.dto.FeedWithTagsListDto;
import com.edu.ssafy.feed.model.dto.response.FriendsFeedsRes;
import com.edu.ssafy.feed.model.entity.Feed;
import com.edu.ssafy.feed.model.entity.QFeed;
import com.edu.ssafy.feed.model.entity.QTaggedFriends;
import com.edu.ssafy.feed.model.entity.TaggedFriends;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.edu.ssafy.feed.model.entity.QFeed.feed;
import static com.edu.ssafy.feed.model.entity.QFeedLikes.feedLikes;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AllFeedInfoDto> findOneWithTags(Long id) {
        List<AllFeedInfoDto> results = queryFactory
                .select(Projections.constructor(AllFeedInfoDto.class, feed, feedLikes))
                .from(feed)
                .leftJoin(feedLikes).on(feed.id.eq(feedLikes.feed.id))
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
    public List<FriendsFeedsRes> findAllByUserIdIn(List<Long> friends) {
        List<FriendsFeedsRes> results = queryFactory
                .select(Projections.constructor(FriendsFeedsRes.class, feed.id, feed.mainImg, feed.location))
                .from(feed)
                .where(feed.user.id.in(friends).and(feed.isDeleted.eq(false)))
                .fetch();

        if (results.isEmpty()) {
            return new ArrayList<>();
        }

        return results;
    }
}
