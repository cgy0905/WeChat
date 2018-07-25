package com.cgy.weixin.api;

import com.cgy.weixin.model.response.AddGroupMemberResponse;
import com.cgy.weixin.model.response.AddToBlackListResponse;
import com.cgy.weixin.model.response.AgreeFriendsResponse;
import com.cgy.weixin.model.response.ChangePasswordResponse;
import com.cgy.weixin.model.response.CheckPhoneResponse;
import com.cgy.weixin.model.response.CreateGroupResponse;
import com.cgy.weixin.model.response.DeleteFriendResponse;
import com.cgy.weixin.model.response.DeleteGroupMemberResponse;
import com.cgy.weixin.model.response.FriendRelationshipResponse;
import com.cgy.weixin.model.response.GetBlackListResponse;
import com.cgy.weixin.model.response.GetFriendInfoByIDResponse;
import com.cgy.weixin.model.response.GetGroupInfoResponse;
import com.cgy.weixin.model.response.GetGroupMemberResponse;
import com.cgy.weixin.model.response.GetGroupResponse;
import com.cgy.weixin.model.response.GetTokenResponse;
import com.cgy.weixin.model.response.GetUserInfoByIdResponse;
import com.cgy.weixin.model.response.GetUserInfoByPhoneResponse;
import com.cgy.weixin.model.response.JoinGroupResponse;
import com.cgy.weixin.model.response.LoginResponse;
import com.cgy.weixin.model.response.QiNiuTokenResponse;
import com.cgy.weixin.model.response.QuitGroupResponse;
import com.cgy.weixin.model.response.RegisterResponse;
import com.cgy.weixin.model.response.RemoveFromBlackListResponse;
import com.cgy.weixin.model.response.ResetPasswordResponse;
import com.cgy.weixin.model.response.SendCodeResponse;
import com.cgy.weixin.model.response.SetFriendDisplayNameResponse;
import com.cgy.weixin.model.response.SetGroupDisplayNameResponse;
import com.cgy.weixin.model.response.SetGroupNameResponse;
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

    //根据userId去服务器查询好友信息
    @GET("friendship/{userId}profile")
    Observable<GetFriendInfoByIDResponse> getFriendInfoByID(@Path("userId") String userId);

    //设置自己的昵称
    @POST("user/set_nickname")
    Observable<SetNameResponse> setName(@Body RequestBody body);

    //设置用户头像
    @POST("user/set_portrait_uri")
    Observable<SetPortraitResponse> setPortrait(@Body RequestBody body);

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
    Observable<FriendRelationshipResponse> sendFriendInvitation(@Body RequestBody body);

    //同意对方好友邀请
    @POST("friendship/agree")
    Observable<AgreeFriendsResponse> agreeFriends(@Body RequestBody body);

    //删除好友
    @POST("friendship/set_display_name")
    Observable<DeleteFriendResponse> deleteFriend(@Body RequestBody body);

    //设置好友的备注名称
    @POST("friendship/set_display_name")
    Observable<SetFriendDisplayNameResponse> setFriendDisplayName(@Body RequestBody body);

    //获取黑名单
    @GET("user/blacklist")
    Observable<GetBlackListResponse> getBlackList();

    //加入黑名单
    @POST("user/add_to_blacklist")
    Observable<AddToBlackListResponse> addToBlackList(@Body RequestBody body);

    //移除黑名单
    @POST("user/remove_from_blacklist")
    Observable<RemoveFromBlackListResponse> removeFromBlackList(@Body RequestBody body);

    //创建群组
    @POST("group/create")
    Observable<CreateGroupResponse> createGroup(@Body RequestBody body);

    //获取当前用户所属群组列表
    @GET("user/groups")
    Observable<GetGroupResponse> getGroups();

    //根据 群组id 查询该群组信息 403群组成员才能看
    @GET("group/{groupId}")
    Observable<GetGroupInfoResponse> getGroupInfo(@Path("groupId") String groupId);

    //根据群id获取群组成员
    @POST("group/{groupId}/members")
    Observable<GetGroupMemberResponse> getGroupsMember(@Path("groupId") String groupId);

    //当前用户添加群组成员
    @POST("group/add")
    Observable<AddGroupMemberResponse> addGroupMember(@Body RequestBody body);

    //创建者将群组成员踢出群组
    @POST("group/kick")
    Observable<DeleteGroupMemberResponse> deleteGroupMember(@Body RequestBody body);

    //创建者更改群组昵称
    @POST("group/rename")
    Observable<SetGroupNameResponse> setGroupName(@Body RequestBody body);

    //用户自行退出群组
    @POST("group/quit")
    Observable<QuitGroupResponse> quitGroup(@Body RequestBody body);

    //创建者解散群组
    @POST("group/dismiss")
    Observable<QuitGroupResponse> dismissGroup(@Body RequestBody body);

    //修改自己在当前群的群昵称
    @POST("group/set_display_name")
    Observable<SetGroupDisplayNameResponse> setGroupDisplayName(@Body RequestBody body);

    //当前用户加入某群组
    @POST("group/join")
    Observable<JoinGroupResponse> joinGroup(@Body RequestBody body);

    //得到七牛的token
    @GET("user/get_image_token")
    Observable<QiNiuTokenResponse> getQiNiuToken();

    //下载图片
    @GET
    Observable<ResponseBody> downloadPic(@Url String mUrl);


}
