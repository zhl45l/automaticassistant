package com.auto.automaticassistant.assist

import android.os.Build
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import androidx.annotation.RequiresApi
import com.auto.automaticassistant.app.MyApplication

fun AccessibilityNodeInfo.findText(){
    var abc = "123";
    abc.let {  }
}
class ABSNode {

    @RequiresApi(Build.VERSION_CODES.R)
    fun abc(){
        val node = AccessibilityNodeInfo(View(MyApplication.instance))
//        node.findAccessibilityNodeInfosByText()
    }
}