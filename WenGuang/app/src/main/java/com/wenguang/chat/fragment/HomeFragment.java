package com.wenguang.chat.fragment;

import android.Manifest;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.wenguang.chat.R;
import com.wenguang.chat.base.BaseFragment;
import com.wenguang.chat.common.Common;
import com.wenguang.chat.mvp.presenter.BasePresenter;
import com.wenguang.chat.mvp.presenter.HomeFragmentPresenter;
import com.wenguang.chat.mvp.view.HomeFragmentView;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

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

    @Override
    protected int getlayoutId() {
        return R.layout.home_fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initEventAndData() {

        MPermissions.requestPermissions(this, Common.REQUECT_CODE_MISSCALL, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS);


    }

    @Override
    protected void lazyLoadData() {

    }

    @Override
    public BasePresenter getPresenter() {
        return new HomeFragmentPresenter(mActivity);
    }


    @Override
    public void setSMSCount(int count) {

    }

    @Override
    public void setMissedCalls(int count) {
       // mHomeMissedCalls.setText(getString(R.string));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @PermissionGrant(Common.REQUECT_CODE_MISSCALL)
    public void requestMissCallSuccess()
    {
        ((HomeFragmentPresenter)mPresenter).rigsterContentObserver();
        ((HomeFragmentPresenter)mPresenter).getSMSCount();
        ((HomeFragmentPresenter)mPresenter).getMissCallCount();
    }

    @PermissionDenied(Common.REQUECT_CODE_MISSCALL)
    public void requestMissCallFailed()
    {
    }
}