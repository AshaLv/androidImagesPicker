package com.example.ashaphotospicker.AshaConstant;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;

import com.example.ashaphotospicker.AshaHelper.BaseActivity;

public class SizeConstant {

    static private final int DesignScrennWidth = 750;

    static private DisplayMetrics metrics;
    static private float density;
    static private float sp;
    static private float widthPixels;

    static public void getSingletonMetrics(Context context) {
        if(metrics == null) {
            metrics = context.getResources().getDisplayMetrics();
            widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
            float dpProportion = widthPixels / (DesignScrennWidth*(metrics.density));
            float spProportion = widthPixels / (DesignScrennWidth*(metrics.density)*(TypedValue.COMPLEX_UNIT_SP));
            sp = (TypedValue.COMPLEX_UNIT_SP * spProportion);
            density = (metrics.density * dpProportion);
        }
    }

    public static float getWidthPixels() {
        return widthPixels;
    }

    public static float getDensity() {
        return density;
    }

    public static float getSp() {
        return sp;
    }

    public static int getPixel(int pixel) {
        return (int) (pixel * density);
    }

    public static float getSpPixel(int pixel) {
        return pixel * sp;
    }

    //关于字体大小的
    static public float extremeSmallSize() {
        return 8*sp;
    }
    static public float smallSize() {
        return 14*sp;
    }
    static public float mediumSize() {
        return 16*sp;
    }
    static public float bigSize() {
        return 24*sp;
    }
    static public float hugeSize() {
        return 34*sp;
    }
    //关于图片大小的
    static public int extremeImageSmallSize() {
        return 8;
    }
    static public int smallImageSize() {
        return getPixel(120);
    }
    static public int mediumImageSize() {
        return 16;
    }
    static public int bigImageSize() {
        return 24;
    }
    static public int hugeImageSize() {
        return 34;
    }
    //关于距离的
    static public int extremeSmallDistance() {
        return getPixel(24);
    }
    static public int smallDistance() {
        return getPixel(32);
    }
    static public int mediumDistance() {
        return 16;
    }
    static public int bigDistance() {
        return 24;
    }
    static public int hugeDistance() {
        return 34;
    }

}
