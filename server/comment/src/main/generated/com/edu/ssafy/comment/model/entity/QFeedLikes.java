package com.edu.ssafy.comment.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFeedLikes is a Querydsl query type for FeedLikes
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFeedLikes extends EntityPathBase<FeedLikes> {

    private static final long serialVersionUID = -2093161338L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFeedLikes feedLikes = new QFeedLikes("feedLikes");

    public final QFeed feed;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QUser user;

    public QFeedLikes(String variable) {
        this(FeedLikes.class, forVariable(variable), INITS);
    }

    public QFeedLikes(Path<? extends FeedLikes> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFeedLikes(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFeedLikes(PathMetadata metadata, PathInits inits) {
        this(FeedLikes.class, metadata, inits);
    }

    public QFeedLikes(Class<? extends FeedLikes> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.feed = inits.isInitialized("feed") ? new QFeed(forProperty("feed"), inits.get("feed")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

