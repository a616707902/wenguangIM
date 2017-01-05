package com.wenguang.chat.activity;

import android.Manifest;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wenguang.chat.R;
import com.wenguang.chat.base.BaseActivity;
import com.wenguang.chat.bean.User;
import com.wenguang.chat.common.Common;
import com.wenguang.chat.common.UserManager;
import com.wenguang.chat.mvp.presenter.BasePresenter;
import com.wenguang.chat.mvp.presenter.SendMessagePresenter;
import com.wenguang.chat.mvp.view.SendMessageView;
import com.wenguang.chat.utils.ToastUtils;
import com.wenguang.chat.widget.CircleImageView;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import me.naturs.library.statusbar.StatusBarHelper;

public class SendMessageActivity extends BaseActivity implements SendMessageView {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.mine_img)
    CircleImageView mineImg;

    @Bind(R.id.send_msg)
    EditText sendMsg;
    @Bind(R.id.send)
    Button send;
    @Bind(R.id.mine_name)
    TextView mineName;
    @Bind(R.id.mine_sign)
    TextView mineSign;
    private String message;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_message;
    }

    @Override
    protected void initInjector() {
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.icon_back);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void initEventAndData() {
        User user = UserManager.getInstance().getUser();
        mineName.setText(user.getName());
        mineSign.setText(user.getMinesign());
        sendMsg.setText(getString(R.string.send_message, user.getName()));
        BmobFile file = user.getMinepic();
        if (file != null) {
            String url = file.getUrl();
            if (!TextUtils.isEmpty(url)) {
                Glide.with(this)
                        .load(url)
                        .placeholder(R.drawable.icon_default)
                        .error(R.drawable.head_icon)
                        .into(mineImg);
            }
        }
    }
    @Override
    protected void onTintStatusBar() {
        if (mStatusBarHelper == null) {
            mStatusBarHelper = new StatusBarHelper(this, StatusBarHelper.LEVEL_19_TRANSLUCENT,
                    StatusBarHelper.LEVEL_NONE);
        }
        mStatusBarHelper.setActivityRootLayoutFitSystemWindows(false);
//        mStatusBarHelper.setColor(getResources().getColor(R.color.drawer_status_bar_color));
        mStatusBarHelper.setColor(Color.TRANSPARENT);


    }
    @Override
    public BasePresenter getPresenter() {
        return new SendMessagePresenter();
    }


    @OnClick(R.id.send)
    public void onClick() {
        message = sendMsg.getText().toString();
        MPermissions.requestPermissions(this, Common.REQUECT_SEND_MESSAGE, Manifest.permission.SEND_SMS);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @PermissionGrant(Common.REQUECT_SEND_MESSAGE)
    public void requestCallPhone() {
        ((SendMessagePresenter) mPresenter).sendMessage(this, message);

    }

    @PermissionDenied(Common.REQUECT_SEND_MESSAGE)
    public void requestCallFailed() {
    }

    @Override
    public void showMessage(String str) {
        ToastUtils.showToast(this, str);
    }

    @Override
    public void back() {
        onBackPressed();
    }

    @Override
    public void showLoadProgressDialog(String str) {

    }

    @Override
    public void dissDialog() {

    }
}
