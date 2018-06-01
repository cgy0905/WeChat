package com.cgy.weixin.api.redpacket;

import com.cgy.weixin.model.redpacket.SignModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by cgy
 * 2018/5/24  14:09
 *
 * 云账户获取demo签名接口
 */
public interface SignService {

    @GET("api/sign/")
    Call<SignModel> getSignInfo(@Query("uid") String userId, @Query("token") String token);
}
