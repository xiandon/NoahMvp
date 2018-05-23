package com.dong.noah.mvp.presenter.impl;


import com.dong.noah.mvp.view.LoginView;

public interface ILogin {
    /**
     * 登陆方法
     */
    void login();

    /**
     * 绑定view
     *
     * @param loginView
     */
    void bind(LoginView loginView);

}
