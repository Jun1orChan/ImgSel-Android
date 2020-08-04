package org.jun1or.imgsel.preview;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import org.jun1or.imgsel.ISNav;
import org.jun1or.imgsel.bean.Image;
import org.jun1or.widget.image.PhotoView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cwj
 */
public class ImageViewPagerAdapter extends PagerAdapter {

    private List<Image> mImageList;
    private OnImageClickListener mOnImageClickListener;

    public ImageViewPagerAdapter(List<Image> imageList, OnImageClickListener onImageClickListener) {
        mImageList = imageList == null ? new ArrayList<Image>() : imageList;
        this.mOnImageClickListener = onImageClickListener;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        PhotoView photoView = new PhotoView(container.getContext());
        photoView.setAdjustViewBounds(true);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        photoView.setLayoutParams(layoutParams);
        photoView.enable();
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnImageClickListener != null) {
                    mOnImageClickListener.onImageClick(mImageList.get(position));
                }
            }
        });
        ISNav.getInstance().displayImage(container.getContext(), mImageList.get(position).path, photoView);
        container.addView(photoView);
        return photoView;
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public interface OnImageClickListener {
        void onImageClick(Image image);
    }
}
