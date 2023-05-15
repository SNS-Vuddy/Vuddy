package com.buddy3.buddy3.distance;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GPS {

    private final double EARTH_RADIUS = 6378137;

    public List<String> convertToDMS(String latitude, String longitude) {
        List<String> result = new ArrayList<>();
        String[] latitudeArr = latitude.split("\\.");
        String[] longitudeArr = longitude.split("\\.");
        String latitudeDegree = latitudeArr[0];
        String longitudeDegree = longitudeArr[0];
        latitudeArr = String.valueOf(Double.parseDouble("0." + latitudeArr[1]) * 60).split("\\.");
        longitudeArr = String.valueOf(Double.parseDouble("0." + longitudeArr[1]) * 60).split("\\.");
        String latitudeMinute = latitudeArr[0];
        String longitudeMinute = longitudeArr[0];
        String latitudeSecondStr = String.valueOf(Double.parseDouble("0." + latitudeArr[1]) * 60);
        String longitudeSecondStr = String.valueOf(Double.parseDouble("0." + longitudeArr[1]) * 60);
        String latitudeSecond = null;
        String longitudeSecond = null;
        if (latitudeSecondStr.length() < 5) {
            latitudeSecond = latitudeSecondStr;
        }
        else {
            latitudeSecond = latitudeSecondStr.substring(0,5);
        }
        if (longitudeSecondStr.length() < 5) {
            longitudeSecond = longitudeSecondStr;
        }
        else {
            longitudeSecond = longitudeSecondStr.substring(0,5);
        }
        result.add(latitudeDegree + "," + latitudeMinute + "," + latitudeSecond);
        result.add(longitudeDegree + "," + longitudeMinute + "," + longitudeSecond);
        return result;
    }

    public List<String> convertToDecimal(String latitude, String longitude) {
        List<String> result = new ArrayList<>();
        String[] latitudeArr = latitude.split(",");
        String[] longitudeArr = longitude.split(",");
        Double[] latitudeArrD = new Double[3];
        Double[] longitudeArrD = new Double[3];
        for (int i = 0; i < 3; i++) {
            latitudeArrD[i] = Double.parseDouble(latitudeArr[i]);
            longitudeArrD[i] = Double.parseDouble(longitudeArr[i]);
        }
        result.add(String.valueOf(latitudeArrD[0] + (latitudeArrD[1] + latitudeArrD[2] / 60) / 60));
        result.add(String.valueOf(longitudeArrD[0] + (longitudeArrD[1] + longitudeArrD[2] / 60) / 60));
        return result;
    }

    public Double getDistance(String lat1, String lon1, String lat2, String lon2) {
        Double latitude1 = Double.parseDouble(lat1);
        Double longitude1 = Double.parseDouble(lon1);
        Double latitude2 = Double.parseDouble(lat2);
        Double longitude2 = Double.parseDouble(lon2);

        double dLat = Math.toRadians(latitude2 - latitude1);
        double dLon = Math.toRadians(longitude2 - longitude1);

        System.out.println(lat1 + "   " + lon1);
        System.out.println(lat2 + "   " + lon2);
        System.out.println();

        double a = Math.sin(dLat/2)* Math.sin(dLat/2)+ Math.cos(Math.toRadians(latitude1))* Math.cos(Math.toRadians(latitude2))* Math.sin(dLon/2)* Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = EARTH_RADIUS * c;    // Distance in m
        return d;

    }

}
