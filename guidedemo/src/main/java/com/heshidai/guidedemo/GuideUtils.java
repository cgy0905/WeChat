package com.heshidai.guidedemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by cgy
 * 2018/5/31  10:57
 */
public class GuideUtils {

    private Context context;
    private ImageView imageView;
    private ViewGroup content;
    private static GuideUtils mInstance;

    private boolean isFirst = true;
    private int i = 0;

    int images[] = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3, R.drawable.guide_4, R.drawable.guide_5};

    private GuideUtils() {

    }

    public static GuideUtils getInstance() {
        if (mInstance == null) {
            synchronized (GuideUtils.class) {
                if (mInstance == null) {
                    mInstance = new GuideUtils();
                }
            }
        }
        return mInstance;
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    // 设置LayoutParams参数
                    final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                    // 设置显示的类型，TYPE_PHONE指的是来电话的时候会被覆盖，其他时候会在最前端，显示位置在stateBar下面，其他更多的值请查阅文档
                    params.type = WindowManager.LayoutParams.TYPE_PHONE;
                    // 设置显示格式
                    params.format = PixelFormat.RGBA_8888;
                    // 设置对齐方式
                    params.gravity = Gravity.LEFT | Gravity.TOP;
                    // 设置宽高
                    params.width = ScreenUtils.getScreenWidth(context);
                    params.height = ScreenUtils.getScreenHeight(context);
                    // 设置动画
                    //params.windowAnimations = R.style.view_anim;

                    // 添加到当前的界面上
                    content.addView(imageView, params);
                    break;
            }
        }
    };

    public void initGuide(Activity context, int mipmapRourcesId) {
        if (!isFirst) {
            return;
        }
        this.context = context;
        content = context.findViewById(android.R.id.content);

        // 动态初始化图层
        imageView = new ImageView(context);
        imageView.setLayoutParams(new WindowManager.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(mipmapRourcesId);
        handler.sendEmptyMessage(1);

        // 点击图层之后，将图层移除
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//              windowManager.removeView(imgView);//这句话是移除当前ImageView的
                imageView.setImageResource(images[i]);
                if (i < images.length) {
                    i++;
                } else if (i == images.length) {
                    i = 0;
                    content.removeView(imageView);
                }
            }
        });
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }
}
