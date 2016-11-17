package com.wenguang.chat.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;

import com.wenguang.chat.R;
import com.wenguang.chat.common.AppManager;
import com.wenguang.chat.event.RxManager;
import com.wenguang.chat.mvp.presenter.BasePresenter;
import com.wenguang.chat.mvp.view.BaseView;
import com.wenguang.chat.utils.StatusBarUtil;

import butterknife.ButterKnife;


public abstract class BaseActivity<T extends BasePresenter<BaseView>> extends AppCompatActivity implements IBase{
    public BasePresenter mPresenter;
    public RxManager mRxManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxManager = new RxManager();
        setBaseConfig();
        setContentView(getLayoutId());
        ButterKnife.bind(this);

        initInjector();
        mPresenter = getPresenter();
        if (mPresenter != null && this instanceof BaseView) {
            mPresenter.attach((BaseView) this);
        }
        initEventAndData();
    }

    private void setBaseConfig() {
        initTheme();
        AppManager.getAppManager().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        SetStatusBarColor();
    }


    /**
     * 获取布局
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected abstract void initInjector();

    /**
     * 设置监听
     */
    protected abstract void initEventAndData();

    private void initTheme() {
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor() {
        StatusBarUtil.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorAccent));
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor(int color) {
        StatusBarUtil.setStatusBarColor(this, color);
    }

    /**
     * 沉浸状态栏（4.4以上系统有效）
     */
    protected void SetTranslanteBar() {
        StatusBarUtil.translucentStatusBar(this, false);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null && this instanceof BaseView) {
            mPresenter.detachView();
            mPresenter = null;
        }
        mRxManager.clear();
        ButterKnife.unbind(this);
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
