package com.hut.reoger.pictruecarousel;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by 24540 on 2017/3/16.
 * 轮播图的核心类
 */

public class Banner extends ViewGroup {

    private int childCount;//子view的个数
    private int childHeight;//子view的高度
    private int childWidth;//子view的宽度

    private int x;      //移动前的横坐标
    private int index = 0; //当前图片的索引位置

    private boolean isAuto=true;//是否自动轮播，true表示自动轮播，false表示不轮播

    private boolean isClick = false;//判断是否为单击事件
    private Timer timer = new Timer();
    private TimerTask task;

    public static int TIME_INTERVAL=3000;

    private Scroller scroller;

    private ImageBannerListener listener;//点击事件
    private ImageBannerSelect ImageBannerSelect;//通知当前的图片索引


    public interface ImageBannerListener {
        void OnClickListener(int pos);
    }


    private boolean isFirstOpen = true;
    private Handler autoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://此时需要轮播
//                    if (++index >= childCount) {
//                        index = 0;
//                    }
                    if(isFirstOpen){
                       isFirstOpen = false;
                        return;
                    }
                    int preX  = index*childWidth;
                    carouselOrder();
                    int currentX = index * childWidth-preX;
                    //   scrollTo(currentX, 0);
                    scroller.startScroll(preX , 0, currentX, 0);
                    invalidate();
                    ImageBannerSelect.selectImage(index);
                    break;
                case 1:
                    break;

            }
        }
    };


    private boolean hasAddIndex = true;//当为true时，表示index正在增长

    private void carouselOrder() {
        if (hasAddIndex) {
            if (++index >= childCount-1) {
                hasAddIndex = false;
            }
        } else {
            if (--index <= 0) {
                hasAddIndex = true;
            }
        }
    }

    public ImageBannerSelect getImageBannerSelect() {
        return ImageBannerSelect;
    }

    public void setImageBannerSelect(ImageBannerSelect ImageBannerSelect) {
        this.ImageBannerSelect = ImageBannerSelect;
    }

    public ImageBannerListener getListener() {
        return listener;
    }

    public void setListener(ImageBannerListener listener) {
        this.listener = listener;
    }

    public Banner(Context context) {
        super(context);

        initScroller();

    }

    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initScroller();
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initScroller();
    }

    private void initScroller() {
        scroller = new Scroller(getContext());
        task = new TimerTask() {
            @Override
            public void run() {
                if (isAuto) {//开启了轮播图
                    autoHandler.sendEmptyMessage(0);
                }
            }
        };

        timer.schedule(task, 100, TIME_INTERVAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        childCount = getChildCount();
        if (0 == childCount) {
            setMeasuredDimension(0, 0);
        } else {
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            View view = getChildAt(0);
            childHeight = view.getMeasuredHeight();
            childWidth = view.getMeasuredWidth();
            int width = view.getMeasuredWidth() * childCount;//所有子视图的宽度和
            setMeasuredDimension(width, childHeight);
        }
    }


    /**
     * @param change
     * @param l      left
     * @param t      top
     * @param r      right
     * @param b      bottom
     */
    @Override
    protected void onLayout(boolean change, int l, int t, int r, int b) {
        if (change) {
            int leftMargin = 0;
            for (int i = 0; i < childCount; i++) {
                View view = getChildAt(i);
                view.layout(leftMargin, 0, leftMargin + childWidth, childHeight);
                leftMargin += childWidth;
            }
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {//是否滑动完毕
            scrollTo(scroller.getCurrX(), 0);
            invalidate();//重绘
        }
    }

    //拦截事件的处理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isClick = true;
                stopAtto();
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                x = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                isClick = false;
                int moveX = (int) event.getX();
                int distance = moveX - x;
                scrollBy(-distance, 0);
                x = moveX;
                break;
            case MotionEvent.ACTION_UP:
                startAuto();
                int scrollx = getScrollX();
                index = (scrollx + childWidth / 2) / childWidth;
                if (index < 0) {//说明此时已经滑动到了最左边
                    index = 0;
                } else if (index > childCount - 1) {//说明此时已经滑动了最右边
                    index = childCount - 1;
                }

                if (isClick) {//是单机事件
                    listener.OnClickListener(index);
                } else {

                    //scrollTo(index*childWidth,0);
                    int dx = index * childWidth - scrollx;
                    scroller.startScroll(scrollx, 0, dx, 0);

                    postInvalidate();
                    ImageBannerSelect.selectImage(index);
                }

                break;
            default:
                break;
        }
        return true;//返回true，告知拦截了事件
    }

    //事件的拦截，该方法的返回值为true，自定义的容器会处理此次拦截事件
    //返回值为false，点击事件将会继续传递下去
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    public void startAuto() {
        isAuto = true;
    }

    public void stopAtto() {
        isAuto = false;
    }

    public interface ImageBannerSelect {
        void selectImage(int pos);
    }
}
