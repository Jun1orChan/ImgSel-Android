package org.jun1or.imgsel.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;


import org.jun1or.imgsel.ISNav;
import org.jun1or.imgsel.ImageConfig;
import org.jun1or.imgsel.R;
import org.jun1or.imgsel.bean.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cwj
 */
public class ImageListRecAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int TYPE_CAMERA = 0;
    private static int TYPE_MEDIA = 1;

    private List<Image> mImageList = new ArrayList<>();

    private List<Image> mCheckedList = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;

    private ImageConfig mISConfig;
    private int mLastChoiceIndex = -1;


    public void setCheckedList(List<Image> checkedList) {
        mCheckedList = checkedList == null ? new ArrayList<Image>() : checkedList;
    }

    public void setISConfig(ImageConfig isConfig) {
        mISConfig = isConfig == null ? new ImageConfig.Builder().build() : isConfig;
    }

    public List<Image> getCheckedList() {
        return mCheckedList;
    }

    public List<Image> getImageList() {
        return this.mImageList;
    }


    public void setImageList(List<Image> imageList) {
        this.mImageList = imageList == null ? new ArrayList<Image>() : imageList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MediaItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.imgsel_item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final MediaItemViewHolder mediaItemViewHolder = (MediaItemViewHolder) holder;
        final Image image = mImageList.get(position);
        ISNav.getInstance().displayImage(holder.itemView.getContext(), image.path, mediaItemViewHolder.imgThumb);
        mediaItemViewHolder.imgThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onMediaClick(image, position);
                }
            }
        });
        if (isContainsImage(mCheckedList, image)) {
            mediaItemViewHolder.imgCheck.setImageResource(R.mipmap.imgsel_checked);
            mediaItemViewHolder.imgThumb.setColorFilter(Color.argb(130, 0, 0, 0),
                    PorterDuff.Mode.SRC_ATOP
            );
        } else {
            mediaItemViewHolder.imgCheck.setImageResource(R.mipmap.imgsel_uncheck);
            mediaItemViewHolder.imgThumb.setColorFilter(Color.argb(50, 0, 0, 0),
                    PorterDuff.Mode.SRC_ATOP
            );
        }
        mediaItemViewHolder.imgCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isContainsImage(mCheckedList, image)) {
                    removeImage(mCheckedList, image);
                } else {
                    if (mCheckedList.size() >= mISConfig.mMaxNum) {
                        //超过最大选择数量
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onReachMaxNum(mISConfig.mMaxNum);
                        }
                        return;
                    }
                    if (!mISConfig.mMultiSelect) {
                        mCheckedList.clear();
                        notifyItemChanged(mLastChoiceIndex);
                    }
                    mCheckedList.add(image);
                }
                mLastChoiceIndex = position;
                notifyItemChanged(mLastChoiceIndex);
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onMediaCheckClick(mCheckedList, image);
                }
            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        return TYPE_MEDIA;
    }

    /**
     * 不能直接使用contains方法，多次重新创建，对象是不同的
     *
     * @param imageList
     * @param image
     * @return
     */
    private boolean isContainsImage(List<Image> imageList, Image image) {
        for (Image checkedImage : imageList) {
            if (image.path.equals(checkedImage.path)) {
                //相同
                return true;
            }
        }
        return false;
    }

    private void removeImage(List<Image> imageList, Image image) {
        int size = imageList.size();
        for (int i = 0; i < size; i++) {
            Image checkedImage = imageList.get(i);
            if (checkedImage.path.equals(image.path)) {
                imageList.remove(i);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mISConfig == null) {
            mISConfig = new ImageConfig.Builder().build();
        }
        return mImageList.size();
    }


    class CameraItemViewHolder extends RecyclerView.ViewHolder {
        public CameraItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    class MediaItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imgThumb;
        ImageView imgCheck;
//        View viewThumbOverLay;
//        LinearLayout llVideoFalgContainer;
//        TextView tvVideoDuration;

        public MediaItemViewHolder(View itemView) {
            super(itemView);
            imgThumb = (ImageView) itemView.findViewById(R.id.imgThumb);
            imgCheck = (ImageView) itemView.findViewById(R.id.imgCheck);
//            viewThumbOverLay = itemView.findViewById(R.id.viewThumbOverLay);
//            llVideoFalgContainer = (LinearLayout) itemView.findViewById(R.id.llVideoFlagContainer);
//            tvVideoDuration = (TextView) itemView.findViewById(R.id.tvVideoDuration);
        }
    }

    public interface OnItemClickListener {
        void onMediaCheckClick(List<Image> checkedImageList, Image curImage);

        void onMediaClick(Image curImage, int position);

        void onReachMaxNum(int maxNum);

//        void onCameraItemClick();
    }
}
