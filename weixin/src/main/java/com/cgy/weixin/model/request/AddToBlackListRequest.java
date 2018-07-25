package com.cgy.weixin.model.request;

/**
 * Created by cgy
 * 2018/6/11  14:03
 */
public class AddToBlackListRequest {
    private String friendId;

    public AddToBlackListRequest(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }
}
