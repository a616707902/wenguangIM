package com.wenguang.chat.fragment;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wenguang.chat.R;
import com.wenguang.chat.activity.CallPhoneActivity;
import com.wenguang.chat.adapter.BaseRecyclerAdapter;
import com.wenguang.chat.adapter.RecordCallHolder;
import com.wenguang.chat.base.BaseFragment;
import com.wenguang.chat.bean.RecordEntity;
import com.wenguang.chat.common.Common;
import com.wenguang.chat.mvp.presenter.BasePresenter;
import com.wenguang.chat.mvp.presenter.HomeFragmentPresenter;
import com.wenguang.chat.mvp.view.HomeFragmentView;
import com.wenguang.chat.utils.DimenUtil;
import com.wenguang.chat.utils.ToastUtils;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.List;

import butterknife.Bind;

public class HomeFragment extends BaseFragment implements HomeFragmentView {


    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.home_missed_calls)
    TextView mHomeMissedCalls;
    @Bind(R.id.home_unread_messages)
    TextView mHomeUnreadMessages;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.fab)
    FloatingActionButton mFab;


    private BaseRecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected int getlayoutId() {
        return R.layout.home_fragment;
    }

    @Override
    protected void initInjector() {
        mToolbar.setTitle("");
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        mLayoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void initEventAndData() {
        mToolbar.setOverflowIcon(getResources().getDrawable(R.drawable.toolbar_add));
        MPermissions.requestPermissions(this, Common.REQUECT_CODE_MISSCALL, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS, Manifest.permission.READ_CALL_LOG);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity, CallPhoneActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void lazyLoadData() {

    }

    @Override
    public BasePresenter getPresenter() {
        return new HomeFragmentPresenter(mActivity);
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
                Intent intent=new Intent(Intent.ACTION_EDIT, Uri.parse("content://com.android.contacts/contacts/"+"1"));
                startActivity(intent);
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void setSMSCount(int count) {
        String text = getString(R.string.miss_sms_counts, count);
        int index = text.lastIndexOf("信") + 1;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(((int) DimenUtil.dp2px(20f))), index, text.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        mHomeUnreadMessages.setText(spannableStringBuilder);
    }

    @Override
    public void setMissedCalls(int count) {
        String text = getString(R.string.miss_call_counts, count);
        int index = text.lastIndexOf("电") + 1;
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(((int) DimenUtil.dp2px(20f))), index, text.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        mHomeMissedCalls.setText(spannableStringBuilder);
    }

    @Override
    public void setCallRecordList(List<RecordEntity> callRecordList) {
        if (mRecyclerView == null) return;

        if (mAdapter == null) {
            mAdapter = new BaseRecyclerAdapter(callRecordList, R.layout.call_records_item, RecordCallHolder.class);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            if ((mAdapter.getItem(0) == null) && (callRecordList.size() == 0))
                return;
            if ((mAdapter.getItem(0) == null) || (callRecordList.size() == 0) || (!((RecordEntity) mAdapter.getItem(0)).getName().equals(callRecordList.get(0).getName())))
                mAdapter.setmDatas(callRecordList);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @PermissionGrant(Common.REQUECT_CODE_MISSCALL)
    public void requestMissCallSuccess() {
        ((HomeFragmentPresenter) mPresenter).rigsterContentObserver();
        ((HomeFragmentPresenter) mPresenter).getSMSCount();
        ((HomeFragmentPresenter) mPresenter).getMissCallCount();
        ((HomeFragmentPresenter) mPresenter).getCallRecords();

    }

    @PermissionDenied(Common.REQUECT_CODE_MISSCALL)
    public void requestMissCallFailed() {
    }


}