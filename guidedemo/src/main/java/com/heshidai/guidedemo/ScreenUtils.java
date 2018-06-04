package com.heshidai.guidedemo;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by cgy
 * 2018/5/31  11:43
 */
public class ScreenUtils {

    public static int getStatusBarHeight(Context context) {
        int result = 0;

        try {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }

//    public static int[] getScreenSize (Context context, boolean useDeviceSize) {
//
//        int[] size = new int[2];
//        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display d = windowManager.getDefaultDisplay();
//        DisplayMetrics metrics = new DisplayMetrics();
//        d.getMetrics(metrics);
//        int widthPixels = metrics.widthPixels;
//        int heightPixels = metrics.heightPixels;
//
//        if (!useDeviceSize) {
//            size[0] = widthPixels;
//            size[1] = heightPixels - getStatusBarHeight(context);
//
//            return size;
//        }
//
//        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
//            try {
//                widthPixels = (int) Display.class.getMethod("getRawWidth").invoke(d);
//                heightPixels = (int) Display.class.getMethod("getRawHeight").invoke(d);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        if (Build.VERSION.SDK_INT >= 17) {
//
//            Point realSize = new Point();
//            try {
//                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
//                widthPixels = realSize.x;
//                heightPixels = realSize.y;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        size[0] = widthPixels;
//        size[1] = heightPixels;
//        return size;
//    }

    public static DisplayMetrics getDisPlayMetrics(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        if (null != context) {
            ((Activity) context).getWindowManager().getDefaultDisplay()
                    .getMetrics(metric);
        }
        return metric;
    }

    public static int getScreenWidth(Context context) {
        int width = getDisPlayMetrics(context).widthPixels;
        return width;
    }

    public static int getScreenHeight(Context context) {
        int height = getDisPlayMetrics(context).heightPixels;
        return height;
    }

    public static float getDensity(Context context) {
        float density = getDisPlayMetrics(context).density;
        return density;
    }

    public static int getDensityDpi(Context context) {
        int densityDpi = getDisPlayMetrics(context).densityDpi;
        return densityDpi;

    }
}
