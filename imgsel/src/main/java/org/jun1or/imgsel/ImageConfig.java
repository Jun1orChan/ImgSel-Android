package org.jun1or.imgsel;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.istrong.imgsel.R;

import java.util.ArrayList;


public class ImageConfig implements Parcelable {

    //文本颜色
    public int mTextColor;
    //标题
    public String mTitle;
    //回退图标
    public int mBackImageRes;
    //最大选择图片数量
    public int mMaxNum;
    //是否多选
    public boolean mMultiSelect;
    //已选择图片列表
    public ArrayList<String> mImgList;
    //状态栏模式,1:LightMode,0:DarkMode
    public int mMode = 0;
    //状态栏背景色
    public int mTopBarBgColor;
    public boolean mTitleBold;

    public ImageConfig(Builder builder) {
        this.mTextColor = builder.textColor;
        this.mTitle = builder.title;
        this.mBackImageRes = builder.backImageRes;
        this.mMaxNum = builder.maxNum;
        this.mMultiSelect = builder.multiSelect;
        this.mImgList = builder.imgList;
        this.mMode = builder.mode;
        this.mTopBarBgColor = builder.topBarBgColor;
        this.mTitleBold = builder.titleBold;
    }


    public static class Builder implements Parcelable {
        private int textColor = Color.WHITE;
        private String title = "选择";
        private int backImageRes = R.mipmap.imgsel_topbar_back;
        private int maxNum = 9;
        private boolean multiSelect = true;
        private ArrayList<String> imgList;
        private int mode;
        private int topBarBgColor = Color.TRANSPARENT;
        private boolean titleBold = false;

        public Builder titleBold(boolean titleBold) {
            this.titleBold = titleBold;
            return this;
        }

        public Builder topBarBgColor(int color) {
            this.topBarBgColor = color;
            return this;
        }

        public Builder mode(int mode) {
            this.mode = mode;
            return this;
        }

        public Builder textColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder backImageRes(int res) {
            this.backImageRes = res;
            return this;
        }

        public Builder maxNum(int maxNum) {
            this.maxNum = maxNum;
            return this;
        }

        public Builder multiSelect(boolean multiSelect) {
            this.multiSelect = multiSelect;
            return this;
        }

        public Builder selectedList(ArrayList<String> imgList) {
            this.imgList = imgList;
            return this;
        }

        public ImageConfig build() {
            return new ImageConfig(this);
        }

        public Builder() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.textColor);
            dest.writeString(this.title);
            dest.writeInt(this.backImageRes);
            dest.writeInt(this.maxNum);
            dest.writeByte(this.multiSelect ? (byte) 1 : (byte) 0);
            dest.writeStringList(this.imgList);
            dest.writeInt(this.mode);
            dest.writeInt(this.topBarBgColor);
            dest.writeByte(this.titleBold ? (byte) 1 : (byte) 0);
        }

        protected Builder(Parcel in) {
            this.textColor = in.readInt();
            this.title = in.readString();
            this.backImageRes = in.readInt();
            this.maxNum = in.readInt();
            this.multiSelect = in.readByte() != 0;
            this.imgList = in.createStringArrayList();
            this.mode = in.readInt();
            this.topBarBgColor = in.readInt();
            this.titleBold = in.readByte() != 0;
        }

        public static final Creator<Builder> CREATOR = new Creator<Builder>() {
            @Override
            public Builder createFromParcel(Parcel source) {
                return new Builder(source);
            }

            @Override
            public Builder[] newArray(int size) {
                return new Builder[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mTextColor);
        dest.writeString(this.mTitle);
        dest.writeInt(this.mBackImageRes);
        dest.writeInt(this.mMaxNum);
        dest.writeByte(this.mMultiSelect ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.mImgList);
        dest.writeInt(this.mMode);
        dest.writeInt(this.mTopBarBgColor);
        dest.writeByte(this.mTitleBold ? (byte) 1 : (byte) 0);
    }

    protected ImageConfig(Parcel in) {
        this.mTextColor = in.readInt();
        this.mTitle = in.readString();
        this.mBackImageRes = in.readInt();
        this.mMaxNum = in.readInt();
        this.mMultiSelect = in.readByte() != 0;
        this.mImgList = in.createStringArrayList();
        this.mMode = in.readInt();
        this.mTopBarBgColor = in.readInt();
        this.mTitleBold = in.readByte() != 0;
    }

    public static final Creator<ImageConfig> CREATOR = new Creator<ImageConfig>() {
        @Override
        public ImageConfig createFromParcel(Parcel source) {
            return new ImageConfig(source);
        }

        @Override
        public ImageConfig[] newArray(int size) {
            return new ImageConfig[size];
        }
    };
}
