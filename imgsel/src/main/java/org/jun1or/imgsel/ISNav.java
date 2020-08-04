package org.jun1or.imgsel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import org.jun1or.imgsel.callback.ImageLoader;
import org.jun1or.imgsel.image.ImageSelectActivity;


/**
 * @author cwj
 */
public class ISNav {

    private static ISNav instance;

    private ImageLoader loader;

    public static ISNav getInstance() {
        if (instance == null) {
            synchronized (ISNav.class) {
                if (instance == null) {
                    instance = new ISNav();
                }
            }
        }
        return instance;
    }

    /**
     * 图片加载必须先初始化
     *
     * @param loader
     */
    public void init(ImageLoader loader) {
        this.loader = loader;
    }

    public void displayImage(Context context, String path, ImageView imageView) {
        if (loader != null) {
            loader.displayImage(context, path, imageView);
        }
    }

    public void toImageSelectActivity(Object source, ImageConfig config, int reqCode) {
        if (source instanceof Activity) {
            Intent intent = new Intent((Activity) source, ImageSelectActivity.class);
            intent.putExtra(ImageSelectActivity.KEY_config, config);
            ((Activity) source).startActivityForResult(intent, reqCode);
        } else if (source instanceof Fragment) {
            Intent intent = new Intent(((Fragment) source).getActivity(), ImageSelectActivity.class);
            intent.putExtra(ImageSelectActivity.KEY_config, config);
            ((Fragment) source).startActivityForResult(intent, reqCode);
        } else if (source instanceof android.app.Fragment) {
            Intent intent = new Intent(((android.app.Fragment) source).getActivity(), ImageSelectActivity.class);
            intent.putExtra(ImageSelectActivity.KEY_config, config);
            ((android.app.Fragment) source).startActivityForResult(intent, reqCode);
        }
    }
}
