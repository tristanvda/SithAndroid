package com.grietenenknapen.sithandroid.service;

public interface ServiceCallBack<T>  {

    void onSuccess(T returnData);

    void onError(int messageResId);
}
