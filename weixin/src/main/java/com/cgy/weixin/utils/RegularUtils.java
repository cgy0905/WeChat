package com.cgy.weixin.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cgy
 * 2018/5/29  10:39
 * 正则工具类
 */
public class RegularUtils {

    public static boolean isMobile(String phoneNumber) {
        String MOBILE_PHONE_PATTERN = "^((13[0-9])|(15[0-9])|(18)[0-9])|(14[7]|(17[0|6|7|8]))\\d{8}$";
        Pattern p = Pattern.compile(MOBILE_PHONE_PATTERN);
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
