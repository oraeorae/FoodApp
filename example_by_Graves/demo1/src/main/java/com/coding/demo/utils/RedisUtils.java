package com.coding.demo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisUtils {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 传入对应的经纬度
     * @param name
     * @param longitude
     * @param latitude
     */
    public void online(String name, double longitude, double latitude) {
        redisTemplate.opsForGeo().add("geo", new Point(longitude, latitude), name);

    }

    /**
     * 获取两个人直接距离
     * @param member1
     * @param member2
     * @return
     */
    public Distance geoDist(String member1, String member2) {
        //获取两个成员间距离，单位km
        return this.redisTemplate.opsForGeo().distance("geo", member1, member2, RedisGeoCommands.DistanceUnit.KILOMETERS);
    }


    /**
     * 返回范围内的名称
     * @param member
     * @return
     */
    public GeoResults<RedisGeoCommands.GeoLocation<String>> radiusByMember(String member) {
        //返回结果按距离升序，包含距离和经纬度, 返回50条
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(50);
        //半径10公里内
        Distance distance = new Distance(10, Metrics.KILOMETERS);
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = this.redisTemplate.opsForGeo().radius("geo", member, distance, args);
        return geoResults;
    }

    public GeoResults<RedisGeoCommands.GeoLocation<String>> geoRadius(double longitude, double latitude) {
        //半径10公里内
        Distance distance = new Distance(10, Metrics.KILOMETERS);
        //根据经纬度生成范围
        Circle circle = new Circle(new Point(longitude, latitude), distance);
        //返回结果按距离升序，包含距离和经纬度, 返回50条
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(50);
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = this.redisTemplate.opsForGeo().radius("geo", circle, args);
        return  geoResults;
    }

}
