package com.buddy.model.entity.enums;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.buddy.model.entity.FeedPictures;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFeedPictures is a Querydsl query type for FeedPictures
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFeedPictures extends EntityPathBase<FeedPictures> {

    private static final long serialVersionUID = -49270912L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFeedPictures feedPictures = new QFeedPictures("feedPictures");

    public final com.buddy.model.entity.QFeed feed;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imgUrl = createString("imgUrl");

    public QFeedPictures(String variable) {
        this(FeedPictures.class, forVariable(variable), INITS);
    }

    public QFeedPictures(Path<? extends FeedPictures> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFeedPictures(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFeedPictures(PathMetadata metadata, PathInits inits) {
        this(FeedPictures.class, metadata, inits);
    }

    public QFeedPictures(Class<? extends FeedPictures> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.feed = inits.isInitialized("feed") ? new com.buddy.model.entity.QFeed(forProperty("feed"), inits.get("feed")) : null;
    }

}

