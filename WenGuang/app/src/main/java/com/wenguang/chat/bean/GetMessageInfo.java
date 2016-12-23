package com.wenguang.chat.bean;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import com.wenguang.chat.R;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetMessageInfo {
    List<MessageInfo> list;
    Context context;
    MessageInfo messageInfo;  
  
    public GetMessageInfo(Context context) {  
        list = new ArrayList<MessageInfo>();
        this.context = context;  
  
    }  
  
    // --------------------------------收到的短息信息----------------------------------  
    public List<MessageInfo> getSmsInfos() {  
  
        final String SMS_URI_INBOX = "content://sms/inbox";// 收信箱  
        try {  
            ContentResolver cr = context.getContentResolver();
            String[] projection = new String[] { "_id", "address", "person","body", "date", "type" };  
            Uri uri = Uri.parse(SMS_URI_INBOX);
            Cursor cursor = cr.query(uri, projection, null, null, "date desc");
            while (cursor.moveToNext()) {  
                messageInfo = new MessageInfo();  
                // -----------------------信息----------------  
                int nameColumn = cursor.getColumnIndex("person");// 联系人姓名列表序号  
                int phoneNumberColumn = cursor.getColumnIndex("address");// 手机号  
                int smsbodyColumn = cursor.getColumnIndex("body");// 短信内容  
                int dateColumn = cursor.getColumnIndex("date");// 日期  
                int typeColumn = cursor.getColumnIndex("type");// 收发类型 1表示接受 2表示发送  
                String nameId = cursor.getString(nameColumn);  
                String phoneNumber = cursor.getString(phoneNumberColumn);  
                String smsbody = cursor.getString(smsbodyColumn);  
                Date d = new Date(Long.parseLong(cursor.getString(dateColumn)));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd " + "\n" + "hh:mm:ss");
                String date = dateFormat.format(d);  
  
                // --------------------------匹配联系人名字--------------------------  
  
                Uri personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,phoneNumber);
                Cursor localCursor = cr.query(personUri, new String[] {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.PHOTO_ID, ContactsContract.PhoneLookup._ID }, null, null, null);
                  
                System.out.println(localCursor.getCount());  
                System.out.println("之前----"+localCursor);  
                if (localCursor.getCount()!=0) {  
                    localCursor.moveToFirst();  
                    System.out.println("之后----"+localCursor);  
                    String name = localCursor.getString(localCursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                    long photoid = localCursor.getLong(localCursor.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_ID));
                    long contactid = localCursor.getLong(localCursor.getColumnIndex(ContactsContract.PhoneLookup._ID));
                    messageInfo.setName(name);  
                    // 如果photoid 大于0 表示联系人有头像 ，如果没有给此人设置头像则给他一个默认的  
                    if (photoid > 0) {  
                     Uri uri1 = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
                        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri1);
                        messageInfo.setContactPhoto(BitmapFactory.decodeStream(input));
                    } else {  
                        messageInfo.setContactPhoto(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_appicon));
                    }  
                }else{  
                        messageInfo.setName(phoneNumber);  
                        messageInfo.setContactPhoto(BitmapFactory.decodeResource(context.getResources(),R.mipmap.icon_appicon));
                    }  
                      
                localCursor.close();  
                  
                messageInfo.setSmsContent(smsbody);  
                messageInfo.setSmsDate(date);  
                list.add(messageInfo);  
            }  
  
        } catch (SQLiteException e) {
            e.printStackTrace();  
        }  
        return list;  
    }  
  
}  