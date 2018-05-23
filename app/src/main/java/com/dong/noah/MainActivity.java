package com.dong.noah;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dong.noah.activity.OkHttpAccess;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_ok_http_a, R.id.btn_ok_http_b, R.id.btn_ok_http_c, R.id.btn_ok_http_d})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_ok_http_a:
                startActivity(new Intent(MainActivity.this, OkHttpAccess.class));
                break;
            case R.id.btn_ok_http_b:
                break;
            case R.id.btn_ok_http_c:
                break;
            case R.id.btn_ok_http_d:
                break;
        }
    }
}
