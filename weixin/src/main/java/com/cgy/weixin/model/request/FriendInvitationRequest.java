package com.cgy.weixin.model.request;

/**
 * Created by cgy
 * 2018/6/5  18:07
 */
public class FriendInvitationRequest {

    private String friendId;
    private String message;

    public FriendInvitationRequest(String userId, String addFriendMessage) {
        this.message = addFriendMessage;
        this.friendId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
