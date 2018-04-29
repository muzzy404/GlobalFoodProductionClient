package com.spbpu.hackaton.globalfoodproductionclient;

public interface ResponseCallback {
    public void onSuccess(String response);
    public void onError(String error);
}
