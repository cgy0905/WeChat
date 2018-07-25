package com.cgy.weixin.model.request;

import java.util.List;

/**
 * Created by cgy
 * 2018/6/11  17:39
 */
public class CreateGroupRequest {

    private String name;

    private List<String> memberIds;

    public CreateGroupRequest(String name, List<String> memberIds) {
        this.name = name;
        this.memberIds = memberIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }
}
