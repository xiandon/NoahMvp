package com.dong.noah.ok_http2;

public interface ReqCallBack<T> {
    /**
     * 响应成功
     *
     * @param result 结果
     */
    void onReqSuccess(T result);

    /**
     * 响应失败
     *
     * @param errorMsg 错误信息
     */
    void onReqFailed(String errorMsg);
}
