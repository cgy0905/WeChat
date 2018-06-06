package com.cgy.weixin.api;

import com.cgy.weixin.model.response.ChangePasswordResponse;
import com.cgy.weixin.model.response.CheckPhoneResponse;
import com.cgy.weixin.model.response.DeleteFriendResponse;
import com.cgy.weixin.model.response.FriendRelationshipResponse;
import com.cgy.weixin.model.response.GetGroupInfoResponse;
import com.cgy.weixin.model.response.GetGroupMemberResponse;
import com.cgy.weixin.model.response.GetGroupResponse;
import com.cgy.weixin.model.response.GetTokenResponse;
import com.cgy.weixin.model.response.GetUserInfoByIdResponse;
import com.cgy.weixin.model.response.GetUserInfoByPhoneResponse;
import com.cgy.weixin.model.response.LoginResponse;
import com.cgy.weixin.model.response.QiNiuTokenResponse;
import com.cgy.weixin.model.response.RegisterResponse;
import com.cgy.weixin.model.response.ResetPasswordResponse;
import com.cgy.weixin.model.response.SendCodeResponse;
import com.cgy.weixin.model.response.SetNameResponse;
import com.cgy.weixin.model.response.SetPortraitResponse;
import com.cgy.weixin.model.response.UserRelationshipResponse;
import com.cgy.weixin.model.response.VerifyCodeResponse;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;
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

    //设置自己的昵称
    @POST("user/set_nickname")
    Observable<SetNameResponse> setName(RequestBody body);

    //设置用户头像
    @POST("user/set_portrait_uri")
    Observable<SetPortraitResponse> setPortrait(RequestBody body);

    //当前登录用户通过旧密码设置新密码 前置条件需要登录才能访问
    @POST("user/change_password")
    Observable<ChangePasswordResponse> changePassword(RequestBody body);

    //通过手机验证码重置密码
    @POST("user/reset_password")
    Observable<ResetPasswordResponse> resetPassword(RequestBody body);

    //根据 id 去服务器查询用户信息
    @GET("user{userid}")
    Observable<GetUserInfoByIdResponse> getUserInfoById(@Path("userid") String userId);

    //通过国家码和手机号查询用户信息
    @GET("user/find/{region}/{phone}")
    Observable<GetUserInfoByPhoneResponse> getUserInfoFromPhone(@Path("region") String region, @Path("phone") String phone);

    //发送好友邀请
    @POST("friendship/invite")
    Observable<FriendRelationshipResponse> sendFriendInvitation(RequestBody body);

    //删除好友
    @POST("friendship/set_display_name")
    Observable<DeleteFriendResponse> deleteFriend(RequestBody body);

    //得到七牛的token
    @GET("user/get_image_token")
    Observable<QiNiuTokenResponse> getQiNiuToken();

    //下载图片
    @GET
    Observable<ResponseBody> downloadPic(@Url String mUrl);


}
