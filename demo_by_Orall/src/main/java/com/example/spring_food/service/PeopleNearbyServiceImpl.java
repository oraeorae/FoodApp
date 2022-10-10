package com.example.spring_food.service;

import com.example.spring_food.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author czh
 * 参考链接：https://blog.csdn.net/hollycloud/article/details/119951655
 */
@Service
public class PeopleNearbyServiceImpl implements PeopleNearbyService {
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void geoAdd(String name, double longitude, double latitude) {
        redisUtils.geoAdd(name,longitude,latitude);
    }

    @Override
    public List<Point> geoGet(String name) {
        return redisUtils.geoGet(name);
    }

    @Override
    public Distance geoDist(String member1, String member2) {
        return redisUtils.geoDist(member1,member2);
    }

    @Override
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> geoMember(String member) {
        return redisUtils.geoMember(member);
    }

    @Override
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> geoRadius(double longitude, double latitude) {
        return redisUtils.geoRadius(longitude,latitude);
    }
}
