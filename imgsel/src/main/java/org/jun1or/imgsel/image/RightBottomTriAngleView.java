package org.jun1or.imgsel.image;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import org.jun1or.util.DisplayUtil;


/**
 * @author cwj
 */
public class RightBottomTriAngleView extends View {

    private Paint mPaint;

    private int mDefaultWidth = 0;
    private int mDefaultHeight = 0;

    private int mTriangleColor = Color.WHITE;

    public RightBottomTriAngleView(Context context) {
        super(context);
        init();
    }

    public RightBottomTriAngleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RightBottomTriAngleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mTriangleColor);
        mPaint.setAlpha(255);
        mPaint.setStyle(Paint.Style.FILL);
        mDefaultWidth = mDefaultHeight = DisplayUtil.dp2px(getContext(), 15);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 当布局参数设置为wrap_content时，设置默认值
        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height ==
                ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mDefaultWidth, mDefaultHeight);
            // 宽 / 高任意一个布局参数为= wrap_content时，都设置默认值
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mDefaultWidth, heightSize);
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(widthSize, mDefaultHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        //三角形的高度为整个高度的2/3
        path.moveTo(getWidth(), getHeight() / 3);
        path.lineTo(getWidth() - getHeight() * 2 / 3, getHeight());
        path.lineTo(getWidth(), getHeight());
        path.close();
        canvas.drawPath(path, mPaint);
    }

    public void setTriangleColor(int color) {
        this.mTriangleColor = color;
        mPaint.setColor(mTriangleColor);
        invalidate();
    }

    public void setTriangleAlpha(int alpha) {
        mPaint.setAlpha(alpha);
        invalidate();
    }
}
