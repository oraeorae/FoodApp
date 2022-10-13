package com.coding.demo.service;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;

public interface FindSeller {
    void online(String name, double longitude, double latitude);


    Distance geoDist(String member1, String member2);

    GeoResults<RedisGeoCommands.GeoLocation<String>> radiusByMember(String member);

    GeoResults<RedisGeoCommands.GeoLocation<String>> geoRadius(double longitude, double latitude);
}
