package com.wenguang.chat.network;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by Administrator on 2016/5/19.
 */
public abstract class VolleyInterface {
    private Context context;
    public static Response.Listener<String> mListener;
    public static Response.ErrorListener mErrorListener;

    public VolleyInterface(Context context, Response.Listener<String> lisener, Response.ErrorListener errorListener) {
        this.context = context;
        mListener = lisener;
        mErrorListener = errorListener;
    }
    public VolleyInterface(Context context) {
        this.context = context;

    }
    public Response.Listener<String>  loadingListener(){
        mListener=new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onMySuccess(response);
            }
        };
        return mListener;
    }
    public Response.ErrorListener errorListener(){
        mErrorListener=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onMyError(error);
            }
        };
        return mErrorListener;
    }
    public  abstract void onMySuccess(String result);
    public  abstract void onMyError(VolleyError result);
}
