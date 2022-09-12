package com.coding.demo.model;

import java.io.Serializable;

/**
 * 类描述：用于存储用户的购买行为
 */
public class UserActiveDTO implements Serializable {

    private static final long serialVersionUID = -103797500202536441L;

    // 用户id
    private Long userId;

    // 商家类目的id
    private Long businessId;

    // 该用户对该商家类目的点击量
    private Long hits;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

}