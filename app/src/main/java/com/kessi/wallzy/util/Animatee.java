package com.kessi.wallzy.util;

import android.app.Activity;
import android.content.Context;

import com.kessi.wallzy.R;


public class Animatee {
    public static void animateSlideUp(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.animate_slide_up_enter, R.anim.animate_slide_up_exit);
    }

    public static void animateSlideDown(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.animate_slide_down_enter, R.anim.animate_slide_down_exit);
    }
}
