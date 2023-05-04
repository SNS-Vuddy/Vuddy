package com.edu.ssafy.user.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1528335603L;

    public static final QUser user = new QUser("user");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isWithdrawal = createBoolean("isWithdrawal");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath profileImage = createString("profileImage");

    public final StringPath statusMessage = createString("statusMessage");

    public final EnumPath<com.edu.ssafy.user.model.entity.enums.UserRoll> userRoll = createEnum("userRoll", com.edu.ssafy.user.model.entity.enums.UserRoll.class);

    public final DateTimePath<java.time.LocalDateTime> withdrawalAt = createDateTime("withdrawalAt", java.time.LocalDateTime.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

