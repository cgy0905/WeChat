package com.cgy.weixin.api;

import com.cgy.weixin.api.base.BaseApiRetrofit;
import com.cgy.weixin.model.request.ChangePasswordRequest;
import com.cgy.weixin.model.request.CheckPhoneRequest;
import com.cgy.weixin.model.request.LoginRequest;
import com.cgy.weixin.model.request.RegisterRequest;
import com.cgy.weixin.model.request.ResetPasswordRequest;
import com.cgy.weixin.model.request.SendCodeRequest;
import com.cgy.weixin.model.request.SetNameRequest;
import com.cgy.weixin.model.request.SetPortraitRequest;
import com.cgy.weixin.model.request.VerifyCodeRequest;
import com.cgy.weixin.model.response.ChangePasswordResponse;
import com.cgy.weixin.model.response.CheckPhoneResponse;
import com.cgy.weixin.model.response.GetGroupInfoResponse;
import com.cgy.weixin.model.response.GetGroupMemberResponse;
import com.cgy.weixin.model.response.GetGroupResponse;
import com.cgy.weixin.model.response.GetTokenResponse;
import com.cgy.weixin.model.response.GetUserInfoByIdResponse;
import com.cgy.weixin.model.response.LoginResponse;
import com.cgy.weixin.model.response.QiNiuTokenResponse;
import com.cgy.weixin.model.response.RegisterResponse;
import com.cgy.weixin.model.response.ResetPasswordResponse;
import com.cgy.weixin.model.response.SendCodeResponse;
import com.cgy.weixin.model.response.SetNameResponse;
import com.cgy.weixin.model.response.SetPortraitResponse;
import com.cgy.weixin.model.response.UserRelationshipResponse;
import com.cgy.weixin.model.response.VerifyCodeResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by cgy
 * 2018/5/25  10:52
 *
 * 使用Retrofit对网络请求进行配置
 */
public class ApiRetrofit extends BaseApiRetrofit{

    public MyApi mApi;
    public static ApiRetrofit mInstance;

    private ApiRetrofit() {
        super();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        //在构造方法中完成对Retrofit接口的初始化
        mApi = new Retrofit.Builder()
                .baseUrl(MyApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(MyApi.class);
    }

    public static ApiRetrofit getInstance() {
        if (mInstance == null) {
            synchronized (ApiRetrofit.class) {
                if (mInstance == null) {
                    mInstance = new ApiRetrofit();
                }
            }
        }
        return mInstance;
    }
    private RequestBody getRequestBody(Object obj) {
        String route = new Gson().toJson(obj);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), route);
        return body;
    }

    //登录
    public Observable<LoginResponse> login(String region, String phone, String password) {
        return mApi.login(getRequestBody(new LoginRequest(region, phone, password)));
    }

    //注册
    public Observable<CheckPhoneResponse> checkPhoneAvailable(String region, String phone) {
        return mApi.checkPhoneAvailable(getRequestBody(new CheckPhoneRequest(phone, region)));
    }

    public Observable<SendCodeResponse> sendCode(String region, String phone) {
        return mApi.sendCode(getRequestBody(new SendCodeRequest(region, phone)));
    }

    public Observable<VerifyCodeResponse> verifyCode(String region, String phone, String code) {
        return mApi.verifyCode(getRequestBody(new VerifyCodeRequest(region, phone, code)));
    }

    public Observable<RegisterResponse> register(String nickName, String password, String verification_token) {
        return mApi.register(getRequestBody(new RegisterRequest(nickName, password, verification_token)));
    }
    public Observable<GetTokenResponse> getToken() {
        return mApi.getToken();
    }

    //个人信息
    public Observable<SetNameResponse> setName(String nickName) {
        return mApi.setName(getRequestBody(new SetNameRequest(nickName)));
    }

    public Observable<SetPortraitResponse> setPortrait(String portraitUri) {
        return mApi.setPortrait(getRequestBody(new SetPortraitRequest(portraitUri)));
    }

    public Observable<ChangePasswordResponse> changePassword(String oldPassword, String newPassword) {
        return mApi.changePassword(getRequestBody(new ChangePasswordRequest(oldPassword, newPassword)));
    }

    /**
     *
     * @param password              密码 6-20个字节 不能包含空格
     * @param verification_token    调用/user/verify_code 成功后返回的 activation_token
     * @return
     */
    public Observable<ResetPasswordResponse> resetPassword(String password, String verification_token) {
        return mApi.resetPassword(getRequestBody(new ResetPasswordRequest(password, verification_token)));
    }

    public Observable<UserRelationshipResponse> getAllUserRelationship() {
        return mApi.getAllUserRelationship();
    }

    public Observable<GetGroupResponse> getGroups() {
        return mApi.getGroups();
    }

    public Observable<GetGroupMemberResponse> getGroupMember(String groupId) {
        return mApi.getGroupsMember(groupId);
    }

    public Observable<GetGroupInfoResponse> getGroupInfo(String groupId) {
        return mApi.getGroupInfo(groupId);
    }


    public Observable<QiNiuTokenResponse> getQiNiuToken() {
        return mApi.getQiNiuToken();
    }



    public Observable<GetUserInfoByIdResponse> getUserInfoById(String userId) {
        return mApi.getUserInfoById(userId);
    }
}
