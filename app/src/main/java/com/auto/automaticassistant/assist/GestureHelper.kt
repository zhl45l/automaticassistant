package com.auto.automaticassistant.assist

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityService.GestureResultCallback
import android.accessibilityservice.GestureDescription
import android.accessibilityservice.GestureDescription.StrokeDescription
import android.content.Context
import android.graphics.Path
import android.graphics.PointF
import android.os.Build
import android.util.Log
import android.view.ViewConfiguration
import android.view.WindowManager
import androidx.annotation.RequiresApi
import java.util.Timer
import java.util.TimerTask

class GestureHelper(asb: AccessibilityService) {
    var path: Path = Path()
    var asb: AccessibilityService = asb;
    private var callback: GestureResultCallback? = null
    var screenWidth: Int = 0
    var screenHeight: Int = 0

    init {
        val wm = asb.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        screenWidth = wm.defaultDisplay.width
        screenHeight = wm.defaultDisplay.height

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            callback = object : GestureResultCallback() {
                override fun onCompleted(gestureDescription: GestureDescription) {
                    super.onCompleted(gestureDescription)
                    // 手势完成时执行的操作
                    Log.e("手势完成", "vvvvv")
                }

                override fun onCancelled(gestureDescription: GestureDescription) {
                    super.onCancelled(gestureDescription)
                    // 手势取消时执行的操作
                    Log.e("手势取消", "xxxx")
                }
            }
        }
    }

    fun actionUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            path.reset()

            // 定义手势路径 第一次点击
            path.moveTo((screenWidth / 2).toFloat(), (screenHeight / 2).toFloat())
            path.lineTo((screenWidth / 2).toFloat(), (screenHeight / 2 - 1).toFloat())
            val sd = StrokeDescription(path, 0, 200, true)

            val builder = GestureDescription.Builder()

            // 设置手势路径和开始时间
            val gestureDescription = builder.addStroke(sd).build()

            Log.e("发送手势", "ffff")
            // 发送手势事件
            asb.dispatchGesture(gestureDescription, object : GestureResultCallback() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                override fun onCompleted(gestureDescription: GestureDescription) {
                    super.onCompleted(gestureDescription)
                    // 手势完成时执行的操作
                    Log.e("手势完成", "vvvvv")

                    path.reset()
                    path.moveTo(
                        (screenWidth / 2 + Math.random() * 100).toFloat(),
                        (screenHeight / 2 - 1).toFloat()
                    ) //接着上一个
                    path.lineTo((screenWidth / 2).toFloat(), 190f)
                    val sd2 = sd.continueStroke(path, 0, 800, false)
                    asb.dispatchGesture(
                        GestureDescription.Builder().addStroke(sd2).build(),
                        null,
                        null
                    )

                    val timer = Timer()
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            // 这里是要执行的任务
                            tap(PointF(1033f, 1291f))
                            //                            tap();
                        }
                    }, 2000) // 延迟3秒执行任务
                }

                override fun onCancelled(gestureDescription: GestureDescription) {
                    super.onCancelled(gestureDescription)
                    // 手势取消时执行的操作
                    Log.e("手势取消", "xxxx")
                }
            }, null)
        }


//        Instrumentation instrumentation = new Instrumentation();
//        Rect rect = new Rect();
//
////        getRootInActiveWindow().getBoundsInWindow(rect);
//// 计算起始点和终止点的坐标
//        int startX = 100;
//        int startY = 500;
//        int endX = startX;
//        int endY = 0;
//
//        Log.e("开始华东", startY + " to " + endY);
//
//// 发送触摸事件
//        Long pTime = SystemClock.uptimeMillis();
//        MotionEvent event = MotionEvent.obtain(pTime, pTime,
//                MotionEvent.ACTION_DOWN, startX, startY, 0);
//        instrumentation.sendPointerSync(event);
//        event.recycle();
//
//        event = MotionEvent.obtain(pTime, pTime + 1000,
//                MotionEvent.ACTION_MOVE, endX, endY, 0);
//        instrumentation.sendPointerSync(event);
//        event.recycle();
//
//        event = MotionEvent.obtain(pTime + 1000, pTime + 1000,
//                MotionEvent.ACTION_UP, endX, endY, 0);
//        instrumentation.sendPointerSync(event);
//        event.recycle();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun tap(point: PointF) {
        val path = Path()
        path.moveTo(point.x, point.y)

        Log.e("点击了", "vvvvvvvvvvvvv")

        val tap = StrokeDescription(path, 0, ViewConfiguration.getTapTimeout().toLong())
        val builder = GestureDescription.Builder()
        builder.addStroke(tap)
        asb.dispatchGesture(builder.build(), null, null)
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public fun tap(x: Float, y: Float): Boolean {
        val path = Path()
        path.moveTo(x, y)

        Log.e("点击了", "vvvvvvvvvvvvv")

        val tap = StrokeDescription(path, 0, ViewConfiguration.getTapTimeout().toLong())
        val builder = GestureDescription.Builder()
        builder.addStroke(tap)
        return asb.dispatchGesture(builder.build(), null, null)
    }
}