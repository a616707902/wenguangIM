package com.wenguang.chat.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wenguang.chat.R;
import com.wenguang.chat.activity.MessageConvertionActivity;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 作者：chenpan
 * 时间：2016/12/26 11:30
 * 邮箱：616707902@qq.com
 * 描述：
 */

public class MessageCursorAdapter extends CursorAdapter {
    private ViewHolder viewHolder;

    //    private String[] contentProjection = new String[]{ContactsContract.PhoneLookup._ID,
//            ContactsContract.PhoneLookup.DISPLAY_NAME};
    private String[] contentProjection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.PHOTO_ID, ContactsContract.PhoneLookup._ID};

    public MessageCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.message_item,
                    null);
        }

        viewHolder.mImageView = (ImageView) view.findViewById(R.id.msg_photo);
        viewHolder.tvName = (TextView) view.findViewById(R.id.msg_name);
        viewHolder.tvContent = (TextView) view.findViewById(R.id.msg_content);
        viewHolder.tvDate = (TextView) view.findViewById(R.id.msg_date);

        view.setTag(viewHolder);
        return view;


//        viewHolder.tvName.setText(this.mList.get(position).getName());
//        viewHolder.tvContent.setText(this.mList.get(position).getSmsContent());
//        viewHolder.mImageView.setImageBitmap(this.mList.get(position).getContactPhoto());
//        viewHolder.tvDate.setText(this.mList.get(position).getSmsDate());
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        if (view != null) {
            viewHolder = (ViewHolder) view.getTag();
            ImageView photoView = viewHolder.mImageView;
            TextView dataTv = viewHolder.tvDate;

            TextView nameTv = viewHolder.tvName;
            TextView contentTv = viewHolder.tvContent;

            final String thread_id = cursor.getString(0);
            String content = cursor.getString(1);
            int count = cursor.getInt(2);
            final String address = cursor.getString(3);
            long date = cursor.getLong(4);
//            // 短信内容
//            if (content != null && content.length() > 10) {
//                contentTv.setText(content.substring(0, 10));
//            } else {
            contentTv.setText(content);

//            }
            // 次数--时间--java基础知识，获取时间的
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd " + "\n" + "hh:mm:ss");
            String formatTime = dateFormat.format(calendar.getTime());

            /**
             * 定义成局部变量，不要定义成成员变量
             */
            String phoneName = null;

            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                    Uri.encode(address));
            Cursor localCursor = context.getContentResolver().query(uri,
                    contentProjection, null, null, null);
            if (localCursor.getCount() != 0) {
                localCursor.moveToFirst();
                System.out.println("之后----" + localCursor);
                phoneName = localCursor.getString(localCursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                long photoid = localCursor.getLong(localCursor.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_ID));
                long contactid = localCursor.getLong(localCursor.getColumnIndex(ContactsContract.PhoneLookup._ID));
                // 如果photoid 大于0 表示联系人有头像 ，如果没有给此人设置头像则给他一个默认的
                nameTv.setText(phoneName);
                if (photoid > 0) {
                    Uri uri1 = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), uri1);
                    photoView.setImageBitmap(BitmapFactory.decodeStream(input));
                } else {
                    photoView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_appicon));
                }
            } else {
                nameTv.setText(address);
                photoView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_appicon));
            }

            localCursor.close();
            dataTv.setText(formatTime);
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MessageConvertionActivity.class);
                    intent.putExtra("thread_id", thread_id);
                    intent.putExtra("phone", address);
                    long threadId=Long.valueOf(thread_id);
                    if (threadId > 0) {
                        intent.setData(ContentUris.withAppendedId(Telephony.Threads.CONTENT_URI, threadId));
                    }
                    context.startActivity(intent);
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//
//                    intent.setType("vnd.android-dir/mms-sms");
////
//// 此为号码
//                    intent.setData(Uri.parse("content://mms-sms/conversations/" + address + "/"));
//
//                    context.startActivity(intent);
                }
            });

        }
    }

    public static class ViewHolder {
        public ImageView mImageView;
        public TextView tvName;
        public TextView tvContent;
        public TextView tvDate;
    }
}
