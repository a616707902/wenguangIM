package com.wenguang.chat.mvp.model;


import java.util.List;

/**
* Created by MVPHelper on 2016/12/03
*/

public class SendMessageModelImpl implements SendMessageModel{
    @Override
public  void sendMessage(String phoneNumber,String message){
    //获取短信管理器
    android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
    //拆分短信内容（手机短信长度限制）
    List<String> divideContents = smsManager.divideMessage(message);
    for (String text : divideContents) {
        smsManager.sendTextMessage(phoneNumber, null, text, null, null);
    }
}
}