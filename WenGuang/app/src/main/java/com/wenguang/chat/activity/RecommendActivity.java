package com.wenguang.chat.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.wenguang.chat.R;
import com.wenguang.chat.adapter.ReContactAdapter;
import com.wenguang.chat.base.BaseActivity;
import com.wenguang.chat.common.Common;
import com.wenguang.chat.mvp.presenter.BasePresenter;
import com.wenguang.chat.mvp.presenter.RecommendPresenter;
import com.wenguang.chat.mvp.view.RecommendView;
import com.wenguang.chat.utils.common.CharacterParser;
import com.wenguang.chat.utils.common.PinyinComparator;
import com.wenguang.chat.utils.common.SortModel;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 推荐界面
 */
public class RecommendActivity extends BaseActivity implements RecommendView {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.ivClearText)
    ImageView ivClearText;
    @Bind(R.id.layoutContainer)
    FrameLayout layoutContainer;
    @Bind(R.id.selectall_ck)
    CheckBox selectallCk;
    @Bind(R.id.lv_contacts)
    ListView lvContacts;
    @Bind(R.id.btnRight)
    Button btnRight;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private List<SortModel> mAllContactsList;
    private ReContactAdapter adapter;
    private boolean isselect = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recommend;
    }

    @Override
    protected void initInjector() {
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.icon_back);
        setSupportActionBar(toolbar);
        characterParser = CharacterParser.getInstance();
        mAllContactsList = new ArrayList<SortModel>();
        pinyinComparator = new PinyinComparator();
        Collections.sort(mAllContactsList, pinyinComparator);// 根据a-z进行排序源数据
        adapter = new ReContactAdapter(this, mAllContactsList);
        lvContacts.setAdapter(adapter);

    }

    @Override
    protected void initEventAndData() {
        // item事件
        Common.SEND_LIST=null;
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
                ReContactAdapter.ViewHolder viewHolder = (ReContactAdapter.ViewHolder) view.getTag();
                viewHolder.cbChecked.toggle();
                // 将CheckBox的选中状况记录下来
                ReContactAdapter.getIsSelected().put(position, viewHolder.cbChecked.isChecked());
//                viewHolder.cbChecked.performClick();
//                adapter.toggleChecked(position);
            }
        });
        MPermissions.requestPermissions(this, Common.REQUECT_CODE_CONTACT, Manifest.permission.READ_CONTACTS);
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
                    ((RecommendPresenter) mPresenter).serchContact(RecommendActivity.this, content, mAllContactsList);
                    // mAdapter.updateData(mContacts);
                } else {
                    adapter.updateListView(mAllContactsList);
                }
                lvContacts.setSelection(0);

            }

        });

    }

    @Override
    public BasePresenter getPresenter() {
        return new RecommendPresenter();
    }

    @PermissionGrant(Common.REQUECT_CODE_CONTACT)
    public void requestContactSuccess() {
        ((RecommendPresenter) mPresenter).getContactList(this);

    }

    @PermissionDenied(Common.REQUECT_CODE_CONTACT)
    public void requestContactFailed() {
    }


    @OnClick({R.id.ivClearText, R.id.selectall_ck,R.id.btnRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivClearText:
                etSearch.setText("");
                break;
            case R.id.selectall_ck:
                if (isselect) {
                    adapter.selectAll();
                    isselect = false;
                } else {
                    adapter.ReselectAll();
                    isselect = true;
                }
                break;
            case R.id.btnRight:
                Common.SEND_LIST=adapter.getSelectedList();
                Intent intent= new Intent(this,SendMessageActivity.class);
                startActivity(intent);
                finish();

                break;
        }
    }

    @Override
    public void setAdapter(List<SortModel> models) {
        Collections.sort(models, pinyinComparator);
        selectallCk.setChecked(false);
        adapter.updateListView(models);
    }

    @Override
    public void setList(List<SortModel> models) {
        mAllContactsList = models;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoadProgressDialog(String str) {

    }

    @Override
    public void dissDialog() {

    }
}
