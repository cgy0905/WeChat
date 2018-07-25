package com.cgy.weixin.model.request;

/**
 * Created by cgy
 * 2018/6/13  14:55
 */
public class JoinGroupRequest {

    private String groupId;

    public JoinGroupRequest(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
