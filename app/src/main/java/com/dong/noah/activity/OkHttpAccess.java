package com.dong.noah.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.dong.noah.R;
import com.dong.noah.card.Constant;
import com.dong.noah.card.CreateSignature;
import com.dong.noah.card.CreateSignature2;
import com.dong.noah.card.JsonRequest;
import com.dong.noah.ok_http.OkManager;
import com.dong.noah.ok_http2.ReqCallBack;
import com.dong.noah.ok_http2.RequestManager;
import com.dong.noah.utils.GetDate;
import com.dong.noah.utils.PreferencesUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpAccess extends AppCompatActivity {
    @BindView(R.id.et_ok_http_card_bind_phone)
    EditText etOkHttpCardBindPhone;
    @BindView(R.id.et_ok_http_card_login_phone)
    EditText etOkHttpCardLoginPhone;
    private OkManager okManager;
    private String TAG = "OkHttpAccess";
    private RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http_access);
        ButterKnife.bind(this);

        okManager = OkManager.getInstance();
        requestManager = RequestManager.getmInstance(this);

    }

    @OnClick({
            R.id.btn_ok_http_access_one,
            R.id.btn_ok_http_access_winning,
            R.id.btn_ok_http_generate_signature,
            R.id.btn_ok_http_card_login,
            R.id.btn_ok_http_card_bind_phone_verification_code,
            R.id.btn_ok_http_card_bind_phone,
            R.id.btn_ok_http_card_login_phone_verification_code,
            R.id.btn_ok_http_card_login_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_ok_http_access_one:
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("q", "陆军微博");
                map.put("key", Constant.JUHE_NEWS_KEY);
                map.put("dtype", "json");


        /*        okManager.sendComplexForm(Constant.JUHE_NEWS_URL, map, new OkManager.Fun4() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i(TAG, jsonObject.toString());
                        Toast.makeText(OkHttpAccess.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
                    }
                });*/
                String url = okManager.getUrl(map);
                Log.i(TAG, "异步请求URL = " + url);
                okManager.getAsyncGetJsonObjectByUrl(url, new OkManager.Fun4() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i(TAG, "Get异步请求 = " + jsonObject.toString());
                        Toast.makeText(OkHttpAccess.this, jsonObject.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btn_ok_http_access_winning:
                final HashMap<String, String> map2 = new HashMap<String, String>();
                map2.put("q", "特朗普");
                map2.put("key", Constant.JUHE_NEWS_KEY);
                map2.put("dtype", "json");

                requestManager.requestAsyn("onebox/news/query", 2, map2, new ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        Log.i(TAG, "数据请求成功 = " + result.toString());
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                    }
                });
                break;

            case R.id.btn_ok_http_generate_signature:
               /* JsonRequest jsonRequest = new JsonRequest();

                jsonRequest.setAccessToken("aimaii1212u19283nfna");
                jsonRequest.setMemberChannel("001");
                jsonRequest.setAppKey("faoifafhaf111");
                jsonRequest.setAccessToken("affajfaijfna");

                HashMap<String, Object> map1 = new HashMap<>();
                map1.put("mobile", "13800138000");
                map1.put("password", "123456");

                jsonRequest.setData(map1);

                Page page = new Page(1, 20);
                jsonRequest.setPage(page);

                CreateSignature signature = new CreateSignature();
                boolean b = signature.verifySign("/cbclient/user/logina", jsonRequest, "d854efca-7865-4377-9409-e9049ea03ab6", "asdf");
                if (b) {
                    Log.i(TAG, "签名校验成功");
                } else {
                    Log.i(TAG, "签名校验失败");
                }*/

                break;

            case R.id.btn_ok_http_card_login:
                final HashMap<String, String> map3 = new HashMap<String, String>();
                map3.put("mobile", "13272678783");
                map3.put("agentCode", "268");
                map3.put("clientId", "36e9295f-5be7-46d8-bc86-539df21fcf5d");


                requestManager.requestAsyn(Constant.CARD_LOGIN_THIRD, 0, map3, new ReqCallBack<Object>() {
                    @Override
                    public void onReqFailed(String errorMsg) {

                    }

                    @Override
                    public void onReqSuccess(Object result) {
                    }
                });


                break;

            case R.id.btn_ok_http_card_bind_phone_verification_code:
                // 绑定手机号验证码
                HashMap<String, Object> mapGame = new HashMap<>();
                mapGame.put("gameType", "HOT");


                String jsonGame = new CreateSignature().verifySign(Constant.CARD_LOGIN_PHONE_CODE, mapGame);


                okManager.postSendString(Constant.CARD_GAME_LIST_GAME, jsonGame, new OkManager.Fun4() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i(TAG, "访问成功" + jsonObject.toString());
                    }
                });


                break;
            case R.id.btn_ok_http_card_bind_phone:
                // 绑定手机
                final HashMap<String, String> mapBindPhone = new HashMap<String, String>();
                mapBindPhone.put("mobile", "13272678783");
                mapBindPhone.put("smsCode", etOkHttpCardBindPhone.getText().toString());

                requestManager.requestAsyn(Constant.CARD_BIND_PHONE, 2, mapBindPhone, new ReqCallBack<Object>() {
                    @Override
                    public void onReqFailed(String errorMsg) {
                    }

                    @Override
                    public void onReqSuccess(Object result) {
                        Log.i(TAG, "");
                    }
                });

                break;
            case R.id.btn_ok_http_card_login_phone_verification_code:
                // 生成签名

                HashMap<String, Object> mapData = new HashMap<>();
                mapData.put("mobile", "13272678783");

                final String json = new CreateSignature().verifySign(Constant.CARD_LOGIN_PHONE_CODE, mapData);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("jsonUp", json);

          /*      okManager.postSendString(Constant.CARD_LOGIN_PHONE_CODE, json, new OkManager.Fun4() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i(TAG, "-------------" + jsonObject.toString());
                    }
                });*/


                requestManager.requestAsyn(Constant.CARD_LOGIN_PHONE_CODE, 3, hashMap, new ReqCallBack<Object>() {
                    @Override
                    public void onReqFailed(String errorMsg) {

                    }

                    @Override
                    public void onReqSuccess(Object result) {
                        Log.i(TAG, "访问成功 = " + result);
                    }
                });


                break;
            case R.id.btn_ok_http_card_login_phone:
                // 手机登陆
                HashMap<String, Object> mapLogin = new HashMap<>();
                mapLogin.put("mobile", "13272678783");
                mapLogin.put("agentCode", "268");
                mapLogin.put("referee", "QQQ");
                mapLogin.put("channel", "AAA");
                mapLogin.put("smsCode", etOkHttpCardLoginPhone.getText().toString());

                final String jsonLogin = new CreateSignature().verifySign(Constant.CARD_LOGIN_PHONE_CODE, mapLogin);
                HashMap<String, String> hashMapLogin = new HashMap<>();
                hashMapLogin.put("jsonUp", jsonLogin);
                requestManager.requestAsyn(Constant.CARD_LOGIN_PHONE, 3, hashMapLogin, new ReqCallBack<Object>() {
                    @Override
                    public void onReqFailed(String errorMsg) {

                    }

                    @Override
                    public void onReqSuccess(Object result) {
                        Log.i(TAG, "登陆成功 = " + result.toString());
                    }
                });

                break;
        }

    }

    private void post(String s) {
        OkHttpClient okHttpClient = new OkHttpClient();

        //创建一个RequestBody(参数1：数据类型 参数2传递的json串)
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), s);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url("http://guess.laikajj.com/smsCode/sendAuth")
//                .header("cookie", "JSESSIONID=EB36DE5E50E342D86C55DAE0CDDD4F6D")
                .addHeader("content-type", "application/json;charset:utf-8")
                .post(requestBody)       // 表单提交
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("fail", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.e("success", res);

            }
        });


    }


}
