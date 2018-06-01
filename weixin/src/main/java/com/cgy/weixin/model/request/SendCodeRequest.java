package com.cgy.weixin.model.request;

/**
 * Created by cgy
 * 2018/5/29  11:20
 */
public class SendCodeRequest {
    private String region;

    private String phone;

    public SendCodeRequest(String region, String phone) {
        this.region = region;
        this.phone = phone;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
