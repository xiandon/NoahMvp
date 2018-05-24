package com.dong.noah.ok_http2;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.dong.noah.card.Constant;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestManager {


    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static final String TAG = RequestManager.class.getSimpleName();
    private static final String BASE_URL = Constant.CARD_URL;//请求接口根地址
    private static volatile RequestManager mInstance;//单利引用
    public static final int TYPE_GET = 0;//get请求
    public static final int TYPE_POST_JSON = 1;//post请求参数为json
    public static final int TYPE_POST_FORM = 2;//post请求参数为表单
    public static final int TYPE_POST_JSON_STR = 3;//post请求参数为json
    private OkHttpClient mOkHttpClient;//okHttpClient 实例
    private Handler okHttpHandler;//全局处理子线程和M主线程通信

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public RequestManager(Context context) {
        // 初始化Okhttp
        mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        // 初始化Handler
        okHttpHandler = new Handler(context.getMainLooper());
    }

    /**
     * 获取单例引用
     *
     * @param context 上下文
     * @return 返回RequestManager
     */
    public static RequestManager getmInstance(Context context) {
        RequestManager inst = mInstance;
        if (inst == null) {
            synchronized (RequestManager.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new RequestManager((context.getApplicationContext()));
                    mInstance = inst;
                }
            }
        }
        return inst;
    }

    /**
     * OkHttp异步请求
     *
     * @param url         接口地址
     * @param requestType 请求类型
     * @param paramsMap   请求参数
     * @param callBack    请求数据回调
     * @param <T>         数据泛型
     * @return Call
     */
    public <T> Call requestAsyn(String url, int requestType, HashMap<String, String> paramsMap, ReqCallBack<T> callBack) {

        Call call = null;
        switch (requestType) {
            case TYPE_GET:
                call = getByAsyn(url, paramsMap, callBack);
                break;
            case TYPE_POST_JSON:
                call = postJsonByAsyn(url, paramsMap, callBack);
                break;
            case TYPE_POST_FORM:
                call = postFormByAsyn(url, paramsMap, callBack);
                break;
            case TYPE_POST_JSON_STR:
                call = postJsonByAsynString(url, paramsMap, callBack);
                break;
        }
        return call;
    }

    /**
     * OkHttp 异步请求.
     * 提交Form表单
     *
     * @param url       接口地址
     * @param paramsMap 数据参数
     * @param callBack  回调函数
     * @param <T>       数据泛型
     * @return
     */
    private <T> Call postFormByAsyn(String url, HashMap<String, String> paramsMap, final ReqCallBack<T> callBack) {
        try {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                builder.add(key, paramsMap.get(key));
            }
            RequestBody formBody = builder.build();
            String requestUrl = String.format("%s/%s", BASE_URL, url);

            Log.i(TAG, "POST FORM异步请求URL = " + requestUrl);

            final Request request = addHeaders().url(requestUrl).post(formBody).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败", callBack);
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        Log.e(TAG, "response ----->" + string);
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack("服务器错误", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * OkHttp异步请求
     * 提交JSON数据
     *
     * @param url       接口地址
     * @param paramsMap 数据参数
     * @param callBack  回调函数
     * @param <T>       数据泛型
     * @return
     */
    private <T> Call postJsonByAsyn(String url, HashMap<String, String> paramsMap, final ReqCallBack<T> callBack) {
        try {
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }
            String params = tempParams.toString();
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, params);
            String requestUrl = String.format("%s/%s", BASE_URL, url);

            Log.i(TAG, "POST JSON异步请求URL = : " + requestUrl);

            final Request request = addHeaders().url(requestUrl).post(body).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败", callBack);
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        Log.e(TAG, "response ----->" + string);
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack("服务器错误", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * OkHttp异步请求
     * 提交JSON数据
     *
     * @param url       接口地址
     * @param paramsMap 数据参数
     * @param callBack  回调函数
     * @param <T>       数据泛型
     * @return
     */
    private <T> Call postJsonByAsynString(String url, HashMap<String, String> paramsMap, final ReqCallBack<T> callBack) {
        try {

            String params = paramsMap.get("jsonUp");

            Log.i(TAG, "JSON请求参数 = " + params);

            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, params);

            String requestUrl = BASE_URL + url;

            final Request request = addHeaders().url(requestUrl).post(body).build();
            final Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败", callBack);
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        Log.e(TAG, "response ----->" + string);
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack("服务器错误", callBack);
                    }
                }
            });
            return call;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }


    /**
     * OkHttp异步请求
     *
     * @param url       接口地址
     * @param paramsMap 请求参数
     * @param callBack  回调函数
     * @param <T>       数据泛型
     * @return
     */
    private <T> Call getByAsyn(String url, HashMap<String, String> paramsMap, final ReqCallBack<T> callBack) {
        StringBuilder tempParams = new StringBuilder();
        try {
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                pos++;
            }

            // 补全地址
            String requestUrl = String.format("%s/%s?%s", BASE_URL, url, tempParams.toString());

            Log.i(TAG, "GET 异步请求URL = " + requestUrl);

            Request request = addHeaders().url(requestUrl).build();

            Call call = mOkHttpClient.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    failedCallBack("访问失败", callBack);
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        Log.e(TAG, "response ----->" + string);
                        successCallBack((T) string, callBack);
                    } else {
                        failedCallBack("服务器错误", callBack);
                    }
                }
            });

            return call;


        } catch (Exception e) {
            Log.e(TAG, "OKHttp请求错误");
        }
        return null;
    }

    /**
     * 统一同意处理成功信息
     *
     * @param result   成功数据
     * @param callBack 回调
     * @param <T>      数据泛型
     */
    private <T> void successCallBack(final T result, final ReqCallBack<T> callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onReqSuccess(result);
                }
            }
        });
    }

    /**
     * 统一处理失败信息
     *
     * @param errorMsg 错误信息
     * @param callBack 回调
     * @param <T>      数据泛型
     */
    private <T> void failedCallBack(final String errorMsg, final ReqCallBack<T> callBack) {
        okHttpHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onReqFailed(errorMsg);
                }
            }
        });
    }

    /**
     * OkHttp同步请求
     * 同一入口
     *
     * @param url         接口地址
     * @param requestType 请求类型
     * @param params      请求参数
     */
    public void reqestSyn(String url, int requestType, HashMap<String, String> params) {
        switch (requestType) {
            case TYPE_GET:
                getBySyn(url, params);
                break;
            case TYPE_POST_JSON:
                postJsonBySyn(url, params);
                break;
            case TYPE_POST_FORM:
                postFormBySyn(url, params);
                break;
        }
    }

    /**
     * POST同步请求.
     * 提交FORM表单.
     *
     * @param url    接口地址
     * @param params 请求参数
     */
    private void postFormBySyn(String url, HashMap<String, String> params) {
        try {
            //处理参数
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : params.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(params.get(key), "utf-8")));
                pos++;
            }

            //补全请求地址
            String requestUrl = String.format("%s/%s", BASE_URL, url);

            //生成参数
            String sParams = tempParams.toString();

            //创建一个请求实体对象RequestBody
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, sParams);

            //创建一个请求
            Request request = addHeaders().url(requestUrl).post(body).build();

            // 创建一个Call
            Call call = mOkHttpClient.newCall(request);

            //执行请求
            Response response = call.execute();

            //请求执行成功
            if (response.isSuccessful()) {
                // 返回成功的数据
                Log.i(TAG, "POST FORM 同步请求: " + response.body().string());
            }

        } catch (Exception e) {

        }

    }

    /**
     * POST同步请求.
     * 提交JSON数据.
     *
     * @param url
     * @param params
     */
    private void postJsonBySyn(String url, HashMap<String, String> params) {
        try {

            //创建一个FormBody.Builder
            FormBody.Builder builder = new FormBody.Builder();

            for (String key : params.keySet()) {
                //追加表单信息
                builder.add(key, params.get(key));
            }

            // 生成表单实体对象
            RequestBody formBody = builder.build();

            // 补全请求地址
            String requestUrl = String.format("%s/%s", BASE_URL, url);

            // 创建一个请求
            Request request = addHeaders().url(requestUrl).post(formBody).build();

            // 创建一个call
            Call call = mOkHttpClient.newCall(request);

            // 执行请求
            Response response = call.execute();

            if (response.isSuccessful()) {
                // 得到请求到的值
                Log.i(TAG, "POST JSON 同步请求: " + response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * GET同步请求请求数据
     *
     * @param url    接口地址
     * @param params 请求参数
     */
    private void getBySyn(String url, HashMap<String, String> params) {
        StringBuilder tempParams = new StringBuilder();
        try {
            // 处理参数
            int pos = 0;
            for (String key : params.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                // 对参数进行URLEncoder
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(params.get(key), "utf-8")));
                pos++;
            }

            // 补全请求地址
            String requestUrl = String.format("%s/%s?%s", BASE_URL, url, tempParams.toString());
            Log.i(TAG, "同步请求URL = " + requestUrl);

            // 创建一个请求
            Request request = addHeaders().url(requestUrl).build();

            // 创建一个Call
            Call call = mOkHttpClient.newCall(request);

            // 执行请求
            Response response = call.execute();

            // 得到请求的值
            Log.i(TAG, "GET 同步请求: " + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /**
     * 统一请求添加头信息
     *
     * @return Request.Builder
     */
    private Request.Builder addHeaders() {
        Request.Builder builder = new Request.Builder().addHeader("content-type", "application/json;charset:utf-8");
        return builder;
    }
}
