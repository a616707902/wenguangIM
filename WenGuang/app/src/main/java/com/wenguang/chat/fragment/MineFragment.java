package com.wenguang.chat.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.wenguang.chat.R;
import com.wenguang.chat.activity.ImagesDetailActivity;
import com.wenguang.chat.activity.LocalAlbum;
import com.wenguang.chat.activity.RecommendActivity;
import com.wenguang.chat.base.BaseFragment;
import com.wenguang.chat.bean.User;
import com.wenguang.chat.common.Common;
import com.wenguang.chat.common.UserManager;
import com.wenguang.chat.mvp.presenter.BasePresenter;
import com.wenguang.chat.mvp.presenter.MineFragmentPresenter;
import com.wenguang.chat.mvp.view.MineFragmentView;
import com.wenguang.chat.utils.ImageUtils;
import com.wenguang.chat.utils.LocalImageHelper;
import com.wenguang.chat.utils.ToastUtils;
import com.wenguang.chat.widget.CircleImageView;
import com.wenguang.chat.widget.LoadProgressDialog;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.io.File;

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
    @Bind(R.id.edit_img)
    ImageView editImg;
    /**
     * true为编辑状态
     */
    private boolean isSave;
    /*选择照片对话框*/
    private Dialog dialog_choose_img_way;
    DisplayImageOptions options;

    private LoadProgressDialog progressDialog;
    String url;


    @Override
    protected int getlayoutId() {
        return R.layout.mine_fragment;
    }

    @Override
    protected void initInjector() {
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(false)
                .showImageForEmptyUri(R.drawable.icon_default).showImageOnFail(R.drawable.head_icon)
                .showImageOnLoading(R.drawable.dangkr_no_picture_small).bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer()).build();
    }

    @Override
    protected void initEventAndData() {
        LocalImageHelper.getInstance().setLocalfile(null);
        User user = UserManager.getInstance().getUser();
        mineName.setText(user.getName());
        mineSign.setText(user.getMinesign());
        mineAccount.setText(user.getAccount());
        mineIdcard.setText(user.getIdcard());
        BmobFile file = user.getMinepic();
        if (file != null) {
            url = file.getUrl();
            if (!TextUtils.isEmpty(url)) {
//                Glide.with(mActivity)
//                        .load(url)
//                        .placeholder(R.drawable.icon_default)
//                        .error(R.drawable.head_icon)
//                        .into(mineImg);
                ImageLoader.getInstance().displayImage(url, mineImg);
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


    @OnClick({R.id.recommend, R.id.about, R.id.btnRight, R.id.edit_img, R.id.mine_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recommend:
                Intent intent = new Intent(mActivity, RecommendActivity.class);
                startActivity(intent);
                break;
            case R.id.about:

                break;
            case R.id.mine_img:
                showBigPic(mineImg);
                break;
            case R.id.btnRight:
                isSave = !isSave;
                showEdit();

                break;
            case R.id.edit_img:
                showChooseIMG_WAYDialog();
                break;
        }
    }

    /**
     * 显示大图
     */
    private void showBigPic(View view) {
        Rect frame = new Rect();
        //这里可能会出错
        ((Activity) mActivity).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int[] location = new int[2];
        view.getLocationOnScreen(location);
        location[1] += statusBarHeight;

        int width = view.getWidth();
        int height = view.getHeight();
        Bundle extras = new Bundle();
        extras.putString(ImagesDetailActivity.INTENT_IMAGE_URL_TAG, url);
        extras.putInt(ImagesDetailActivity.INTENT_IMAGE_X_TAG, location[0]);
        extras.putInt(ImagesDetailActivity.INTENT_IMAGE_Y_TAG, location[1]);
        extras.putInt(ImagesDetailActivity.INTENT_IMAGE_W_TAG, width);
        extras.putInt(ImagesDetailActivity.INTENT_IMAGE_H_TAG, height);
             /*   readyGo(ImagesDetailActivity.class, extras);
                getActivity().overridePendingTransition(0, 0);*/
        Intent intent = new Intent(mActivity, ImagesDetailActivity.class);
        intent.putExtras(extras);
        mActivity.startActivity(intent);
        (((Activity) mActivity)).overridePendingTransition(0, 0);
    }

    private void showEdit() {
        if (isSave) {
            btnRight.setBackgroundResource(R.drawable.submit);
            setEditAble(mineName, true);
            setEditAble(mineSign, true);
            setEditAble(mineIdcard, true);
            editImg.setVisibility(View.VISIBLE);
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
            editImg.setVisibility(View.GONE);
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
        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(sign) && TextUtils.isEmpty(idcard) && LocalImageHelper.getInstance().getLocalfile() == null) {

        } else {
            ((MineFragmentPresenter) mPresenter).upDataUserMessage(mActivity, name, sign, idcard, LocalImageHelper.getInstance().getLocalfile().getPath());
            showLoadProgressDialog("更新中...");
        }

    }

    @Override
    public void setIdCard(String idCard) {
        mineIdcard.setText(idCard);
    }

    @Override
    public void showMessage(String msg) {

        dissDialog();

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
        progressDialog = LoadProgressDialog.getInstance(mActivity);
        progressDialog.setMessage(str);
        progressDialog.show();
    }

    @Override
    public void dissDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();

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
        gallery();
//调用本地相册
    }

    @PermissionDenied(Common.REQUECT_READ_EXTERNAL_STORAGE)
    public void requestReadStorageFailed() {
    }

    @PermissionGrant(Common.REQUECT_CAMERA)
    public void requestCameraSuccess() {
//调用系统相机
        camera();
    }

    @PermissionDenied(Common.REQUECT_CAMERA)
    public void requestCameraFailed() {
    }

    /**
     * @param
     * @return
     * @throws NullPointerException
     * @功能描述：拍照，调用相机
     */
    protected void camera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 拍照后保存图片的绝对路径
        String cameraPath = LocalImageHelper.getInstance().setCameraImgPath();
        File file = new File(cameraPath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, Common.REQUEST_CODE_GETIMAGE_BYCAMERA);
    }

    /**
     * @param
     * @return
     * @throws NullPointerException
     * @功能描述：从本地选择照片
     */
    protected void gallery() {
        Intent intent = new Intent(mActivity, LocalAlbum.class);
        intent.putExtra("single", true);
        startActivityForResult(intent, Common.REQUEST_CODE_GETIMAGE_BYCROP);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case Common.REQUEST_CODE_GETIMAGE_BYCAMERA:
                String cameraPath = LocalImageHelper.getInstance().getCameraImgPath();
                if (TextUtils.isEmpty(cameraPath)) {
                    Toast.makeText(mActivity, "图片获取失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                File file = new File(cameraPath);
                if (file.exists()) {
                    Uri uri = Uri.fromFile(file);
                    LocalImageHelper.LocalFile localFile = new LocalImageHelper.LocalFile();
                    localFile.setThumbnailUri(uri.toString());
                    localFile.setOriginalUri(uri.toString());
                    localFile.setOrientation(ImageUtils.getBitmapDegree(cameraPath));
                    // LocalImageHelper.getInstance().getCheckedItems().add(localFile);
                    LocalImageHelper.getInstance().setLocalfile(localFile);
                    LocalImageHelper.getInstance().setResultOk(true);

                    // 这里本来有个弹出progressDialog的，在拍照结束后关闭，但是要延迟1秒，原因是由于三星手机的相机会强制切换到横屏，
                    // 此处必须等它切回竖屏了才能结束，否则会有异常
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // finish();
                        }
                    }, 1000);
                } else {
                    Toast.makeText(mActivity, "图片获取失败", Toast.LENGTH_SHORT).show();
                }
            case Common.REQUEST_CODE_GETIMAGE_BYCROP:
                if (LocalImageHelper.getInstance().isResultOk()) {
                    LocalImageHelper.getInstance().setResultOk(false);

                    ImageLoader.getInstance().displayImage(LocalImageHelper.getInstance().getLocalfile().getThumbnailUri(),
                            new ImageViewAware(mineImg), options, null, null,
                            LocalImageHelper.getInstance().getLocalfile().getOrientation());

                    // 清空选中的图片
                    // files.clear();

                }
                // 清空选中的图片
                LocalImageHelper.getInstance().getCheckedItems().clear();
                break;

            default:
                break;
        }
        // }
    }
}
