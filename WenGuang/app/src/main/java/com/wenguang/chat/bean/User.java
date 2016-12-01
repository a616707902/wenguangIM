package com.wenguang.chat.bean;

import cn.bmob.v3.BmobObject;

/**
 * 作者：chenpan
 * 时间：2016/11/30 10:59
 * 邮箱：616707902@qq.com
 * 描述：
 */

public class User extends BmobObject {
    private String account;
    private String password;
    private String idcard;
public User(){
    this.setTableName("wenguangUser");
}
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }
}
