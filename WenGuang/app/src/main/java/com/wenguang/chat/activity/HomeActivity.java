package com.wenguang.chat.activity;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wenguang.chat.R;
import com.wenguang.chat.base.BaseActivity;
import com.wenguang.chat.fragment.ContactFragment;
import com.wenguang.chat.fragment.HomeFragment;
import com.wenguang.chat.fragment.MessageFragment;
import com.wenguang.chat.fragment.MineFragment;
import com.wenguang.chat.mvp.presenter.BasePresenter;
import com.wenguang.chat.mvp.presenter.HomePresenter;
import com.wenguang.chat.mvp.view.HomeView;

import butterknife.Bind;

public class HomeActivity extends BaseActivity implements HomeView {

    HomeFragment mHomeFragment;
    ContactFragment mContactFragment;
    MessageFragment mMessageFragment;
    MineFragment mMineFragment;

    @Bind(R.id.frame)
    FrameLayout mFrame;
    @Bind(R.id.radio_home)
    RadioButton mRadioHome;
    @Bind(R.id.radio_contact)
    RadioButton mRadioContact;
    @Bind(R.id.radio_message)
    RadioButton mRadioMessage;
    @Bind(R.id.radio_mine)
    RadioButton mRadioMine;
    @Bind(R.id.menu_group)
    RadioGroup mMenuGroup;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initInjector() {
        showFragment(1);

    }

    @Override
    protected void initEventAndData() {
        mMenuGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switchTab(i);
            }
        });
    }

    public void switchTab(int id) {
        switch (id) {
            case R.id.radio_home:
                showFragment(1);
                break;
            case R.id.radio_contact:
                showFragment(2);
                break;
            case R.id.radio_message:
                showFragment(3);
                break;
            case R.id.radio_mine:
                showFragment(4);
                break;
        }
    }

    private void showFragment(int index) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
        hideFragment(ft);
        switch (index) {
            case 1:
                if (mHomeFragment != null)
                    ft.show(mHomeFragment);
                else {
                    mHomeFragment = new HomeFragment();
                    ft.add(R.id.frame, mHomeFragment);
                }
                break;
            case 2:
                if (mContactFragment != null) {
                    ft.show(mContactFragment);
                } else {
                    mContactFragment = new ContactFragment();
                    ft.add(R.id.frame, mContactFragment);
                }
                break;
            case 3:
                if (mMessageFragment != null) {
                    ft.show(mMessageFragment);
                } else {
                    mMessageFragment = new MessageFragment();
                    ft.add(R.id.frame, mMessageFragment);
                }
                break;
            case 4:
                if (mMineFragment != null) {
                    ft.show(mMineFragment);
                } else {
                    mMineFragment = new MineFragment();
                    ft.add(R.id.frame, mMineFragment);
                }
                break;
        }
        ft.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction ft) {
        if (mHomeFragment != null)
            ft.hide(mHomeFragment);
        if (mContactFragment != null)
            ft.hide(mContactFragment);
        if (mMessageFragment != null)
            ft.hide(mMessageFragment);
        if (mMineFragment != null)
            ft.hide(mMineFragment);
    }

    @Override
    public BasePresenter getPresenter() {
        return new HomePresenter();
    }

}