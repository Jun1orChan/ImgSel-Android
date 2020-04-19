package org.jun1or.imgsel.image;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.istrong.dialog.MaterialDialog;
import com.istrong.imgsel.R;
import org.jun1or.imgsel.ImageConfig;
import org.jun1or.imgsel.bean.Image;
import com.istrong.util.AppUtil;
import com.istrong.util.StatusBarUtil;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.ArrayList;
import java.util.List;

public class ImageSelectActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String KEY_config = "config";
    public static final String KEY_result = "result";
    public static final String KEY_media_selected = "media_selected";


    private MaterialDialog mPermissionDeniedDialog;

    private TextView mTvSelected;

    private ImageConfig mISConfig;

    private ArrayList<String> mSelectedImageList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mISConfig = getIntent().getParcelableExtra(KEY_config);
        if (mISConfig == null) {
            mISConfig = new ImageConfig.Builder().build();
        }
        mSelectedImageList = mISConfig.mImgList;
        if (mISConfig.mMode == 0) {
            StatusBarUtil.setStatusBarDarkMode(this);
        } else {
            StatusBarUtil.setStatusBarLightMode(this);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imgsel_activity_image_select);
        initTopBar();
        getPermission();
    }

    private void getPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        //授权
                        ImageSelectFragment imageSelectFragment = ImageSelectFragment.instance();
                        Bundle bundle = imageSelectFragment.getArguments();
                        bundle.putParcelable(ImageSelectFragment.KEY_Config, mISConfig);
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.fmContainer, imageSelectFragment, null)
                                .commit();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        //显示未授权提示框
                        showPermissionDeniedDialog();
                    }
                })
                .start();
    }

    private void initTopBar() {
        ViewGroup topBar = (ViewGroup) findViewById(R.id.topBar);
        if (mISConfig.mTopBarBgColor != Color.TRANSPARENT) {
            topBar.setBackgroundColor(mISConfig.mTopBarBgColor);
        }
        TextView tvTitle = (TextView) topBar.findViewById(R.id.tvTitle);
        tvTitle.setTextColor(mISConfig.mTextColor);
        tvTitle.setText(mISConfig.mTitle);
        if (mISConfig.mTitleBold) {
            tvTitle.setTypeface(tvTitle.getTypeface(), Typeface.BOLD);
        }
        ImageView imgBack = (ImageView) topBar.findViewById(R.id.imgBack);
        imgBack.setImageResource(mISConfig.mBackImageRes);
        imgBack.setOnClickListener(this);
        mTvSelected = (TextView) topBar.findViewById(R.id.tvRight);
        mTvSelected.setTextColor(mISConfig.mTextColor);
        if (mISConfig.mMultiSelect) {
            mTvSelected.setText(String.format(getString(R.string.imgsel_has_selected), mSelectedImageList == null ? 0 : mSelectedImageList.size(), mISConfig.mMaxNum));
        } else {
            mTvSelected.setText(getString(R.string.imgsel_selected));
        }
        mTvSelected.setOnClickListener(this);
    }

    public void setChoiceText(List<Image> imageList) {
        mSelectedImageList = convert(imageList);
        if (mISConfig.mMultiSelect) {
            mTvSelected.setText(String.format(getString(R.string.imgsel_has_selected), imageList.size(), mISConfig.mMaxNum));
        } else {
            //单选无操作
        }

    }

    private ArrayList<String> convert(List<Image> imageList) {
        ArrayList<String> imagePathList = new ArrayList<>();
        if (imageList == null) {
            return imagePathList;
        }
        for (Image image : imageList) {
            imagePathList.add(image.path);
        }
        return imagePathList;
    }

    private void showPermissionDeniedDialog() {
        if (mPermissionDeniedDialog == null)
            mPermissionDeniedDialog = new MaterialDialog();
        mPermissionDeniedDialog
                .content(String.format(getString(R.string.imgsel_storage_permission_denied_tips),
                        AppUtil.getAppName(this), AppUtil.getAppName(this)))
                .btnText(getString(R.string.imgsel_btn_text_denied_cancel), getString(R.string.imgsel_btn_text_denied_setting))
                .btnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPermissionDeniedDialog.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPermissionDeniedDialog.dismiss();
                        AppUtil.goAppDetailsSettings(ImageSelectActivity.this);
                        finish();
                    }
                })
                .show(getSupportFragmentManager());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.imgBack) {
            finish();
        } else if (id == R.id.tvRight) {
            resultData();
        }
    }

    private void resultData() {
        Intent intent = new Intent();
        if (mSelectedImageList == null)
            mSelectedImageList = new ArrayList();
        intent.putParcelableArrayListExtra(KEY_result, (ArrayList) mSelectedImageList);
        setResult(RESULT_OK, intent);
        finish();
    }
}
