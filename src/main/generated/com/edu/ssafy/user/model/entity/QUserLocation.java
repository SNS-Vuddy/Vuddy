package com.edu.ssafy.user.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserLocation is a Querydsl query type for UserLocation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserLocation extends EntityPathBase<UserLocation> {

    private static final long serialVersionUID = -2006443550L;

    public static final QUserLocation userLocation = new QUserLocation("userLocation");

    public final StringPath homeAddress = createString("homeAddress");

    public final StringPath officeAddress = createString("officeAddress");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUserLocation(String variable) {
        super(UserLocation.class, forVariable(variable));
    }

    public QUserLocation(Path<? extends UserLocation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserLocation(PathMetadata metadata) {
        super(UserLocation.class, metadata);
    }

}

