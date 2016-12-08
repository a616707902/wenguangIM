package com.wenguang.chat.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.wenguang.chat.common.MyApplication;

import java.util.Map;

/**
 * Created by Administrator on 2016/5/19.
 */
public class VolleyRequest {
    public  static StringRequest stringRequest;
    public static Context mContext;
    public  static void  RequestGetString(Context context,String url,String tag,VolleyInterface volleyInterface){
        MyApplication.getQueues().cancelAll(tag);
        stringRequest=new StringRequest(Request.Method.GET,url,volleyInterface.loadingListener(),volleyInterface.errorListener());
        stringRequest.setTag(tag);
        MyApplication.getQueues().add(stringRequest);
        MyApplication.getQueues().start();

    }
    public static void RequestPostString(Context context,String url,String tag, final Map<String,String> params,VolleyInterface volleyInterface){
        MyApplication.getQueues().cancelAll(tag);
        stringRequest=new StringRequest(Request.Method.POST,url,volleyInterface.loadingListener(),volleyInterface.errorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        stringRequest.setTag(tag);
        MyApplication.getQueues().add(stringRequest);
        MyApplication.getQueues().start();
    }
}
