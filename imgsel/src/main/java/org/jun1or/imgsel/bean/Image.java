package org.jun1or.imgsel.bean;


import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {

    public String path;
    public String name;
    public String durationStr;

    public Image(String path, String name, int durationMs) {
        this.path = path;
        this.name = name;
        durationStr = getDurationStr(durationMs);
    }

    public Image(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public Image() {

    }

    private String getDurationStr(int durationMs) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = durationMs / dd;
        long hour = (durationMs - day * dd) / hh;
        long minute = (durationMs - day * dd - hour * hh) / mi;
        long second = (durationMs - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = durationMs - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

        return strMinute + " : " + strSecond + "";

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.name);
    }

    protected Image(Parcel in) {
        this.path = in.readString();
        this.name = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}