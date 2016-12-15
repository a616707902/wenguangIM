package com.wenguang.chat.mvp.model;


import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.wenguang.chat.bean.User;
import com.wenguang.chat.common.UserManager;
import com.wenguang.chat.event.CallBackBmob;

import org.json.JSONArray;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by MVPHelper on 2016/11/16
 */

public class MineFragmentModelImpl implements MineFragmentModel {
    public void getUserByAccount(String account, final CallBackBmob callBackBmob) {
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
                            callBackBmob.succssCallBack(users[0]);
                        } else {
                            callBackBmob.failed("数据加载失败");
                        }

                    } else {
                        callBackBmob.failed(e.getMessage());
                    }
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    callBackBmob.failed(e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void upDataUserMessageByAccount(String name, String sign, String idcard, String picpath, final CallBackBmob<String> call) {
        final User user = new User();
        user.setName(name);
        user.setIdcard(idcard);
        user.setMinesign(sign);
        if (!TextUtils.isEmpty(picpath)) {
            final BmobFile bmobFile = new BmobFile(new File(picpath));
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        user.setMinepic(bmobFile);
                    }else{

                    }
                    user.update(UserManager.getInstance().getUser().getObjectId(), new UpdateListener() {

                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                call.succssCallBack("更新成功");
                            } else {
                                Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                                call.failed(e.getMessage() + "," + e.getErrorCode());
                            }
                        }
                    });
                }
            });
          //  user.setMinepic(bmobFile);
        } else {
            user.update(UserManager.getInstance().getUser().getObjectId(), new UpdateListener() {

                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        call.succssCallBack("更新成功");
                    } else {
                        Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                        call.failed(e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });
        }
    }


}