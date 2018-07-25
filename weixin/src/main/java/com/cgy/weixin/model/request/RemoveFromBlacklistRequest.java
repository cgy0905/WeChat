package com.cgy.weixin.model.request;

/**
 * Created by cgy
 * 2018/6/11  14:06
 */
public class RemoveFromBlacklistRequest {

    private String friendId;

    public RemoveFromBlacklistRequest(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }
}
