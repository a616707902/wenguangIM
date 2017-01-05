package com.wenguang.chat.mvp.model;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.wenguang.chat.bean.User;
import com.wenguang.chat.common.UserManager;
import com.wenguang.chat.event.CallBackBmob;

import org.json.JSONArray;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by MVPHelper on 2016/11/15
 */

public class LoginModelImpl implements LoginModel {
    CallBackBmob<String> backBmob;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 1:
                    backBmob.succssCallBack((String) msg.obj);
                    break;
                case 2:
                    backBmob.failed("环信注册失败");
                    break;
            }
        }

    };

    @Override
    public void sendVerifyCode(Context context, String phone, final CallBackBmob callBackBmob) {
//        SMSSDK.getVerificationCode("+86", phone.trim(), new OnSendMessageHandler() {
//            @Override
//            public boolean onSendMessage(String s, String s1) {
//                return false;
//            }
//        });
        BmobSMS.requestSMSCode( phone.trim(), "wenguang1", new QueryListener<Integer>() {

            @Override
            public void done(Integer smsId, BmobException ex) {
                // TODO Auto-generated method stub
                if (ex == null) {//验证码发送成功
                    callBackBmob.succssCallBack(true);
                    Log.i("bmob", "短信id：" + smsId);//用于查询本次短信发送详情
                } else {
                    callBackBmob.succssCallBack(false);
                }
            }
        });
    }

    /**
     * 查询数据
     */
    public void queryData(String account, final CallBackBmob callBackBmob) {
        BmobQuery query = new BmobQuery("wenguangUser");
        query.addWhereEqualTo("account", account);
        //v3.5.0版本提供`findObjectsByTable`方法查询自定义表名的数据
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray jsonArray, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "查询成功：" + jsonArray.toString());
                    System.out.print(jsonArray);
                    if (jsonArray != null && jsonArray.length() > 0) {

                        Gson gson = new Gson();
                        User[] users = gson.fromJson(jsonArray.toString(), User[].class);
                        if (users != null) {
                            UserManager.getInstance().saveUser(users[0]);
                            callBackBmob.succssCallBack(true);
                         //   UserManager.getInstance().saveUser(users[0]);
                        } else {
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

    public void addData(final String account, final String password, final CallBackBmob callBackBmob) {
        User user = new User();
        backBmob = callBackBmob;
//注意：不能调用user.setObjectId("")方法
        user.setAccount(account);
        user.setPassword(password);
        UserManager.getInstance().saveUser(user);
        user.save(new SaveListener<String>() {

            @Override
            public void done(final String objectId, BmobException e) {
                if (e == null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
//注册失败会抛出HyphenateException
                            try {
                                EMClient.getInstance().createAccount(account, password);//同步方法
                                Message message = new Message();
                                message.arg1 = 1;
                                message.obj = objectId;
                                handler.sendMessage(message);
                            } catch (HyphenateException e1) {
                                e1.printStackTrace();
                                Message message = new Message();
                                message.arg1 = 2;
                                message.obj = objectId;
                                handler.sendMessage(message);
                            }
                        }
                    }).start();
                    // callBackBmob.succssCallBack(objectId);
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
    public void queryDataByUser(String account, String password, final CallBackBmob callBackBmob) {
        BmobQuery query = new BmobQuery("wenguangUser");
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
                        User[] users = gson.fromJson(jsonArray.toString(), User[].class);
                        if (users != null) {
                            UserManager.getInstance().saveUser(users[0]);
                            callBackBmob.succssCallBack(true);

                        } else {
                            callBackBmob.failed("账号或密码错误");
                        }
                    } else {
                        // callBackBmob.succssCallBack(false );
                        callBackBmob.failed("账号或密码错误");
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    callBackBmob.failed(e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }
}