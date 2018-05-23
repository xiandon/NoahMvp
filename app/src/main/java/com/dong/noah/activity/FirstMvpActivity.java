package com.dong.noah.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dong.noah.R;
import com.dong.noah.entity.User;
import com.dong.noah.mvp.presenter.LoginPresenter;
import com.dong.noah.mvp.view.LoginView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FirstMvpActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.tv_first_mvp_user_name)
    EditText tvFirstMvpUserName;
    @BindView(R.id.tv_first_mvp_user_password)
    EditText tvFirstMvpUserPassword;
    @BindView(R.id.btn_first_mvp_submit)
    Button btnFirstMvpSubmit;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_mvp);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        presenter = new LoginPresenter();
        presenter.bind(this);
    }

    @OnClick({R.id.tv_first_mvp_user_name, R.id.tv_first_mvp_user_password, R.id.btn_first_mvp_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_first_mvp_user_name:
                break;
            case R.id.tv_first_mvp_user_password:
                break;
            case R.id.btn_first_mvp_submit:
                presenter.login();
                break;
        }
    }

    @Override
    public String getAccount() {
        return tvFirstMvpUserName.getText().toString();
    }

    @Override
    public String getPassword() {
        return tvFirstMvpUserPassword.getText().toString();
    }

    @Override
    public void loginSuccess(User user) {
        Toast.makeText(this, user.getName() + "登陆成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNetworkError(String netError) {
        Toast.makeText(this, netError, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showVerifyFailed(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
