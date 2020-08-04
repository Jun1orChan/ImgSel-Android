package org.jun1or.mediaselect_android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jun1or.imgsel.ISNav;
import org.jun1or.imgsel.ImageConfig;
import org.jun1or.imgsel.callback.ImageLoader;
import org.jun1or.imgsel.image.ImageSelectActivity;
import org.jun1or.mediaselect_android.glide.GlideApp;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                GlideApp.with(context).load(path).into(imageView);
            }
        });
//        mLoadingDialog = new LoadingDialog();
//        mLoadingDialog.show(getSupportFragmentManager());
//        mLoadingDialog.dismiss();
//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mLoadingDialog.dismiss();
//            }
//        }, 1000);
    }

    public void goImageSingleSelectActivity(View view) {
        ImageConfig config = new ImageConfig.Builder()
                .textColor(Color.RED)
                .backImageRes(R.mipmap.imgsel_topbar_back)
                .title("测试标题")
                .multiSelect(false)
                .titleBold(true)
                .mode(0)
                .topBarBgColor(Color.WHITE)
                .build();
        Log.e("TAG", "===========" + Color.WHITE);
        ISNav.getInstance().toImageSelectActivity(this, config, 100);
    }

    public void goImageMultiSelectActivity(View view) {
        ArrayList<String> images = new ArrayList<>();
        TextView tvImages = (TextView) findViewById(R.id.tvImages);
        if (!TextUtils.isEmpty(tvImages.getText())) {
            String[] paths = tvImages.getText().toString().split(",\n");
            for (int i = 0; i < paths.length; i++) {
                images.add(paths[i]);
            }
        }
        ImageConfig config = new ImageConfig.Builder()
                .maxNum(5)
                .title("测试标题")
                .multiSelect(true)
                .mode(1)
                .selectedList(images)
                .build();
        ISNav.getInstance().toImageSelectActivity(this, config, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
//        mLoadingDialog.show(getSupportFragmentManager());
        LinearLayout llImageContainer = (LinearLayout) findViewById(R.id.llImageContainer);
        llImageContainer.removeAllViewsInLayout();
        List<String> imageList = data.getStringArrayListExtra(ImageSelectActivity.KEY_result);
        TextView tvImages = (TextView) findViewById(R.id.tvImages);
        String str = "";
        for (String path : imageList) {
            str += path + ",\n";
            ImageView imageView = new ImageView(this);
            llImageContainer.addView(imageView);
            GlideApp.with(this).load(path).into(imageView);
        }
        tvImages.setText(str);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("TAG", "=============onSaveInstanceState");
    }
}
