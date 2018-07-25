package com.cgy.weixin.model.request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cgy
 * 2018/6/12  15:17
 */
public class DeleteGroupMemberRequest {
    private String groupId;

    private List<String> memberIds;

    public DeleteGroupMemberRequest(String groupId, ArrayList<String> memberIds) {
        this.groupId = groupId;
        this.memberIds = memberIds;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }
}
