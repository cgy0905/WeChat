package com.cgy.weixin.api;

import com.cgy.weixin.model.response.CheckPhoneResponse;
import com.cgy.weixin.model.response.GetGroupInfoResponse;
import com.cgy.weixin.model.response.GetGroupMemberResponse;
import com.cgy.weixin.model.response.GetGroupResponse;
import com.cgy.weixin.model.response.GetTokenResponse;
import com.cgy.weixin.model.response.LoginResponse;
import com.cgy.weixin.model.response.RegisterResponse;
import com.cgy.weixin.model.response.SendCodeResponse;
import com.cgy.weixin.model.response.UserRelationshipResponse;
import com.cgy.weixin.model.response.VerifyCodeResponse;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by cgy
 * 2018/5/25  10:53
 */
public interface MyApi {
    public static final String BASE_URL = "http://api.sealtalk.im/";

    //检查手机是否被注册
    @POST("user/check_phone_available")
    Observable<CheckPhoneResponse> checkPhoneAvailable(@Body RequestBody body);

    //发送验证码
    @POST("user/send_code")
    Observable<SendCodeResponse> sendCode(@Body RequestBody body);

    //验证验证码是否正确(必须先用手机号码调sendCode)
    @POST("user/verify_code")
    Observable<VerifyCodeResponse> verifyCode(RequestBody body);

    //注册
    @POST("user/register")
    Observable<RegisterResponse> register(@Body RequestBody body);

    //登录
    @POST("user/login")
    Observable<LoginResponse> login(@Body RequestBody body);

    //获取token前置条件需要登录 502坏的网关 测试环境用户已达上限
    @GET("user/get_token")
    Observable<GetTokenResponse> getToken();

    //获取发生过用户关系的列表
    @GET("friendship/all")
    Observable<UserRelationshipResponse> getAllUserRelationship();

    //获取当前用户所属群组列表
    @GET("user/groups")
    Observable<GetGroupResponse> getGroups();

    //根据 群组id 查询该群组信息 403群组成员才能看
    @GET("group/{groupId}")
    Observable<GetGroupInfoResponse> getGroupInfo(@Path("groupId") String groupId);

    //根据群id获取群组成员
    @POST("group/{groupId}/members")
    Observable<GetGroupMemberResponse> getGroupsMember(@Path("groupId") String groupId);


}
