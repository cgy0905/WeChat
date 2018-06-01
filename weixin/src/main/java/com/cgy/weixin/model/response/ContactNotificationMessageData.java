package com.cgy.weixin.model.response;

/**
 * Created by cgy
 * 2018/6/1  11:33
 */
public class ContactNotificationMessageData {
    /**
     * sourceUserNickname : 赵哈哈
     * version : 1456282826213
     */

    private String sourceUserNickname;
    private long version;

    public void setSourceUserNickname(String sourceUserNickname) {
        this.sourceUserNickname = sourceUserNickname;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getSourceUserNickname() {
        return sourceUserNickname;
    }

    public long getVersion() {
        return version;
    }
}
