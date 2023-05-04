package com.edu.ssafy.user.model.repository.custom;

import com.edu.ssafy.user.model.dto.BriefFeedIngoDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.edu.ssafy.user.model.entity.QFeed.feed;


@Repository
@RequiredArgsConstructor
public class FeedRepositoryImpl implements FeedRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BriefFeedIngoDto> findAllBriefInfoByUserId(Long userId) {
        return queryFactory
                .select(Projections.constructor(BriefFeedIngoDto.class, feed.id, feed.mainImg))
                .from(feed)
                .where(feed.user.id.eq(userId).and(feed.isDeleted.eq(false)))
                .fetch();
    }
}
