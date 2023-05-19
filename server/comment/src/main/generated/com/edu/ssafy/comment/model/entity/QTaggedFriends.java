package com.edu.ssafy.comment.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTaggedFriends is a Querydsl query type for TaggedFriends
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTaggedFriends extends EntityPathBase<TaggedFriends> {

    private static final long serialVersionUID = -261570991L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTaggedFriends taggedFriends = new QTaggedFriends("taggedFriends");

    public final QFeed feed;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nickname = createString("nickname");

    public final QUser user;

    public QTaggedFriends(String variable) {
        this(TaggedFriends.class, forVariable(variable), INITS);
    }

    public QTaggedFriends(Path<? extends TaggedFriends> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTaggedFriends(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTaggedFriends(PathMetadata metadata, PathInits inits) {
        this(TaggedFriends.class, metadata, inits);
    }

    public QTaggedFriends(Class<? extends TaggedFriends> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.feed = inits.isInitialized("feed") ? new QFeed(forProperty("feed"), inits.get("feed")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

