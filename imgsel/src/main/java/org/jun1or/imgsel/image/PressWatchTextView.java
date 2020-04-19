package org.jun1or.imgsel.image;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.istrong.widget.view.AlphaTextView;


public class PressWatchTextView extends AlphaTextView {

    private OnPressWatchListener mOnPressWatchListener;

    public void setOnPressWatchListener(OnPressWatchListener onPressWatchListener) {
        this.mOnPressWatchListener = onPressWatchListener;
    }

    public PressWatchTextView(Context context) {
        super(context);
    }

    public PressWatchTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PressWatchTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (mOnPressWatchListener != null) {
            mOnPressWatchListener.onPressWath(pressed);
        }
    }


    public interface OnPressWatchListener {
        void onPressWath(boolean pressed);
    }
}
