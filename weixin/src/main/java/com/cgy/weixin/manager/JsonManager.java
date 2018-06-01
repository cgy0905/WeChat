package com.cgy.weixin.manager;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;

import java.util.List;

import retrofit2.HttpException;

/**
 * Created by cgy
 * 2018/5/24  17:29
 *
 * Json管理器（fastjson）
 */
public class JsonManager {
    static {
        TypeUtils.compatibleWithJavaBean = true;
    }

    private static final String tag = JsonManager.class.getSimpleName();


    /**
     * 将json字符串转换成java对象
     * @param json
     * @param cls
     * @param <T>
     * @return
     * @throws HttpException
     */
    public static <T>  T jsonToBean(String json, Class<T> cls) throws HttpException {
      return JSON.parseObject(json, cls);
    }

    /**
     * 将json字符串转换成 java List
     * @param json
     * @param cls
     * @param <T>
     * @return
     * @throws HttpException
     */
    public static <T>List<T> jsonToList(String json, Class<T> cls) throws HttpException{
        return JSON.parseArray(json, cls);
    }

    /**
     * 将bean对象转换成json字符串
     * @param obj
     * @return
     * @throws HttpException
     */
    public static String beanToJson(Object obj) throws HttpException {
        String result = JSON.toJSONString(obj);
        Log.e(tag, "beanToJson: " + result);
        return result;
    }
}
