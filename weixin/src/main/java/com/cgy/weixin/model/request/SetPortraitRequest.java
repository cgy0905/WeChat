package com.cgy.weixin.model.request;

/**
 * Created by cgy
 * 2018/6/4  11:13
 */
public class SetPortraitRequest {

    private String portraitUri;


    public SetPortraitRequest(String portraitUri) {
        this.portraitUri = portraitUri;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }

}
