package com.buddy3.buddy3.service;

import com.buddy3.buddy3.model.UserLocation;
import com.buddy3.buddy3.repository.UserLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoUserLocationService {

    @Autowired
    private UserLocationRepository userLocationRepository;

    public List<UserLocation> getAllUserLocation() {
        return userLocationRepository.findAll();
    }

    public void saveAllUserLocation(List<UserLocation> userLocationList) {
        userLocationRepository.saveAll(userLocationList);
    }
}
