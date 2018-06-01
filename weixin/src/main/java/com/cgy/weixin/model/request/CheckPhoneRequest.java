package com.cgy.weixin.model.request;

/**
 * Created by cgy
 * 2018/5/29  11:00
 */
public class CheckPhoneRequest {
    private String phone;

    private String region;

    public CheckPhoneRequest(String phone, String region) {
        this.phone = phone;
        this.region = region;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
