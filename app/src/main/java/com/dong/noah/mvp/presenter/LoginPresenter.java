package com.dong.noah.mvp.presenter;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.dong.noah.entity.User;
import com.dong.noah.mvp.model.LoginModel;
import com.dong.noah.mvp.presenter.impl.ILogin;
import com.dong.noah.mvp.view.LoginView;

public class LoginPresenter implements ILogin {

    private static String TAG = "LoginPresenter";
    /**
     * 登陆用户实现者
     */
    private LoginModel mLoginModel;

    /**
     * 构造方法中实例化model对象
     */
    public LoginPresenter() {
        mLoginModel = new LoginModel();
    }

    // 视图接口对象
    private LoginView mLoginView;


    /**
     * 登陆业务
     */
    public void logina() {
        String account = mLoginView.getAccount();
        String password = mLoginView.getPassword();

        Log.e(TAG, "用户名：" + account + "; 密码：" + password);

        if (checkParameter(account, password)) {
            // 写在调用最后一步时，所需要做的准备工作
            doSomePrepare();

            // 实现登陆逻辑，并实现登陆结果接口调用
            mLoginModel.login(account, password, new LoginModel.onLoginResultListener() {
                @Override
                public void loginSuccess(User user) {
                    // 登陆成功
                    mLoginView.loginSuccess(user);
                }

                @Override
                public void loginFailure() {
                    // 登陆失败
                    mLoginView.showVerifyFailed("用户名或密码错误");
                }
            });
        }


    }

    @Override
    public void login() {
        String account = mLoginView.getAccount();
        String password = mLoginView.getPassword();

        Log.e(TAG, "用户名：" + account + "; 密码：" + password);

        if (checkParameter(account, password)) {
            // 写在调用最后一步时，所需要做的准备工作
            doSomePrepare();

            // 实现登陆逻辑，并实现登陆结果接口调用
            mLoginModel.login(account, password, new LoginModel.onLoginResultListener() {
                @Override
                public void loginSuccess(User user) {
                    // 登陆成功
                    mLoginView.loginSuccess(user);
                }

                @Override
                public void loginFailure() {
                    // 登陆失败
                    mLoginView.showVerifyFailed("用户名或密码错误");
                }
            });
        }
    }

    @Override
    public void bind(LoginView loginView) {
        mLoginView = loginView;
    }


    private void doSomePrepare() {
    }

    private boolean checkParameter(String account, String password) {
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            mLoginView.showVerifyFailed("用户名或密码错误");//提示错误信息
            return false;
        } else if (!checkNetwork()) {
            mLoginView.showNetworkError("网络错误");
            return false;
        }
        return true;
    }

    private boolean checkNetwork() {
        return true;
    }
}
