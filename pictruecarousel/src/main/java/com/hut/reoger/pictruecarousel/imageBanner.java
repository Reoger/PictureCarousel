package com.hut.reoger.pictruecarousel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by 24540 on 2017/3/16.
 * 加底部圆点实现
 */

public class imageBanner extends FrameLayout implements Banner.ImageBannerSelect, Banner.ImageBannerListener {


    private boolean mIsAutoCycle;//是否开启自动循环播放 默认为true
    private int mTimeInerval=3000;//时间间隔，默认默认为3000毫秒

    private LinearLayout linearLayout;
    private Banner banner;

    public OnImageViewSelectClick getOnImageViewSelectClick() {
        return onImageViewSelectClick;
    }

    public void setOnImageViewSelectClick(OnImageViewSelectClick onImageViewSelectClick) {
        this.onImageViewSelectClick = onImageViewSelectClick;
    }

    private OnImageViewSelectClick onImageViewSelectClick;

    @Override
    public void OnClickListener(int pos) {
        onImageViewSelectClick.OnClickListener(pos);
    }

    public interface OnImageViewSelectClick {
        void OnClickListener(int position);
    }

    public imageBanner(@NonNull Context context) {
        super(context);
        initImageBarnner();
        initDotLinearLayout();
    }

    public imageBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData(attrs,context);
        initImageBarnner();
        initDotLinearLayout();
    }

    public imageBanner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(attrs,context);
        initImageBarnner();
        initDotLinearLayout();
    }

    void  initData(AttributeSet attrs,Context context){
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,R.styleable.imageBanner);
        mIsAutoCycle = mTypedArray.getBoolean(R.styleable.imageBanner_auto_loop,true);
        mTimeInerval = mTypedArray.getInteger(R.styleable.imageBanner_time_interval,3000);
        //mIsAutoCycle 无效参数，没有具体实现
    }


    public void addImageToImageBarnner(List<Bitmap> bitmaps) {
        for (int i = 0; i < bitmaps.size(); i++) {
            addImageToBanner(bitmaps.get(i));
            addDotLinearLayout();
        }
    }

    private void addDotLinearLayout() {
        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(lp);
        lp.setMargins(5, 5, 5, 5);
        imageView.setImageResource(R.drawable.dot_normal);
        linearLayout.addView(imageView);
    }

    private void addImageToBanner(Bitmap bm) {
        ImageView iv = new ImageView(getContext());
        iv.setImageBitmap(bm);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        banner.addView(iv);
    }

    private void initDotLinearLayout() {
        linearLayout = new LinearLayout(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                40);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        //    linearLayout.setBackground(Color.RED);
        addView(linearLayout);

        LayoutParams layoutParams = (LayoutParams) linearLayout.getLayoutParams();
        layoutParams.gravity = Gravity.BOTTOM;
        linearLayout.setLayoutParams(layoutParams);

        //3.0版本使用的是setAlpha() 在3.0之前使用的是setAlpha(),但是调用者不同
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            linearLayout.setAlpha(0.5f);
        } else {
            linearLayout.getBackground().setAlpha(100);
        }
    }

    private void initImageBarnner() {
        Banner.TIME_INTERVAL = mTimeInerval;
        banner = new Banner(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        banner.setLayoutParams(lp);
        banner.setImageBannerSelect(this);
        banner.setListener(this);
        addView(banner);
    }

    @Override
    public void selectImage(int pos) {
        int count = linearLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            ImageView iv = (ImageView) linearLayout.getChildAt(i);
            if (i == pos) {
                iv.setImageResource(R.drawable.dot_selectl);
            } else {
                iv.setImageResource(R.drawable.dot_normal);
            }
        }
    }
}
