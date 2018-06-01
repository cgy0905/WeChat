package com.cgy.weixin.model.redpacket;

/**
 * Created by cgy
 * 2018/5/24  14:16
 */
public class SignModel {

    public String partner;
    public String user_id;
    public String timestamp;
    public String sign;
    public String reg_hongbao_user;

    @Override
    public String toString() {
        return "SignModel{" +
                "partner='" + partner + '\'' +
                ", user_id='" + user_id + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", sign='" + sign + '\'' +
                ", reg_hongbao_user='" + reg_hongbao_user + '\'' +
                '}';
    }
}
