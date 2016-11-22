package com.wenguang.chat.mvp.model;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.wenguang.chat.utils.common.SortModel;
import com.wenguang.chat.utils.common.SortToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
* Created by MVPHelper on 2016/11/16
*/

public class ContactFragmentModelImpl implements ContactFragmentModel{
    String chReg = "[\\u4E00-\\u9FA5]+";// 中文字符串匹配

    /**
     * 加载联系人数据
     */
    public  void loadContacts(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 插叙
                    String queryTye[] = { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, "sort_key", "phonebook_label",
                            ContactsContract.CommonDataKinds.Phone.PHOTO_ID };
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
                    int PHONEBOOK_LABEL = phoneCursor.getColumnIndex("phonebook_label");
                    int PHOTO_ID = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID);
                    if (phoneCursor.getCount() > 0) {
                       List<SortModel> mAllContactsList = new ArrayList<SortModel>();
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
                            // //优先使用系统sortkey取,取不到再使用工具取
                            // String sortLetters =
                            // getSortLetterBySortKey(sortKey);
                            // Log.i("main", "sortLetters:"+sortLetters);
                            // if (sortLetters == null) {
                            // sortLetters = getSortLetter(contactName);
                            // }
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
                   runOnUiThread(new Runnable() {
                        public void run() {
                            Collections.sort(mAllContactsList, pinyinComparator);
                            adapter.updateListView(mAllContactsList);
                        }
                    });
                } catch (Exception e) {
                    Log.e("xbc", e.getLocalizedMessage());
                }
            }
        }).start();
    }



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
}