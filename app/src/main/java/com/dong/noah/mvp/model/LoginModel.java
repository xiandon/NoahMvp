package com.dong.noah.mvp.model;

import com.dong.noah.entity.User;

public class LoginModel {

    public void login(String name, String password, onLoginResultListener onLoginResultListener) {
        // 验证登陆信息
        if ("qwer".equals(name) && "123456".equals(password)) {
            onLoginResultListener.loginSuccess(new User(name, password));
        } else {
            onLoginResultListener.loginFailure();
        }
    }

    public interface onLoginResultListener {

        void loginSuccess(User user);// 登陆成功回调方法

        void loginFailure();// 登陆失败回调方法

    }
}
