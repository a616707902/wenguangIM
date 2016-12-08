package com.wenguang.chat.mvp.model;


import android.content.Context;

import com.android.volley.VolleyError;
import com.wenguang.chat.bean.PhoneLocal;
import com.wenguang.chat.event.CallBackBmob;
import com.wenguang.chat.network.VolleyInterface;
import com.wenguang.chat.network.VolleyRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MVPHelper on 2016/12/07
 */

public class EMCallPhoneModelImpl implements EMCallPhoneModel {
    String url = "http://apis.juhe.cn/mobile/get";

    @Override
    public void volleyPost(Context context, String phonenum, CallBackBmob<PhoneLocal> callBackBmob) {


        VolleyInterface volleyInterface = new VolleyInterface(context) {
            @Override
            public void onMySuccess(String result) {

            }

            @Override
            public void onMyError(VolleyError result) {
            }
        };
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phonenum);
        map.put("key", "e59b086b3ef990049b3056d6a5d5e342");
        VolleyRequest.RequestPostString(context, url, "get", map, volleyInterface);
    }

}