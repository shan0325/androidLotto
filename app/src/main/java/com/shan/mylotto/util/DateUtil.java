package com.shan.mylotto.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String formatByDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
