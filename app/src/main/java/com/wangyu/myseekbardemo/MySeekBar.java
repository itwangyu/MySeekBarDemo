package com.wangyu.myseekbardemo;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * seekbar 用viewdraghelper实现，可以自由滑动，但是在max太小的时候会出现指针对不齐的情况
 * Created by wangyu on 2017/6/2 0002.
 */

public class MySeekBar extends RelativeLayout {
    private Context context;
    private ImageView ivThumb;//滑块
    private ProgressBar progressBar;//进度条
    private ViewDragHelper mDrager;
    private int range;//可滑动的范围
    /**
    属性
     */
    private int max = 100;//默认100
    private int progress;//进度
    private ProgressChangeListener listener;//进度回调
    private float progressbarHeight = 6;//progressbar的高度 单位是dp
    private float padding;//左右边距，让出滑块一半的宽度，让滑块指针对齐进度
    private float distance=3;//进度条和滑块之间的间距 dp
    private LayoutParams ivPara;//用于在点击的时候控制滑块的位置
    private LayoutParams params;//控制progressbar的参数


    public MySeekBar(Context context) {
        this(context, null);
    }

    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    /**
     * view初始化
     */
    private void init() {
        ivPara = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        ivThumb = new ImageView(context);
        ivThumb.setImageResource(R.mipmap.sbo);
        ivThumb.setId(R.id.ivThumb);
        addView(ivThumb);
        progressBar = (ProgressBar) View.inflate(context, R.layout.view_seekbar, null);
        params = new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(progressbarHeight));
        params.addRule(RelativeLayout.BELOW, R.id.ivThumb);
        params.topMargin = dip2px(distance);
        progressBar.setLayoutParams(params);
        addView(progressBar);
        progressBar.setMax(max);
        mDrager = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == ivThumb;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                int progress = (int) (left / (range * 1.0f) * max);
                setProgress(progress);
            }


            @Override
            public int getViewHorizontalDragRange(View child) {
                return range;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                //边界限制
                if (left < 0) {
                    left = 0;
                }
                if (left > range) {
                    left = range;
                }
//                if (isFollow) {
//                    float fen=range/max;
//                    Log.i("wangyu", "left：" + left + "");
//                    Log.i("wangyu", "商：" + ((int) (left / fen)) + "");
//                    float a = ((int)(left /fen)) * fen;
//                    Log.i("wangyu", a+"");
//                    left= (int) a;
//                }
                return left;
            }
        });
    }

    /**
     * 设置进度
     * @param progress
     */
    public void setProgress(int progress) {
        Log.i("wangyu", "progress:" + progress);
        progressBar.setProgress(progress);
        if (listener != null) {
            listener.onProgressChange(progress);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN) {
            float x=event.getX();
            if (0 <= x&&x <= range) {
                ivPara.leftMargin= (int) x;
                ivThumb.setLayoutParams(ivPara);
                //手指按下的时候，设置进度
                int progress = (int) (x / (range * 1.0f) * max);
                setProgress(progress);
            }
        }
        return mDrager.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDrager.processTouchEvent(event);
        return true;
    }

    public void setMax(int max) {
        this.max = max;
        progressBar.setMax(max);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        range = widthSize - ivThumb.getWidth();
        //这里滑块图片的宽度已经测量完毕
        if (ivThumb.getWidth() > 0) {
            padding = ivThumb.getWidth()/2;
            params.leftMargin= (int) padding;
            params.rightMargin= (int) padding;
            progressBar.setLayoutParams(params);
        }
    }

    public interface ProgressChangeListener {
        void onProgressChange(int progress);
    }

    public void setOnProgressChangeListener(ProgressChangeListener progressChangeListener) {
        this.listener = progressChangeListener;
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
