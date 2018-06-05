package com.cgy.weixin.model.request;

/**
 * Created by cgy
 * 2018/6/4  10:03
 */
public class SetNameRequest {
    private String nickname;

    public SetNameRequest(String nickname) {
        this.nickname = nickname;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
