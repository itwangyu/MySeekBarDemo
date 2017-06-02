package com.wangyu.myseekbardemo;

import android.content.Context;
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
 * seekbar 重写ontouch事件来实现
 * Created by wangyu on 2017/6/2 0002.
 */

public class MySeekBar2 extends RelativeLayout {
    private Context context;
    private ImageView ivThumb;//滑块
    private ProgressBar progressBar;//进度条
    private int range;//可滑动的范围
    /**
    属性
     */
    private int max = 10;//默认100
    private int progress;//进度
    private ProgressChangeListener listener;//进度回调
    private float progressbarHeight = 6;//progressbar的高度 单位是dp
    private float padding;//左右边距，让出滑块一半的宽度，让滑块指针对齐进度
    private float distance=3;//进度条和滑块之间的间距 dp
    private LayoutParams ivPara;//用于在点击的时候控制滑块的位置
    private LayoutParams params;//控制progressbar的参数


    public MySeekBar2(Context context) {
        this(context, null);
    }

    public MySeekBar2(Context context, AttributeSet attrs) {
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
    public boolean onTouchEvent(MotionEvent event) {
        //不管是按下还是滑动，都要改变位置
        float x=event.getX();
        int left;
        if (0 <= x&&x <= range) {
            if (x > range - ivThumb.getWidth()) {
                left = range;
            } else {
                left = ((int) (x / (range / max))) * (range / max);
            }
            ivPara.leftMargin= left;
            ivThumb.setLayoutParams(ivPara);
            //设置进度
            int progress = (int) (x / (range * 1.0f) * max);
            Log.i("wangyu", "range:" + range + "  x:" + x);
            if (x >= range-ivThumb.getWidth()) {
                progress=max;
            }
            setProgress(progress);
        }
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
