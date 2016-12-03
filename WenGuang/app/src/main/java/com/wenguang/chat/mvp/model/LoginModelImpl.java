package com.wenguang.chat.mvp.model;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.wenguang.chat.bean.User;
import com.wenguang.chat.common.UserManager;
import com.wenguang.chat.event.CallBackBmob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

/**
* Created by MVPHelper on 2016/11/15
*/

public class LoginModelImpl implements LoginModel{

    @Override
    public void sendVerifyCode(Context context, String phone) {
        SMSSDK.getVerificationCode("+86", phone.trim(), new OnSendMessageHandler() {
            @Override
            public boolean onSendMessage(String s, String s1) {
                return false;
            }
        });
    }
    /**
     * 查询数据
     */
    public void queryData(String account, final CallBackBmob callBackBmob){
        BmobQuery query =new BmobQuery("wenguangUser");
        query.addWhereEqualTo("account", account);
        //v3.5.0版本提供`findObjectsByTable`方法查询自定义表名的数据
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "查询成功：" + jsonArray.toString());
                    System.out.print(jsonArray);
                    if (jsonArray != null && jsonArray.length() > 0) {
                       /* try {
                            JSONObject jsonObj = jsonArray.getJSONObject(0);
                            String userid = jsonObj.getString("objectId");
                            if (!TextUtils.isEmpty(userid)) {
                              callBackBmob.succssCallBack(true);
                            } else {
                                callBackBmob.succssCallBack(false);
                            }
                        } catch (JSONException s) {
                            e.printStackTrace();
                            callBackBmob.failed(e.getMessage());
                        }*/
                        Gson gson = new Gson();
                        User[]  users = gson.fromJson(jsonArray.toString(), User[].class);
                        if (users!=null){
                            callBackBmob.succssCallBack(true);
                            UserManager.getInstance().saveUser(users[0]);
                        }else{
                            callBackBmob.succssCallBack(false);
                        }
                    } else {
                        callBackBmob.succssCallBack(false);
                      //  callBackBmob.failed(e.getMessage() );
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    callBackBmob.succssCallBack(false);
                }
            }
        });
    }
    public void addData(String account ,String password, final CallBackBmob callBackBmob){
        User user = new User();
//注意：不能调用user.setObjectId("")方法
        user.setAccount(account);
        user.setPassword(password);
        UserManager.getInstance().saveUser(user);
        user.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    callBackBmob.succssCallBack(objectId);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    callBackBmob.failed(e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
    /**
     * 查询数据
     */
    public void queryDataByUser(String account, String password,final CallBackBmob callBackBmob){
        BmobQuery query =new BmobQuery("wenguangUser");
        query.addWhereEqualTo("account", account);
        query.addWhereEqualTo("password", password);
        //v3.5.0版本提供`findObjectsByTable`方法查询自定义表名的数据
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "查询成功：" + jsonArray.toString());
                    System.out.print(jsonArray);
                    if (jsonArray != null && jsonArray.length() > 0) {

                        Gson gson = new Gson();
                        User[]  users = gson.fromJson(jsonArray.toString(), User[].class);
                        if (users!=null){
                            callBackBmob.succssCallBack(true);
                            UserManager.getInstance().saveUser(users[0]);
                        }else{
                            callBackBmob.failed("账号或密码错误" );
                        }
                    } else {
                       // callBackBmob.succssCallBack(false );
                        callBackBmob.failed("账号或密码错误" );
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    callBackBmob.failed(e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
}