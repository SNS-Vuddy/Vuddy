package com.edu.ssafy.user.model.repository;

import com.edu.ssafy.user.model.entity.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {

}
