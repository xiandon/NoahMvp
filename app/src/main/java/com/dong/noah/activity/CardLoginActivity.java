package com.dong.noah.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.dong.noah.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardLoginActivity extends AppCompatActivity {

    private static String CARD_LOGIN_HTML = "CARD_LOGIN_HTML";
    @BindView(R.id.wv_card_login)
    WebView wvCardLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_login);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String html = intent.getStringExtra(CARD_LOGIN_HTML);

        wvCardLogin.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);


    }

    public static void actionStart(Context context, String html) {
        Intent intent = new Intent(context, CardLoginActivity.class);
        intent.putExtra("CARD_LOGIN_HTML", html);
        context.startActivity(intent);
    }

}
