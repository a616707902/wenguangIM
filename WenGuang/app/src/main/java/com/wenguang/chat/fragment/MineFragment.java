package com.wenguang.chat.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wenguang.chat.R;
import com.wenguang.chat.activity.RecommendActivity;
import com.wenguang.chat.base.BaseFragment;
import com.wenguang.chat.bean.User;
import com.wenguang.chat.common.UserManager;
import com.wenguang.chat.mvp.presenter.BasePresenter;
import com.wenguang.chat.mvp.presenter.MineFragmentPresenter;
import com.wenguang.chat.mvp.view.MineFragmentView;
import com.wenguang.chat.utils.ToastUtils;
import com.wenguang.chat.widget.CircleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
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
        BmobFile file= user.getMinepic();
        if (file!=null){
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



    @OnClick({R.id.recommend, R.id.about,R.id.btnRight})
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
        String name=mineName.getText().toString();
        String sign=mineSign.getText().toString();
        String idcard=mineIdcard.getText().toString();
        ((MineFragmentPresenter)mPresenter).upDataUserMessage(mActivity,name,sign,idcard);

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



}
