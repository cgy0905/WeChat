package com.cgy.weixin.model.request;

/**
 * Created by cgy
 * 2018/6/5  17:10
 */
public class DeleteFriendRequest {

    private String friendId;
    public DeleteFriendRequest(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }
}
