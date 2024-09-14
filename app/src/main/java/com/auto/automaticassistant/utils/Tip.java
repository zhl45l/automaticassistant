package com.auto.automaticassistant.utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.auto.automaticassistant.app.MyApplication;

public class Tip {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    private static Toast sToast;

    /**
     * 短暂提示
     *
     * @param text
     */
    public static void shortShow(final String text) {
        HANDLER.post(() -> {
//            cancel();
            sToast = Toast.makeText(MyApplication.instance, text, Toast.LENGTH_SHORT);
            sToast.show();
        });
    }

    /**
     * 长提示（比短暂长一点，实际也不太长）
     *
     * @param text
     */
    public static void longShow(final String text) {
        HANDLER.post(() -> {
            cancel();
            sToast = Toast.makeText(MyApplication.instance, text, Toast.LENGTH_LONG);
            sToast.show();
        });
    }

    /**
     * 取消提示
     */
    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }

}
