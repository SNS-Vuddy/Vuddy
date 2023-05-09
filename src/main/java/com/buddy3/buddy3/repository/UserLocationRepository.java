package com.buddy3.buddy3.repository;

import com.buddy3.buddy3.model.UserLocation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserLocationRepository extends MongoRepository<UserLocation, String> {
    List<UserLocation> findAll();
}
