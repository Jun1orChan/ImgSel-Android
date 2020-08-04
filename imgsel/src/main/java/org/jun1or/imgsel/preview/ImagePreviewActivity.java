package org.jun1or.imgsel.preview;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import org.jun1or.imgsel.ImageConfig;
import org.jun1or.imgsel.R;
import org.jun1or.imgsel.bean.Image;
import org.jun1or.util.DisplayUtil;
import org.jun1or.util.StatusBarUtil;

import java.util.List;


/**
 * @author cwj
 */
public class ImagePreviewActivity extends AppCompatActivity implements ImageViewPagerAdapter.OnImageClickListener,
        ViewPager.OnPageChangeListener, View.OnClickListener {

    public static String KEY_IMAGELIST = "image_list";
    public static String KEY_index = "index";
    public static String KEY_config = "config";

    public static List<Image> sImageList;

    private ImageConfig mISConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mISConfig = getIntent().getParcelableExtra(KEY_config);
        if (mISConfig == null) {
            mISConfig = new ImageConfig.Builder().build();
        }
        List<Image> imageList = getIntent().getParcelableArrayListExtra(KEY_IMAGELIST);
        if (imageList != null) {
            sImageList = imageList;
        }
        if (mISConfig.mMode == 0) {
            StatusBarUtil.setStatusBarDarkMode(this);
        } else {
            StatusBarUtil.setStatusBarLightMode(this);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imgsel_activity_image_preview);
        initTopBar();
        ViewPager vpImage = (ViewPager) findViewById(R.id.vpIamge);
        vpImage.setAdapter(new ImageViewPagerAdapter(sImageList, this));
        vpImage.setCurrentItem(getIntent().getIntExtra(KEY_index, 0));
        vpImage.addOnPageChangeListener(this);
    }

    private void initTopBar() {
        ViewGroup topBar = findViewById(R.id.topBar);
        if (mISConfig.mTopBarBgColor != Color.TRANSPARENT) {
            topBar.setBackgroundColor(mISConfig.mTopBarBgColor);
        }
        findViewById(R.id.tvRight).setVisibility(View.GONE);
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setImageResource(mISConfig.mBackImageRes);
        imgBack.setOnClickListener(this);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setTextColor(mISConfig.mTextColor);
        onPageSelected(getIntent().getIntExtra(KEY_index, 0));
    }

    @Override
    public void onImageClick(Image image) {
        if (DisplayUtil.isFullScreen(this)) {
            DisplayUtil.cancelFullScreen(this);
            //显示topbar
            findViewById(R.id.topBar).startAnimation(AnimationUtils.loadAnimation(this, R.anim.imgsel_topbar_show));
        } else {
            DisplayUtil.setFullScreen(this);
            //隐藏topbar
            findViewById(R.id.topBar).startAnimation(AnimationUtils.loadAnimation(this, R.anim.imgsel_topbar_hide));
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (sImageList == null || sImageList.size() == 0) {
            return;
        }
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText((position + 1) + "/" + sImageList.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.imgBack) {
            finish();
        }
    }
}
