package com.wenguang.chat.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wenguang.chat.R;
import com.wenguang.chat.base.BaseFragment;
import com.wenguang.chat.mvp.presenter.BasePresenter;

public class MineFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_fragment, null);
		return view;
	}

	@Override
	protected int getlayoutId() {
		return 0;
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
		return null;
	}
}
