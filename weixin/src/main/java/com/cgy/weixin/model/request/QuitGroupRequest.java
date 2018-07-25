package com.cgy.weixin.model.request;

/**
 * Created by cgy
 * 2018/6/12  15:49
 */
public class QuitGroupRequest {

    private String groupId;
    public QuitGroupRequest(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
