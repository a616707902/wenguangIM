package com.wenguang.chat.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenguang.chat.R;
import com.wenguang.chat.base.BaseFragment;
import com.wenguang.chat.mvp.presenter.BasePresenter;
import com.wenguang.chat.mvp.presenter.MessageFragmentPresenter;

public class MessageFragment extends BaseFragment {


	@Override
	protected int getlayoutId() {
		return R.layout.message_fragment;
	}

	@Override
	protected void initInjector() {

	}

	@Override
	protected void initEventAndData() {

	}

	@Override
	protected void lazyLoadData() {

	}

	@Override
	public BasePresenter getPresenter() {
		return new MessageFragmentPresenter();
	}
}
