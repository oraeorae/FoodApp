package com.coding.demo.service;

import com.coding.demo.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;

public class FindSellerImpl implements FindSeller {
    @Autowired
    private RedisUtils redisUtils;
    @Override
    public void online(String name, double longitude, double latitude) {
        redisUtils.online(name, longitude, latitude);
    }

    @Override
    public Distance geoDist(String member1, String member2) {
        return redisUtils.geoDist(member1, member2);
    }

    @Override
    public GeoResults<RedisGeoCommands.GeoLocation<String>> radiusByMember(String member) {
        return redisUtils.radiusByMember(member);
    }

    @Override
    public GeoResults<RedisGeoCommands.GeoLocation<String>> geoRadius(double longitude, double latitude) {
        return redisUtils.geoRadius(longitude, latitude);
    }
}
