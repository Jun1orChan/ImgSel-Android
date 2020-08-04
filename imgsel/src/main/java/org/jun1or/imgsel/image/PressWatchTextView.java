package org.jun1or.imgsel.image;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import org.jun1or.widget.view.AlphaTextView;


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
        /**
         * 按下
         *
         * @param pressed
         */
        void onPressWath(boolean pressed);
    }
}
