package com.auto.automaticassistant.assist;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC;
import static java.sql.DriverManager.println;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.app.ActionBar;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.NotificationCompat;

import com.auto.automaticassistant.R;
import com.auto.automaticassistant.utils.Tip;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class AutoService extends AccessibilityService {
    private static AutoService _this;

    private Timer timer;
    private static boolean isFirstAnchor = true;
    private static AccessibilityNodeInfo nextNode, anchorNode1, anchorNode2;
    // window hash code
    int whc = 0;

    @Override
    public void onCreate() {
        Log.e("看看这里", "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("看看这里", "onStartCommand");
        _this = this;
        timer = new Timer();
        // 安装手势操作


        Notification notification = createNotification();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, notification, FOREGROUND_SERVICE_TYPE_DATA_SYNC);
        } else {
            startForeground(1, notification);
        }

        Toast.makeText(this, "服务系统启动！", Toast.LENGTH_SHORT).show();
        int res = super.onStartCommand(intent, flags, startId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            AssistHelper.INSTANCE.setupAutoService(this);
        } else {
            Tip.longShow("当前机型，系统版本过低，暂不支持");
        }
        return res;
    }

    @Override
    protected void onServiceConnected() {
        println("看看这里？onServiceConnected");
        super.onServiceConnected();
    }

    private Notification createNotification() {
        String channelID = "MyAccessibilityServiceChannel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelID,
                    "Accessibility Service Channel",
                    NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID);
        builder.setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("自动辅助系统")
                .setContentText("辅助系统运行中")
                .setPriority(NotificationCompat.PRIORITY_LOW);
        return builder.build();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("xxxx", "服务退出了");
        return super.onUnbind(intent);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        AccessibilityNodeInfo node = accessibilityEvent.getSource();

        Log.e("onAccessibilityEvent:", accessibilityEvent.getEventType() + " --- " + node);
        if (node != null && accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
            Log.e("点击节点：==", "  node：" + node);
        }

        if (getRootInActiveWindow() == null) return;

        if (
                accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
                        || accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                        || accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED
        ) {
            if (whc != getRootInActiveWindow().hashCode()) { // 换窗口了就重新开始
                whc = getRootInActiveWindow().hashCode();
                AssistHelper.INSTANCE.updateNode(getRootInActiveWindow());
            }
        }
    }

    @Override
    public void onInterrupt() {

    }
}
