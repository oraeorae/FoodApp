package com.example.spring_food.test;

import com.example.spring_food.service.PeopleNearbyService;
import com.example.spring_food.service.RecommendService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GeoTest {
    @Autowired
    private PeopleNearbyService peopleNearbyService;

    @Test
    public void testgeoAdd(String name, double longitude, double latitude){
        peopleNearbyService.geoAdd(name,longitude,latitude);
    }

    /**
     * 得到指定用户的经纬度
     */
    @Test
    public List<Point> testgeoGet(String name){
        return peopleNearbyService.geoGet(name);
    }

    /**
     * 获取两成员的距离
     */
    @Test
    public Distance testgeoDist(String member1, String member2){
        return peopleNearbyService.geoDist(member1,member2);
    }

    /**
     * 获取成员附近的人
     */
    @Test
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> testgeoMember(String member){
        return peopleNearbyService.geoMember(member);
    }

    /**
     * 获取指定经纬度附近的人
     */
    @Test
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> testgeoRadius(double longitude, double latitude){
        return peopleNearbyService.geoRadius(longitude,latitude);
    }

}
