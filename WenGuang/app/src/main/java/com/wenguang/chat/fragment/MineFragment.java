package com.wenguang.chat.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wenguang.chat.R;
import com.wenguang.chat.activity.RecommendActivity;
import com.wenguang.chat.base.BaseFragment;
import com.wenguang.chat.bean.User;
import com.wenguang.chat.common.Common;
import com.wenguang.chat.common.UserManager;
import com.wenguang.chat.mvp.presenter.BasePresenter;
import com.wenguang.chat.mvp.presenter.MineFragmentPresenter;
import com.wenguang.chat.mvp.view.MineFragmentView;
import com.wenguang.chat.utils.ToastUtils;
import com.wenguang.chat.widget.CircleImageView;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;

public class MineFragment extends BaseFragment implements MineFragmentView {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.mine_img)
    CircleImageView mineImg;
    @Bind(R.id.mine_name)
    EditText mineName;
    @Bind(R.id.mine_sign)
    EditText mineSign;
    @Bind(R.id.mine_account)
    TextView mineAccount;
    @Bind(R.id.mine_idcard)
    EditText mineIdcard;
    @Bind(R.id.recommend)
    LinearLayout recommend;
    @Bind(R.id.about)
    LinearLayout about;
    @Bind(R.id.btnRight)
    Button btnRight;
    /**
     * true为编辑状态
     */
    private boolean isSave;
    /*选择照片对话框*/
    private Dialog dialog_choose_img_way;

    @Override
    protected int getlayoutId() {
        return R.layout.mine_fragment;
    }

    @Override
    protected void initInjector() {
        toolbar.setTitle("");

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

    }

    @Override
    protected void initEventAndData() {

        User user = UserManager.getInstance().getUser();
        mineName.setText(user.getName());
        mineSign.setText(user.getMinesign());
        mineAccount.setText(user.getAccount());
        mineIdcard.setText(user.getIdcard());
        BmobFile file = user.getMinepic();
        if (file != null) {
            String url = file.getUrl();
            if (!TextUtils.isEmpty(url)) {
                Glide.with(mActivity)
                        .load(url)
                        .placeholder(R.drawable.icon_default)
                        .error(R.drawable.head_icon)
                        .into(mineImg);
            }
        }


        setEditAble(mineName, false);
        setEditAble(mineSign, false);
        setEditAble(mineIdcard, false);


    }

    @Override
    protected void lazyLoadData() {

    }

    @Override
    public BasePresenter getPresenter() {
        return new MineFragmentPresenter();
    }


    @OnClick({R.id.recommend, R.id.about, R.id.btnRight})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recommend:
                Intent intent = new Intent(mActivity, RecommendActivity.class);
                startActivity(intent);
                break;
            case R.id.about:

                break;
            case R.id.btnRight:
                isSave = !isSave;
                showEdit();
                break;
        }
    }

    private void showEdit() {
        if (isSave) {
            btnRight.setBackgroundResource(R.drawable.submit);
            setEditAble(mineName, true);
            setEditAble(mineSign, true);
            setEditAble(mineIdcard, true);
            recommend.setVisibility(View.GONE);
            about.setVisibility(View.GONE);
            mineName.setHint(R.string.inputname);
            mineSign.setHint(R.string.inputsgn);
            mineIdcard.setHint(R.string.inputidcard);

        } else {
            saveUserData();
            btnRight.setBackgroundResource(R.drawable.bianji);
            setEditAble(mineName, false);
            setEditAble(mineSign, false);
            setEditAble(mineIdcard, false);
            recommend.setVisibility(View.VISIBLE);
            about.setVisibility(View.VISIBLE);

        }
    }

    /**
     * 保存数据
     */
    private void saveUserData() {
        String name = mineName.getText().toString();
        String sign = mineSign.getText().toString();
        String idcard = mineIdcard.getText().toString();
        ((MineFragmentPresenter) mPresenter).upDataUserMessage(mActivity, name, sign, idcard);

    }

    @Override
    public void setIdCard(String idCard) {
        mineIdcard.setText(idCard);
    }

    @Override
    public void showMessage(String msg) {
        ToastUtils.showToast(mActivity, msg);
    }

    private void setEditAble(EditText editText, boolean is) {
        if (is) {
            editText.setFocusableInTouchMode(true);
            editText.setFocusable(true);
            editText.requestFocus();
        } else {
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
        }
    }


    @Override
    public void showLoadProgressDialog(String str) {

    }

    @Override
    public void dissDialog() {

    }

    /**
     * 弹出POP样式对话框 选择拍照 选择本地图片
     */
    protected void showChooseIMG_WAYDialog() {
        dialog_choose_img_way = new Dialog(mActivity, R.style.MyDialogStyle_top);
        dialog_choose_img_way.setContentView(R.layout.dialog_choose_img);
        dialog_choose_img_way.setCanceledOnTouchOutside(true);
        dialog_choose_img_way.findViewById(R.id.other_view).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog_choose_img_way.cancel();
            }
        });
        // 取消
        dialog_choose_img_way.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog_choose_img_way.cancel();
            }
        });
        // 拍照
        dialog_choose_img_way.findViewById(R.id.choose_by_camera).setOnClickListener(new View.OnClickListener() {

            @TargetApi(23)
            @Override
            public void onClick(View v) {
                dialog_choose_img_way.cancel();
                MPermissions.requestPermissions(MineFragment.this, Common.REQUECT_CAMERA, Manifest.permission.CAMERA);

//                if (Build.VERSION.SDK_INT >= 23) {
//                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                        requestPermissions(new String[] { Manifest.permission.CAMERA }, 1);
//
//                    } else {
//                        camera();
//                    }
//                } else {
//                    camera();
//                }

            }
        });
        // 本地上传
        dialog_choose_img_way.findViewById(R.id.choose_by_local).setOnClickListener(new View.OnClickListener() {

            @TargetApi(23)
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                dialog_choose_img_way.cancel();
                MPermissions.requestPermissions(MineFragment.this, Common.REQUECT_READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            }
        });
        dialog_choose_img_way.show();
    }

    @PermissionGrant(Common.REQUECT_READ_EXTERNAL_STORAGE)
    public void requestReadStorageSuccess() {
//调用本地相册
    }

    @PermissionDenied(Common.REQUECT_READ_EXTERNAL_STORAGE)
    public void requestReadStorageFailed() {
    }

    @PermissionGrant(Common.REQUECT_CAMERA)
    public void requestCameraSuccess() {
//调用系统相机
    }

    @PermissionDenied(Common.REQUECT_CAMERA)
    public void requestCameraFailed() {
    }
}
