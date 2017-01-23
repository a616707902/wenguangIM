package com.wenguang.chat.fragment;


import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wenguang.chat.R;
import com.wenguang.chat.adapter.ContactsSortAdapter;
import com.wenguang.chat.base.BaseFragment;
import com.wenguang.chat.common.Common;
import com.wenguang.chat.mvp.presenter.BasePresenter;
import com.wenguang.chat.mvp.presenter.ContactFragmentPresenter;
import com.wenguang.chat.mvp.view.ContactFragmentView;
import com.wenguang.chat.utils.MobileUtils;
import com.wenguang.chat.utils.common.CharacterParser;
import com.wenguang.chat.utils.common.PinyinComparator;
import com.wenguang.chat.utils.common.SortModel;
import com.wenguang.chat.widget.CallPhoneDialog;
import com.wenguang.chat.widget.SideBar;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;

public class ContactFragment extends BaseFragment implements ContactFragmentView {


    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.ivClearText)
    ImageView ivClearText;
    @Bind(R.id.layoutContainer)
    FrameLayout layoutContainer;
    @Bind(R.id.dialog)
    TextView dialog;
    @Bind(R.id.sidrbar)
    SideBar sideBar;
    @Bind(R.id.lv_contacts)
    ListView mListView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private List<SortModel> mAllContactsList;
    private ContactsSortAdapter adapter;
    private String callPhoneNum;
    private CallPhoneDialog callPhoneDialog;


    @Override
    protected int getlayoutId() {
        return R.layout.contact_fragment;
    }

    @Override
    protected void initInjector() {
        toolbar.setTitle("");
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        sideBar.setTextView(dialog);
        characterParser = CharacterParser.getInstance();
        mAllContactsList = new ArrayList<SortModel>();
        pinyinComparator = new PinyinComparator();
        Collections.sort(mAllContactsList, pinyinComparator);// 根据a-z进行排序源数据
        adapter = new ContactsSortAdapter(mActivity, mAllContactsList);
        mListView.setAdapter(adapter);
    }

    @Override
    protected void initEventAndData() {
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.toolbar_add));
        MPermissions.requestPermissions(this, Common.REQUECT_CODE_CONTACT, Manifest.permission.READ_CONTACTS);
        /** 清除输入字符 **/
        ivClearText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                etSearch.setText("");
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable e) {

                String content = etSearch.getText().toString();
                if ("".equals(content)) {
                    ivClearText.setVisibility(View.INVISIBLE);
                } else {
                    ivClearText.setVisibility(View.VISIBLE);
                }
                if (content.length() > 0) {
//                    ArrayList<SortModel> fileterList = (ArrayList<SortModel>) search(content);
//                    adapter.updateListView(fileterList);
                    ((ContactFragmentPresenter) mPresenter).serchContact(mActivity, content, mAllContactsList);
                    // mAdapter.updateData(mContacts);
                } else {
                    adapter.updateListView(mAllContactsList);
                }
                mListView.setSelection(0);

            }

        });

        // 设置右侧[A-Z]快速导航栏触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }
            }
        });
        // item事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
                adapter.toggleChecked(position);
                callPhoneNum = ((SortModel) adapter.getItem(position)).getNumber().replace(" ", "");
                ;
                if (MobileUtils.isMobileNo(callPhoneNum)) {
                    ((ContactFragmentPresenter) mPresenter).queryAccount(mActivity, callPhoneNum);
                } else {
                    showLoadProgressDialog(callPhoneNum);
                }

            }
        });

    }

    @Override
    protected void lazyLoadData() {


    }

    @Override
    public BasePresenter getPresenter() {
        return new ContactFragmentPresenter();
    }


    @Override
    public void setAdapter(List<SortModel> models) {
        Collections.sort(models, pinyinComparator);
        adapter.updateListView(models);
    }

    @Override
    public void setList(List<SortModel> models) {
        mAllContactsList = models;
    }

    @Override
    public void showDialog(String phone, String str) {
        if (callPhoneDialog == null) {

        } else {
            callPhoneDialog.dismiss();
            callPhoneDialog = null;

        }
        callPhoneDialog = CallPhoneDialog.getInstance(mActivity);
        callPhoneDialog.setPuOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone();
                callPhoneDialog.dismiss();
            }
        });
        callPhoneDialog.setText(phone, str);
        callPhoneDialog.show();
    }

    @PermissionGrant(Common.REQUECT_CODE_CONTACT)
    public void requestContactSuccess() {
        ((ContactFragmentPresenter) mPresenter).getContactList(mActivity);

    }

    @PermissionDenied(Common.REQUECT_CODE_CONTACT)
    public void requestContactFailed() {
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_item1:
                Intent intent = new Intent(Intent.ACTION_EDIT, Uri.parse("content://com.android.contacts/contacts/" + "1"));
                startActivity(intent);
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void showLoadProgressDialog(String str) {
        callPhoneDialog = CallPhoneDialog.getInstance(mActivity);
        callPhoneDialog.setPuOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone();
                callPhoneDialog.dismiss();
            }
        });
        callPhoneDialog.setText(callPhoneNum, null);
        callPhoneDialog.show();
    }

    @Override
    public void dissDialog() {

    }

    private void callPhone() {
        MPermissions.requestPermissions(this, Common.REQUECT_CALL_PHONE, Manifest.permission.CALL_PHONE);
    }

    @PermissionGrant(Common.REQUECT_CALL_PHONE)
    public void requestCallPhone() {
        ((ContactFragmentPresenter) mPresenter).callPhone(mActivity, callPhoneNum);

    }

    @PermissionDenied(Common.REQUECT_CALL_PHONE)
    public void requestCallFailed() {
    }
}
