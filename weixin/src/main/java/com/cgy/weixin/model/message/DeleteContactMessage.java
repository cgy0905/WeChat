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
 * 2018/6/1  11:38
 */
@MessageTag(
        value = "cgy.DcMsg",
        flag = 3
)
public class DeleteContactMessage extends MessageContent{
    private String contact_id;
    public static final Creator<DeleteContactMessage> CREATOR = new Creator<DeleteContactMessage>() {
        @Override
        public DeleteContactMessage createFromParcel(Parcel source) {
            return new DeleteContactMessage(source);
        }

        @Override
        public DeleteContactMessage[] newArray(int size) {
            return new DeleteContactMessage[size];
        }
    };

    @Override
    public byte[] encode() {
        JSONObject jsonObject = new JSONObject();

        try {
            if (TextUtils.isEmpty(this.getContact_id())) {
                jsonObject.put("contact_id", this.contact_id);
            }

            if (this.getJSONUserInfo() != null) {
                jsonObject.put("bribery", this.getJSONUserInfo());
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

    protected DeleteContactMessage() {

    }

    public static DeleteContactMessage obtain(String contact_id) {
        DeleteContactMessage message = new DeleteContactMessage();
        message.setContact_id(contact_id);
        return message;
    }
    public DeleteContactMessage(byte[] bytes) {
        String str = null;

        try {
            str = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            JSONObject object = new JSONObject(str);

            if (object.has("contact_id")) {
                this.setContact_id(object.optString("contact_id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int describeContents() {
        return 0;
    }


    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public String getContact_id() {

        return contact_id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.contact_id);
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
    }

    public  DeleteContactMessage(Parcel parcel) {
        this.setContact_id(ParcelUtils.readFromParcel(parcel));
        this.setUserInfo(ParcelUtils.readFromParcel(parcel, UserInfo.class));

    }
}
