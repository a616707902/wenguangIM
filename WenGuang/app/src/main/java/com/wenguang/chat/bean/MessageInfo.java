package com.wenguang.chat.bean;

import android.graphics.Bitmap;

/**
 * 作者：chenpan
 * 时间：2016/12/23 11:38
 * 邮箱：616707902@qq.com
 * 描述：
 */

public class MessageInfo {

    private String name;
    private Bitmap contactPhoto;

    private String smsContent;

    private String smsDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public String getSmsDate() {
        return smsDate;
    }

    public void setSmsDate(String smsDate) {
        this.smsDate = smsDate;
    }
    public Bitmap getContactPhoto() {
        return contactPhoto;
    }

    public void setContactPhoto(Bitmap contactPhoto) {
        this.contactPhoto = contactPhoto;
    }

}
