package com.shan.mylotto.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.widget.Toast;

public class CommonUtil {

    // px을 dp로 변경
    public static int getConvertToDP(Resources resorces, int px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, resorces.getDisplayMetrics());
    }

}
