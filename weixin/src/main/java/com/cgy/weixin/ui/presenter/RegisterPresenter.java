package com.cgy.weixin.ui.presenter;

import android.text.TextUtils;

import com.cgy.weixin.ui.activity.MainActivity;
import com.cgy.weixin.R;
import com.cgy.weixin.api.ApiRetrofit;
import com.cgy.weixin.app.AppConst;
import com.cgy.weixin.model.cache.UserCache;
import com.cgy.weixin.model.exception.ServerException;
import com.cgy.weixin.model.response.CheckPhoneResponse;
import com.cgy.weixin.model.response.LoginResponse;
import com.cgy.weixin.model.response.RegisterResponse;
import com.cgy.weixin.model.response.SendCodeResponse;
import com.cgy.weixin.model.response.VerifyCodeResponse;
import com.cgy.weixin.ui.activity.LoginActivity;
import com.cgy.weixin.ui.base.BaseActivity;
import com.cgy.weixin.ui.base.BasePresenter;
import com.cgy.weixin.ui.view.IRegisterView;
import com.cgy.weixin.utils.LogUtils;
import com.cgy.weixin.utils.RegularUtils;
import com.cgy.weixin.utils.UIUtils;

import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cgy
 * 2018/5/29  10:35
 */
public class RegisterPresenter extends BasePresenter<IRegisterView> {

    int time = 0;
    private Timer mTimer;

    private Subscription mSubscription;

    public RegisterPresenter(BaseActivity context) {
        super(context);
    }

    public void sendCode() {
        String phone = getView().getEtPhone().getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            UIUtils.showToast(UIUtils.getString(R.string.phone_not_empty));
            return;
        }

        if (!RegularUtils.isMobile(phone)) {
            UIUtils.showToast(UIUtils.getString(R.string.phone_format_error));
            return;
        }
        mContext.showWaitingDialog(UIUtils.getString(R.string.please_wait));
        ApiRetrofit.getInstance().checkPhoneAvailable(AppConst.REGION, phone)
                .subscribeOn(Schedulers.io())
                .flatMap((Func1<CheckPhoneResponse, Observable<SendCodeResponse>>) checkPhoneResponse -> {
                    int code = checkPhoneResponse.getCode();
                    if (code == 200) {
                        return ApiRetrofit.getInstance().sendCode(AppConst.REGION, phone);
                    } else {
                        return Observable.error(new ServerException(UIUtils.getString(R.string.phone_not_available)));
                    }

                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sendCodeResponse -> {
                    mContext.hideWaitingDialog();
                    int code = sendCodeResponse.getCode();
                    if (code == 200) {
                        changeSendCodeBtn();
                    } else {
                        sendCodeError(new ServerException(UIUtils.getString(R.string.send_code_error)));
                    }
                }, this::sendCodeError);
    }

    private void sendCodeError(Throwable throwable) {
        mContext.hideWaitingDialog();
        LogUtils.e(throwable.getLocalizedMessage());
        UIUtils.showToast(throwable.getLocalizedMessage());
    }

    private void changeSendCodeBtn() {
        //开始一分钟倒计时
        //每一秒执行一次Task
        mSubscription = Observable.create((Observable.OnSubscribe<Integer>) subscriber -> {
            time = 60;
            TimerTask mTask = new TimerTask() {
                @Override
                public void run() {
                    subscriber.onNext(--time);
                }
            };
            mTimer = new Timer();
            mTimer.schedule(mTask, 0, 1000);//每一秒执行一次Task
        }).observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(time -> {
                    if (getView().getBtnSendCode() != null) {
                        if (time >= 0) {
                            getView().getBtnSendCode().setEnabled(false);
                            getView().getBtnSendCode().setText(time + "");
                        } else {
                            getView().getBtnSendCode().setEnabled(true);
                            getView().getBtnSendCode().setText(UIUtils.getString(R.string.send_code_btn_normal_tip));
                        }
                    } else {
                        mTimer.cancel();
                    }
                }, throwable ->
                        LogUtils.sf(throwable.getLocalizedMessage()));

    }

    public void register() {
        String phone = getView().getEtPhone().getText().toString().trim();
        String password = getView().getEtPwd().getText().toString().trim();
        String nickName = getView().getEtNickName().getText().toString().trim();
        String code = getView().getEtVerifyCode().getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            UIUtils.showToast(UIUtils.getString(R.string.phone_not_empty));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            UIUtils.showToast(UIUtils.getString(R.string.password_not_empty));
            return;
        }
        if (TextUtils.isEmpty(nickName)) {
            UIUtils.showToast(UIUtils.getString(R.string.nickname_not_empty));
            return;
        }
        if (TextUtils.isEmpty(code)) {
            UIUtils.showToast(UIUtils.getString(R.string.verify_code_not_empty));
            return;
        }

        ApiRetrofit.getInstance().verifyCode(AppConst.REGION, phone, code)
                .flatMap((Func1<VerifyCodeResponse, Observable<RegisterResponse>>) verifyCodeResponse -> {
                    int code1 = verifyCodeResponse.getCode();
                    if (code1 == 200) {
                        return ApiRetrofit.getInstance().register(nickName, password, verifyCodeResponse.getResult().getVerification_token());
                    } else {
                        return Observable.error(new ServerException(UIUtils.getString(R.string.verify_code_error) + code1));
                    }
                })
                .flatMap((Func1<RegisterResponse, Observable<LoginResponse>>) registerResponse -> {
                    int code12 = registerResponse.getCode();
                    if (code12 == 200) {
                        return ApiRetrofit.getInstance().login(AppConst.REGION, phone, password);
                    } else {
                        return Observable.error(new ServerException(UIUtils.getString(R.string.register_error) + code12));
                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loginResponse -> {
                    int responseCode = loginResponse.getCode();
                    if (responseCode == 200) {
                        UserCache.save(loginResponse.getResult().getId(), phone, loginResponse.getResult().getToken());
                        mContext.jumpToActivityAndClearTask(MainActivity.class);
                        mContext.finish();
                    } else {
                        UIUtils.showToast(UIUtils.getString(R.string.login_error));
                        mContext.jumpToActivity(LoginActivity.class);
                    }
                }, this::registerError);

    }

    private void registerError(Throwable throwable) {
        LogUtils.sf(throwable.getLocalizedMessage());
        UIUtils.showToast(throwable.getLocalizedMessage());

    }

    public void unsubscribe() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }
}
