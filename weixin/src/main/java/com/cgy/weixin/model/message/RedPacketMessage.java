package com.cgy.weixin.model.message;

import android.os.Parcel;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

/**
 * Created by cgy
 * 2018/6/1  15:45
 */
@MessageTag(
        value = "cgy:RpMsg",
        flag = 3
)
public class RedPacketMessage extends MessageContent{
    private String content;
    private String Bribery_ID;
    private String Bribery_Name;
    private String Bribery_Message;
    public static final Creator<RedPacketMessage> CREATOR = new Creator<RedPacketMessage>() {
        @Override
        public RedPacketMessage createFromParcel(Parcel source) {
            return new RedPacketMessage();
        }

        @Override
        public RedPacketMessage[] newArray(int size) {
            return new RedPacketMessage[size];
        }
    };

    @Override
    public byte[] encode() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (!TextUtils.isEmpty(this.getContent())) {
                jsonObject.put("content", this.content);
            }
            if (!TextUtils.isEmpty(this.getBribery_ID())) {
                jsonObject.put("Bribery_ID", this.Bribery_ID);
            }

            if (!TextUtils.isEmpty(this.getBribery_Name())) {
                jsonObject.put("Bribery_Name", this.Bribery_Name);
            }

            if (!TextUtils.isEmpty(this.getBribery_Message())) {
                jsonObject.put("Bribery_Message", this.Bribery_Message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return jsonObject.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

    }

    private String getEmotion(String str) {
        Pattern pattern = Pattern.compile("\\[/u([0-9A-Fa-f]+)\\]");
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();

        while ( matcher.find()) {
            int c = Integer.parseInt(matcher.group(1), 16);
            matcher.appendReplacement(sb, String.valueOf(Character.toChars(c)));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    protected RedPacketMessage() {

    }

    public static RedPacketMessage obtain(String bribery_ID, String bribery_Name, String bribery_Message, String content) {
        RedPacketMessage message = new RedPacketMessage();
        message.setBribery_ID(bribery_ID);
        message.setBribery_Name(bribery_Name);
        message.setBribery_Message(bribery_Message);
        message.setContent(content);
        return message;
    }

    public RedPacketMessage(byte[] bytes) {
        String str = null;
        try {
            str = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            JSONObject object = new JSONObject(str);

            if (object.has("content")) {
                this.setContent(object.optString("content"));
            }
            if (object.has("Bribery_ID")) {
                this.setBribery_ID(object.optString("Bribery_ID"));
            }

            if (object.has("Bribery_Name")) {
                this.setBribery_Name(object.optString("Bribery_Name"));
            }

            if (object.has("Bribery_Message")) {
                this.setBribery_Message(object.optString("Bribery_Message"));
            }

            if (object.has("bribery")) {
                this.setUserInfo(this.parseJsonToUserInfo(object.getJSONObject("bribery")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.Bribery_ID);
        ParcelUtils.writeToParcel(dest, this.Bribery_Name);
        ParcelUtils.writeToParcel(dest, this.Bribery_Message);
        ParcelUtils.writeToParcel(dest, this.content);
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
    }

    public RedPacketMessage(Parcel parcel) {
        this.setBribery_ID(ParcelUtils.readFromParcel(parcel));
        this.setBribery_Name(ParcelUtils.readFromParcel(parcel));
        this.setBribery_Message(ParcelUtils.readFromParcel(parcel));
        this.setContent(ParcelUtils.readFromParcel(parcel));
        this.setUserInfo((UserInfo) ParcelUtils.readFromParcel(parcel, UserInfo.class));
    }


    public String getBribery_ID() {
        return this.Bribery_ID;
    }

    public void setBribery_ID(String bribery_id) {
        this.Bribery_ID = bribery_id;
    }

    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getBribery_Name() {
        return this.Bribery_Name;
    }
    public void setBribery_Name(String bribery_name) {
        this.Bribery_Name = bribery_name;
    }

    public String getBribery_Message() {
        return this.Bribery_Message;
    }
    public void setBribery_Message(String bribery_message) {
        this.Bribery_Message = bribery_message;
    }






}
