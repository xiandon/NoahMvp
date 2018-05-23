package com.dong.noah.mvp.view;

import com.dong.noah.entity.User;

public interface LoginView {
    /**
     * 第一步，在LoginView中编写需要实现的接口，并在相应的Activity和Fragment中调用
     */


    String getAccount(); //获取用户的账号，返回账号

    String getPassword(); // 获取用户的密码，返回密码

    void loginSuccess(User user); // 登陆的实现，需要传入用户对象

    void showNetworkError(String netError); // 显示网络异常

    void showVerifyFailed(String error);    // 信息验证失败，账号或密码错误

}
