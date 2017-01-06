package com.wenguang.chat.mvp.model;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wenguang.chat.bean.User;
import com.wenguang.chat.event.CallBack;
import com.wenguang.chat.event.CallBackBmob;
import com.wenguang.chat.utils.common.SortModel;
import com.wenguang.chat.utils.common.SortToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
* Created by MVPHelper on 2016/11/16
*/

public class ContactFragmentModelImpl implements ContactFragmentModel{
    String chReg = "[\\u4E00-\\u9FA5]+";// 中文字符串匹配

    /**
     * 加载联系人数据
     */
    @Override
    public  void loadContacts(final Context context,final CallBack callBack) {
        final List<SortModel> mAllContactsList = new ArrayList<SortModel>();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                try {
                    // 插叙
                    String queryTye[] = { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, "sort_key",ContactsContract.Contacts.SORT_KEY_PRIMARY,
                            ContactsContract.CommonDataKinds.Phone.PHOTO_ID };
                    if(android.os.Build.VERSION.SDK_INT>=19){
                        queryTye[3]="phonebook_label";
                    }
                    ContentResolver resolver = context.getContentResolver();
                    Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, queryTye, null, null,
                            "sort_key COLLATE LOCALIZED ASC");
                    if (phoneCursor == null || phoneCursor.getCount() == 0) {
                        Toast.makeText(context, "未获得读取联系人权限 或 未获得联系人数据", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    int PHONES_NUMBER_INDEX = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int PHONES_DISPLAY_NAME_INDEX = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    int SORT_KEY_INDEX = phoneCursor.getColumnIndex("sort_key");
                    int PHONEBOOK_LABEL = phoneCursor.getColumnIndex(queryTye[3]);
                    int PHOTO_ID = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID);
                    if (phoneCursor.getCount() > 0) {

                        while (phoneCursor.moveToNext()) {
                            String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                            if (TextUtils.isEmpty(phoneNumber))
                                continue;
                            // 头像id
                            long photoId = phoneCursor.getLong(PHOTO_ID);
                            String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                            String sortKey = phoneCursor.getString(SORT_KEY_INDEX);
                            String book = phoneCursor.getString(PHONEBOOK_LABEL);
                            SortModel sortModel = new SortModel(contactName, phoneNumber, sortKey);

                            if (book == null) {
                                book = "#";
                            } else if (book.equals("#")) {
                                book = "#";
                            } else if (book.equals("")) {
                                book = "#";
                            }
                            sortModel.sortLetters = book;
                            sortModel.sortToken = parseSortKey(book);
                            mAllContactsList.add(sortModel);
                        }
                    }
                    phoneCursor.close();
                   // ((Activity)context). runOnUiThread(new Runnable() {
                      //  public void run() {
                            callBack.getContactlist(mAllContactsList);

//                        }
//                    });
                } catch (Exception e) {
                    Log.e("xbc", e.getLocalizedMessage());
                }
            }
//        }).start();
//    }



    // String chReg="[^\\u4E00-\\u9FA5]";//除中文外的字符匹配
    /**
     * 解析sort_key,封装简拼,全拼
     *
     * @param sortKey
     * @return
     */
    public SortToken parseSortKey(String sortKey) {
        SortToken token = new SortToken();
        if (sortKey != null && sortKey.length() > 0) {
            // 其中包含的中文字符
            String[] enStrs = sortKey.replace(" ", "").split(chReg);
            for (int i = 0, length = enStrs.length; i < length; i++) {
                if (enStrs[i].length() > 0) {
                    // 拼接简拼
                    token.simpleSpell += enStrs[i].charAt(0);
                    token.wholeSpell += enStrs[i];
                }
            }
        }
        return token;
    }


    /**
     * 模糊查询
     *
     * @param str
     * @return
     */
    @Override
    public void search(String str, List<SortModel> mAllContactsList,CallBack callBack) {
        List<SortModel> filterList = new ArrayList<SortModel>();// 过滤后的list
        // if (str.matches("^([0-9]|[/+])*$")) {// 正则表达式 匹配号码
        if (str.matches("^([0-9]|[/+]).*")) {// 正则表达式
            // 匹配以数字或者加号开头的字符串(包括了带空格及-分割的号码)
            String simpleStr = str.replaceAll("\\-|\\s", "");
            for (SortModel contact : mAllContactsList) {
                if (contact.number != null && contact.name != null) {
                    if (contact.simpleNumber.contains(simpleStr) || contact.name.contains(str)) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        } else {
            for (SortModel contact : mAllContactsList) {
                if (contact.number != null && contact.name != null) {
                    // 姓名全匹配,姓名首字母简拼匹配,姓名全字母匹配
                    if (contact.name.toLowerCase(Locale.CHINESE).contains(str.toLowerCase(Locale.CHINESE))
                            || contact.sortKey.toLowerCase(Locale.CHINESE).replace(" ", "")
                            .contains(str.toLowerCase(Locale.CHINESE))
                            || contact.sortToken.simpleSpell.toLowerCase(Locale.CHINESE)
                            .contains(str.toLowerCase(Locale.CHINESE))
                            || contact.sortToken.wholeSpell.toLowerCase(Locale.CHINESE)
                            .contains(str.toLowerCase(Locale.CHINESE))) {
                        if (!filterList.contains(contact)) {
                            filterList.add(contact);
                        }
                    }
                }
            }
        }
        callBack.getContactlist(filterList);
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
                            callBackBmob.succssCallBack(true);
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
}