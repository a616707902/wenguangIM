package com.wenguang.chat.bean;

/**
 * 作者：chenpan
 * 时间：2016/12/23 11:38
 * 邮箱：616707902@qq.com
 * 描述：
 */

public class MessageInfo {

//    private String name;
//    private Bitmap contactPhoto;
//
//    private String smsContent;
//
//    private String smsDate;

    private String phone;
    private String date;
    private String content;
    private int layoutId;

    public MessageInfo(String phone, String date, String content, int layoutId) {
        this.phone = phone;
        this.date = date;
        this.content = content;
        this.layoutId = layoutId;
    }

    public MessageInfo() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }
}
