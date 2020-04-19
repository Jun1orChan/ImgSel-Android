package org.jun1or.imgsel.callback;

import android.content.Context;
import android.widget.ImageView;

public interface ImageLoader {
    void displayImage(Context context, String path, ImageView imageView);
}
