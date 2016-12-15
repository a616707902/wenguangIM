package com.wenguang.chat.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;
import com.wenguang.chat.R;
import com.wenguang.chat.common.AppManager;
import com.wenguang.chat.event.RxManager;
import com.wenguang.chat.mvp.presenter.BasePresenter;
import com.wenguang.chat.mvp.view.BaseView;
import com.wenguang.chat.utils.StatusBarUtil;
import com.wenguang.chat.utils.ToastUtils;

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
        registerCallLister();
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
    }

    private void registerCallLister() {
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        registerReceiver(new CallReceiver(), callFilter);


    }
    private class CallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 拨打方username
            String from = intent.getStringExtra("from");
            // call type
            String type = intent.getStringExtra("type");
            //跳转到通话页面

        }
    }
    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                       // ToastUtils.showToast(BaseActivity.this,"帐号已经被移除");
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                        ToastUtils.showToast(BaseActivity.this,"帐号在其他设备登录");
                    } else {
                        if (NetUtils.hasNetwork(BaseActivity.this)) {
                           // ToastUtils.showToast(BaseActivity.this,"连接不到聊天服务器");
                        }
                        //连接不到聊天服务器
                        else {
                           // ToastUtils.showToast(BaseActivity.this,"当前网络不可用，请检查网络设置");
                        }
                        //当前网络不可用，请检查网络设置
                    }
                }
            });
        }
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
        StatusBarUtil.setStatusBarColor(this, ContextCompat.getColor(this, R.color.white));
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    public void SetStatusBarColor(int color) {
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
