package com.shan.mylotto.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
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

}
