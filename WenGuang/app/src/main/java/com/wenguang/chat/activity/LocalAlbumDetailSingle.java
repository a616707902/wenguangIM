package com.wenguang.chat.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wenguang.chat.R;
import com.wenguang.chat.common.AppManager;
import com.wenguang.chat.common.Common;
import com.wenguang.chat.common.MyApplication;
import com.wenguang.chat.utils.LocalImageHelper;
import com.wenguang.chat.widget.AlbumViewPager;
import com.wenguang.chat.widget.MatrixImageView;

import java.util.List;

/**
 * @类名: LocalAlbumDetailSingle
* @作者: chenpan
* @时间: 2016-9-26
* @最后修改者:
* @最后修改内容:
 */
public class LocalAlbumDetailSingle extends AppCompatActivity implements
		MatrixImageView.OnSingleTapListener, View.OnClickListener {

	GridView gridView;
	TextView title;// 标题
	View titleBar;// 标题栏
	View pagerContainer;// 图片显示部分
	TextView  headerFinish;
	AlbumViewPager viewpager;// 大图显示pager
	String folder;
	List<LocalImageHelper.LocalFile> currentFolder = null;

	ImageView mBackView;
	View headerBar;
	// CheckBox checkBox;
	LocalImageHelper helper = LocalImageHelper.getInstance();
	LocalImageHelper.LocalFile localFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local_album_detail_single);
		if (!LocalImageHelper.getInstance().isInited()) {
			finish();
			return;
		}
		title = (TextView) findViewById(R.id.album_title);
		//finish = (TextView) findViewById(R.id.album_finish);
		headerFinish = (TextView) findViewById(R.id.header_finish);
		gridView = (GridView) findViewById(R.id.gridview);
		titleBar = findViewById(R.id.album_title_bar);
		viewpager = (AlbumViewPager) findViewById(R.id.albumviewpager);
		pagerContainer = findViewById(R.id.pagerview);
		viewpager.setOnPageChangeListener(pageChangeListener);
		viewpager.setOnSingleTapListener(this);
		mBackView = (ImageView) findViewById(R.id.header_bar_photo_back);
		headerBar = findViewById(R.id.album_item_header_bar);
		mBackView.setOnClickListener(this);
		//finish.setOnClickListener(this);
		headerFinish.setOnClickListener(this);
		findViewById(R.id.album_back).setOnClickListener(this);

		folder = getIntent().getExtras()
				.getString(Common.LOCAL_FOLDER_NAME);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 防止停留在本界面时切换到桌面，导致应用被回收，图片数组被清空，在此处做一个初始化处理
				helper.initImage();
				// 获取该文件夹下地所有文件
				final List<LocalImageHelper.LocalFile> folders = helper
						.getFolder(folder);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (folders != null) {
							currentFolder = folders;
							MyAdapter adapter = new MyAdapter(
									LocalAlbumDetailSingle.this, folders);
							title.setText(folder);
							gridView.setAdapter(adapter);

							//finish.setText("完成");
							// finish.setEnabled(false);
							headerFinish.setText("完成");
							// headerFinish.setEnabled(false);

						}
					}
				});
			}
		}).start();
		// checkedItems = helper.getCheckedItems();
		localFile=helper.getLocalfile();
		LocalImageHelper.getInstance().setResultOk(false);
	}
/**
 * @方法名称: showViewPager
* @方法详述:显示大图 
* @参数: null
* @返回值: void
* @异常抛出 Exception:
* @异常抛出 NullPointerException:
 */
	private void showViewPager(int index) {
		pagerContainer.setVisibility(View.VISIBLE);
		gridView.setVisibility(View.GONE);
		findViewById(R.id.album_title_bar).setVisibility(View.GONE);
		viewpager
				.setAdapter(viewpager.new LocalViewPagerAdapter(currentFolder));
		viewpager.setCurrentItem(index);

		AnimationSet set = new AnimationSet(true);
		ScaleAnimation scaleAnimation = new ScaleAnimation((float) 0.9, 1,
				(float) 0.9, 1, pagerContainer.getWidth() / 2,
				pagerContainer.getHeight() / 2);
		scaleAnimation.setDuration(300);
		set.addAnimation(scaleAnimation);
		AlphaAnimation alphaAnimation = new AlphaAnimation((float) 0.1, 1);
		alphaAnimation.setDuration(200);
		set.addAnimation(alphaAnimation);
		pagerContainer.startAnimation(set);
	}
/**
 * @方法名称: hideViewPager
* @方法详述:隐藏大图 
* @参数: null
* @返回值: void
* @异常抛出 Exception:
* @异常抛出 NullPointerException:
 */
	private void hideViewPager() {
		pagerContainer.setVisibility(View.GONE);
		gridView.setVisibility(View.VISIBLE);
		findViewById(R.id.album_title_bar).setVisibility(View.VISIBLE);
		AnimationSet set = new AnimationSet(true);
		ScaleAnimation scaleAnimation = new ScaleAnimation(1, (float) 0.9, 1,
				(float) 0.9, pagerContainer.getWidth() / 2,
				pagerContainer.getHeight() / 2);
		scaleAnimation.setDuration(200);
		set.addAnimation(scaleAnimation);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setDuration(200);
		set.addAnimation(alphaAnimation);
		pagerContainer.startAnimation(set);
		((BaseAdapter) gridView.getAdapter()).notifyDataSetChanged();
	}

	private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			 if (viewpager.getAdapter() != null) {
	               localFile=currentFolder.get(position);
	            }
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public void onSingleTap() {
		if (headerBar.getVisibility() == View.VISIBLE) {
			AlphaAnimation animation = new AlphaAnimation(1, 0);
			animation.setDuration(300);
			headerBar.startAnimation(animation);
			headerBar.setVisibility(View.GONE);
		} else {
			headerBar.setVisibility(View.VISIBLE);
			AlphaAnimation animation = new AlphaAnimation(0, 1);
			animation.setDuration(300);
			headerBar.startAnimation(animation);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.header_bar_photo_back:
			hideViewPager();
			break;
		//case R.id.album_finish:
		case R.id.header_finish:
			localFile=currentFolder.get(viewpager.getCurrentItem());
			LocalImageHelper.getInstance().setLocalfile(localFile);
			AppManager.getAppManager().finishActivity(LocalAlbum.class);
			LocalImageHelper.getInstance().setResultOk(true);
			finish();
			break;
		case R.id.album_back:
			finish();
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (pagerContainer.getVisibility() == View.VISIBLE) {
			hideViewPager();
		} else {
			super.onBackPressed();
		}
	}
/**
 * @类名: MyAdapter
* @功能描述: 适配器
* @作者: chenpan
* @时间: 2016-9-26
* @最后修改者:
* @最后修改内容:
 */
	public class MyAdapter extends BaseAdapter {
		private Context m_context;
		private LayoutInflater miInflater;
		DisplayImageOptions options;
		List<LocalImageHelper.LocalFile> paths;

		public MyAdapter(Context context, List<LocalImageHelper.LocalFile> paths) {
			m_context = context;
			this.paths = paths;
			options = new DisplayImageOptions.Builder()
					.cacheInMemory(true)
					.cacheOnDisk(false)
					.showImageForEmptyUri(R.drawable.dangkr_no_picture_small)
					.showImageOnFail(R.drawable.dangkr_no_picture_small)
					.showImageOnLoading(R.drawable.dangkr_no_picture_small)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.setImageSize(
							new ImageSize(
									((MyApplication) context
											.getApplicationContext())
											.getQuarterWidth(), 0))
					.displayer(new SimpleBitmapDisplayer()).build();
		}

		@Override
		public int getCount() {
			return paths.size();
		}

		@Override
		public LocalImageHelper.LocalFile getItem(int i) {
			return paths.get(i);
		}

		@Override
		public long getItemId(int i) {
			return 0;
		}

		@Override
		public View getView(final int i, View convertView, ViewGroup viewGroup) {
			ViewHolder viewHolder = new ViewHolder();

			if (convertView == null || convertView.getTag() == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = getLayoutInflater();
				convertView = inflater.inflate(R.layout.simple_list_item, null);
				viewHolder.imageView = (ImageView) convertView
						.findViewById(R.id.imageView);
				 viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
				 viewHolder.checkBox.setVisibility(View.GONE);
				 convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			ImageView imageView = viewHolder.imageView;
			LocalImageHelper.LocalFile localFile = paths.get(i);
			// FrescoLoader.getInstance().localDisplay(localFile.getThumbnailUri(),
			// imageView, options);
			ImageLoader.getInstance().displayImage(localFile.getThumbnailUri(),
					new ImageViewAware(viewHolder.imageView), options,
					loadingListener, null, localFile.getOrientation());
			viewHolder.imageView.setTag(localFile);
			viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					
					showViewPager(i);
				}
			});
			return convertView;
		}

		private class ViewHolder {
			ImageView imageView;
			 CheckBox checkBox;
		}
	}

	SimpleImageLoadingListener loadingListener = new SimpleImageLoadingListener() {
		@Override
		public void onLoadingComplete(String imageUri, View view,
				final Bitmap bm) {
			if (TextUtils.isEmpty(imageUri)) {
				return;
			}
			// 由于很多图片是白色背景，在此处加一个#eeeeee的滤镜，防止checkbox看不清
			try {
				((ImageView) view).getDrawable().setColorFilter(
						Color.argb(0xff, 0xee, 0xee, 0xee),
						PorterDuff.Mode.MULTIPLY);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

}
