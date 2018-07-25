package com.cgy.weixin.utils;

import android.webkit.MimeTypeMap;

/**
 * Created by cgy
 * 2018/6/8  9:59
 */
class MimeTypeUtils {

    private MimeTypeUtils(){
    }

    public static String getMimeType(final String fileName) {
        String result = "application/octet-stream";
        int extPos = fileName.lastIndexOf(".");
        if (extPos != -1) {
            String ext = fileName.substring(extPos + 1);
            result = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }
        return result;
    }
}
