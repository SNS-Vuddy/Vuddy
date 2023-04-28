package com.buddy.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserFriends is a Querydsl query type for UserFriends
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserFriends extends EntityPathBase<UserFriends> {

    private static final long serialVersionUID = -1060178759L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserFriends userFriends = new QUserFriends("userFriends");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QUser receiveUser;

    public final QUser requstUser;

    public final StringPath status = createString("status");

    public QUserFriends(String variable) {
        this(UserFriends.class, forVariable(variable), INITS);
    }

    public QUserFriends(Path<? extends UserFriends> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserFriends(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserFriends(PathMetadata metadata, PathInits inits) {
        this(UserFriends.class, metadata, inits);
    }

    public QUserFriends(Class<? extends UserFriends> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.receiveUser = inits.isInitialized("receiveUser") ? new QUser(forProperty("receiveUser")) : null;
        this.requstUser = inits.isInitialized("requstUser") ? new QUser(forProperty("requstUser")) : null;
    }

}

