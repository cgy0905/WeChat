package com.cgy.weixin.model.request;

/**
 * Created by cgy
 * 2018/6/8  16:23
 */
public class AgreeFriendsRequest {

    private String friendId;
    public AgreeFriendsRequest(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }
}
