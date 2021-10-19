package com.shan.mylotto.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class CommonUtil {

    // px을 dp로 변경
    public static int getConvertToDP(Resources resorces, int px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, resorces.getDisplayMetrics());
    }

    // 기기에 맞는 DP 사이즈 알아내기
    public static int getConvertToDeviceDP(Resources resources, int size) {
        DisplayMetrics dm = resources.getDisplayMetrics();
        return Math.round(size * dm.density);
    }

    // 문자열이 숫자인지 체크
    public static boolean isNumeric(String string) {
        if(string == null || "".equals(string)){
            return false;
        }
        return string.matches("-?\\d+(\\.\\d+)?");
    }

    public static void showLoading(Activity activity, boolean isShow) {
        if(isShow) {
            LinearLayout linear = new LinearLayout(activity);
            linear.setTag("MyProgressBar");
            linear.setGravity(Gravity.CENTER);
            linear.setBackgroundColor(0x33000000);
            ProgressBar progressBar = new ProgressBar(activity);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            progressBar.setLayoutParams(layoutParams);
            linear.addView(progressBar);
            linear.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) { /*클릭방지*/ }
            });

            FrameLayout rootView = activity.findViewById(android.R.id.content);
            rootView.addView(linear);
        } else {
            FrameLayout rootView = activity.findViewById(android.R.id.content);
            LinearLayout linear = rootView.findViewWithTag("MyProgressBar");
            if(linear != null) {
                rootView.removeView(linear);
            }
        }
    }

}
