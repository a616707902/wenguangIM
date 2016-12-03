package com.wenguang.chat.activity;

import android.Manifest;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.wenguang.chat.R;
import com.wenguang.chat.adapter.ContactsSortAdapter;
import com.wenguang.chat.base.BaseActivity;
import com.wenguang.chat.common.Common;
import com.wenguang.chat.mvp.presenter.BasePresenter;
import com.wenguang.chat.mvp.presenter.CallPhonePresenter;
import com.wenguang.chat.mvp.view.CallPhoneView;
import com.wenguang.chat.utils.ToastUtils;
import com.wenguang.chat.utils.common.PinyinComparator;
import com.wenguang.chat.utils.common.SortModel;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class CallPhoneActivity extends BaseActivity implements CallPhoneView {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.contact_lv)
    ListView mContactLv;
    @Bind(R.id.call_phone_num)
    EditText mCallPhoneNum;
    @Bind(R.id.one)
    Button mOne;
    @Bind(R.id.two)
    Button mTwo;
    @Bind(R.id.three)
    Button mThree;
    @Bind(R.id.four)
    Button mFour;
    @Bind(R.id.five)
    Button mFive;
    @Bind(R.id.six)
    Button mSix;
    @Bind(R.id.seven)
    Button mSeven;
    @Bind(R.id.eight)
    Button mEight;
    @Bind(R.id.nine)
    Button mNine;
    @Bind(R.id.zero)
    Button mZero;
    @Bind(R.id.main_btn)
    ImageView mMainBtn;
    @Bind(R.id.call_phone)
    ImageView mCallPhone;
    @Bind(R.id.delete)
    ImageView mDelete;
    @Bind(R.id.keyboard)
    LinearLayout mKeyboard;
    private InputMethodManager mIM;
    private String phoneNum = "";

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private List<SortModel> mAllContactsList;
    private ContactsSortAdapter adapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_call_phone;
    }

    @Override
    protected void initInjector() {
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.drawable.icon_back);
        setSupportActionBar(mToolbar);
        mIM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mCallPhoneNum.setInputType(InputType.TYPE_NULL);
        mAllContactsList = new ArrayList<SortModel>();
        pinyinComparator = new PinyinComparator();
        Collections.sort(mAllContactsList, pinyinComparator);// 根据a-z进行排序源数据
        adapter = new ContactsSortAdapter(this, mAllContactsList);
        mContactLv.setAdapter(adapter);

    }

    @Override
    protected void initEventAndData() {


        MPermissions.requestPermissions(this, Common.REQUECT_CODE_CONTACT, Manifest.permission.READ_CONTACTS);
        mCallPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = mCallPhoneNum.getText().toString();
                if (content.length() > 0) {
//                    ArrayList<SortModel> fileterList = (ArrayList<SortModel>) search(content);
//                    adapter.updateListView(fileterList);
                    ((CallPhonePresenter) mPresenter).serchContact(CallPhoneActivity.this, content, mAllContactsList);
                    // mAdapter.updateData(mContacts);
                } else {
                    adapter.updateListView(mAllContactsList);
                }
                mContactLv.setSelection(0);
            }
        });
        // item事件
        mContactLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
                SortModel sortModel = (SortModel) adapter.getItem(position);
                phoneNum=sortModel.getNumber();
                callPhone();

            }
        });
    }

    @Override
    public BasePresenter getPresenter() {
        return new CallPhonePresenter();
    }


    @OnClick({R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.zero, R.id.main_btn, R.id.call_phone, R.id.delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.one:
                phoneNum += "1";
                break;
            case R.id.two:
                phoneNum += "2";
                break;
            case R.id.three:
                phoneNum += "3";
                break;
            case R.id.four:
                phoneNum += "4";
                break;
            case R.id.five:
                phoneNum += "5";
                break;
            case R.id.six:
                phoneNum += "6";
                break;
            case R.id.seven:
                phoneNum += "7";
                break;
            case R.id.eight:
                phoneNum += "8";
                break;
            case R.id.nine:
                phoneNum += "9";
                break;
            case R.id.zero:
                phoneNum += "0";
                break;
            case R.id.main_btn:
                onBackPressed();
                break;
            case R.id.call_phone:
                callPhone();
                break;
            case R.id.delete:
                if (phoneNum.length() > 0) {
                    phoneNum = phoneNum.substring(0, phoneNum.length() - 1);
                }
                break;
        }
        mCallPhoneNum.setText(phoneNum);
    }

    private void callPhone() {
        MPermissions.requestPermissions(this, Common.REQUECT_CALL_PHONE, Manifest.permission.CALL_PHONE);
    }


    @OnClick(R.id.call_phone_num)
    public void onClick() {
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
    public void showMessage(String str) {
        ToastUtils.showToast(this, str);
    }

    @PermissionGrant(Common.REQUECT_CODE_CONTACT)
    public void requestContactSuccess() {
        ((CallPhonePresenter) mPresenter).getContactList(this);

    }

    @PermissionDenied(Common.REQUECT_CODE_CONTACT)
    public void requestContactFailed() {
    }

    @PermissionGrant(Common.REQUECT_CALL_PHONE)
    public void requestCallPhone() {
        ((CallPhonePresenter) mPresenter).callPhone(this, phoneNum);

    }

    @PermissionDenied(Common.REQUECT_CALL_PHONE)
    public void requestCallFailed() {
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
