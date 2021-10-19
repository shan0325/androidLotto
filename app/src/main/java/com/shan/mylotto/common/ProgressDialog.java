package com.shan.mylotto.common;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.shan.mylotto.R;

public class ProgressDialog extends Dialog {

    public ProgressDialog(Context context)
    {
        super(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        // 다이얼 로그 제목을 안보이게...
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);
    }
}
