package org.jun1or.imgsel.callback;

import android.content.Context;
import android.widget.ImageView;

/**
 * @author cwj
 */
public interface ImageLoader {
    /**
     * 显示图片
     *
     * @param context
     * @param path
     * @param imageView
     */
    void displayImage(Context context, String path, ImageView imageView);
}
