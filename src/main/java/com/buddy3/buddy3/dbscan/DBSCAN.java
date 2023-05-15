package com.buddy3.buddy3.dbscan;

import com.buddy3.buddy3.distance.GPS;
import com.buddy3.buddy3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Async
public class DBSCAN {

    @Autowired
    private RedisTemplate<String, String> redisLocationTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GPS gps;

    private final Double maxDist = 10D;
    private final int minCount = 10;

//    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
//    public void statusUpdate() {
//        List<String> allUsersList = userRepository.findAllUsers();
//    }


    @Scheduled(cron="0 0 * * * *", zone = "Asia/Seoul")
    public void startDBScan() {
//        System.out.println("시작!");
        List<String> allUsersList = userRepository.findAllUsers();
        for (String userNickname : allUsersList) {
            List<String> locationList = redisLocationTemplate.opsForList().range(userNickname, 0, -1);
            if (locationList.size() == 0) {
                continue;
            }
            int[] clusterGroup = new int[locationList.size()];
            int groupNow = 0;
            for (int i = 0; i < locationList.size(); i++) {
                if (clusterGroup[i] == 0) {
                    List<Integer> neighbors = countNeighbor(locationList, i);
//                    System.out.println("neighbors : " + neighbors.toString());
                    if (neighbors.size() < minCount) {
                        clusterGroup[i] = -1;
                    }
                    else {
                        groupNow++;
                        clusterGroup[i] = groupNow;
                        List<Integer> scanList = new ArrayList<>();
                        scanList.addAll(neighbors);
                        for (int k = 0; k < scanList.size(); k++) {
                            int num = scanList.get(k);
                            if (clusterGroup[num] == -1) {
                                clusterGroup[num] = groupNow;
                            }
                            if (clusterGroup[num] != 0) {
                                continue;
                            }
                            clusterGroup[num] = groupNow;
                            neighbors = countNeighbor(locationList, num);
                            if (neighbors.size() >= minCount)
                                scanList.addAll(neighbors);
                        }
                    }
                }
            }
//            System.out.println(locationList.get(0));
            clusteringLocation(locationList, clusterGroup, groupNow, userNickname);
        }
    }

    public void clusteringLocation(List<String> locationList, int[] clusterGroup, int groupCount, String nickname) {
//        System.out.println("groupCount : " + groupCount);
        int[][] hoursArr = new int[3][groupCount + 1];
        double[][] sumArr = new double[groupCount + 1][2];
        int[] countArr = new int[groupCount + 1];

        for (int j = 0; j < locationList.size(); j++) {
            if (clusterGroup[j] == -1) {
//                System.out.println(locationList.size());
//                System.out.println(clusterGroup[j]);
                continue;
            }
//            System.out.println(clusterGroup[j]);
            String[] locationArr = locationList.get(j).split(" ");
//            System.out.println(locationArr[0]);
//            System.out.println(sumArr[clusterGroup[j]][0]);
//            System.out.println("clusterGroup - " + clusterGroup[j] + " : " + locationArr[0] + " " + locationArr[1]);
            sumArr[clusterGroup[j]][0] += Double.parseDouble(locationArr[0]);
            sumArr[clusterGroup[j]][1] += Double.parseDouble(locationArr[1]);
            countArr[clusterGroup[j]] += 1;
            int hours = Integer.parseInt(locationArr[3].split(":")[0]);
            if (hours > 18 || hours <= 8) {
                hoursArr[1][clusterGroup[j]]++;
//                System.out.println(hoursArr[1][clusterGroup[j]]);
            }
            else if (hours > 8 && hours <= 18) {
                hoursArr[2][clusterGroup[j]]++;
//                System.out.println(hoursArr[2][clusterGroup[j]]);
            }
            else {
                hoursArr[0][clusterGroup[j]]++;
//                System.out.println(hoursArr[0][clusterGroup[j]]);
            }
        }



        for (int p = 0; p < hoursArr.length; p++) {
            int maxGroup = 0;
            for (int l = 1; l < hoursArr[p].length; l++) {
                if (hoursArr[p][l] > hoursArr[p][maxGroup]) {
                    maxGroup = l;
                }
            }
            if (maxGroup != 0) {
                if (p == 1) {
//                    System.out.println(nickname);
                    String latitude = String.valueOf(sumArr[maxGroup][0] / (double) countArr[maxGroup]);
                    String longitude = String.valueOf(sumArr[maxGroup][1] / (double) countArr[maxGroup]);
                    redisLocationTemplate.opsForHash().put(nickname + "-location", "home", latitude + " " + longitude);
//                    System.out.println("home : " + redisLocationTemplate.opsForHash().get(nickname + "-location", "home"));
                }
                else if (p == 2) {
                    String latitude = String.valueOf(sumArr[maxGroup][0] / (double) countArr[maxGroup]);
                    String longitude = String.valueOf(sumArr[maxGroup][1] / (double) countArr[maxGroup]);
                    redisLocationTemplate.opsForHash().put(nickname + "-location", "office", latitude + " " + longitude);
//                    System.out.println("workplace : " + redisLocationTemplate.opsForHash().get(nickname + "-location", "workplace"));
                }
            }
        }
    }

    public List<Integer> countNeighbor(List<String> locationList, int idx) {
        String[] locationArrIdx = locationList.get(idx).split(" ");
        List<Integer> neighbors = new ArrayList<>();
        for (int i = 0; i < locationList.size(); i++) {
            if (idx != i) {
                String[] locationArr = locationList.get(i).split(" ");
                Double dataDistance = gps.getDistance(locationArr[0], locationArr[1], locationArrIdx[0], locationArrIdx[1]);
//                System.out.println("distance : " + dataDistance);
                if (dataDistance < maxDist) {
                    neighbors.add(i);
                }
            }
        }
        return neighbors;
    }

}