package com.example.spring_food.service;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.domain.geo.Metrics;

import java.util.List;

public interface PeopleNearbyService {

    /**
     * 添加指定用户的经纬度
     * @param name
     * @param longitude
     * @param latitude
     * @return
     */
    public void geoAdd(String name, double longitude, double latitude);

    /**
     * 得到指定用户的经纬度
     *
     * @param name
     * @param name
     * @return
     */
    public List<Point> geoGet(String name);

    /**
     * 获取两成员的距离
     *
     * @param member1
     * @param member2
     * @return
     */
    public Distance geoDist(String member1, String member2);


    /**
     * 获取成员附近的人
     *
     * @param member
     * @return
     */
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> geoMember(String member);

    /**
     * 获取指定经纬度附近的人
     *
     * @param longitude
     * @param latitude
     * @return
     */
    public GeoResults<RedisGeoCommands.GeoLocation<Object>> geoRadius(double longitude, double latitude);

}