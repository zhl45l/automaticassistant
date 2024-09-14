package com.auto.automaticassistant.assist

import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.text.TextUtils
import android.view.accessibility.AccessibilityNodeInfo
import androidx.annotation.RequiresApi


// 辅助助手，各种手势操作
@RequiresApi(api = Build.VERSION_CODES.N)
object AssistHelper {
    lateinit var rootNode: AccessibilityNodeInfo
    lateinit var service: AutoService
    lateinit var gestureHelper: GestureHelper

    var nodeList = ArrayList<AccessibilityNodeInfo>() // 最新的，所有的节点

    // 安装手势操作
    fun setupAutoService(service: AutoService) {
        this.service = service
        gestureHelper = GestureHelper(service)
    }

    // 更新根节点
    fun updateNode(rootNode: AccessibilityNodeInfo) {
        this.rootNode = rootNode
    }

    ////////////////////  下面是一些常规操作方法

    /**
     * 模拟人体点击屏幕,点击节点正中心坐标
     */
    suspend fun clickScreenByNode(
        node: AccessibilityNodeInfo
    ): Boolean {
        // 生成5到20之间的随机数，用于点击位置的随机化
        var r = Rect()
        node.getBoundsInScreen(r)
        val x: Float = r.left + r.width() / 2.0f
        val y: Float = r.top + r.height() / 2.0f

        return clickPointByScreen(x, y)// 返回当前时间戳
    }

    suspend fun clickPointByScreen(
        p: Point
    ): Boolean {
        return gestureHelper.tap(p.x.toFloat(), p.y.toFloat()) // 返回当前时间戳
    }

    suspend fun clickPointByScreen(
        x: Float, y: Float
    ): Boolean {
        return gestureHelper.tap(x, y) // 返回当前时间戳
    }

    fun getAllNodes(): ArrayList<AccessibilityNodeInfo> {
        nodeList.clear()
        getAllNodes(rootNode)
        return nodeList
    }

    /**
     * 获取所有元素
     */
    private fun getAllNodes(node: AccessibilityNodeInfo) {
        nodeList.add(node)
        for (index in 0 until rootNode.childCount) {
            nodeList.add(node.getChild(index))
            getAllNodes(node.getChild(index))
        }
    }

    /**
     * 通过文本查找所有符合条件元素
     */
    fun findByText(text: String): List<AccessibilityNodeInfo>? {
        return rootNode?.findAccessibilityNodeInfosByText(text)?.toList()
    }

    /**
     * 通过文本查找所有符合条件元素
     */
    fun findByTag(tag: String): ArrayList<AccessibilityNodeInfo>? {
        val nodeList = ArrayList<AccessibilityNodeInfo>()
        getAllNodes().forEach {
            if (TextUtils.equals(tag, it.className)) {
                nodeList.add(it)
            }
        }
        return nodeList
    }


    //////////////////////////////////////////////////
////////////  这里定义一些扩展方法  ////////////////
//////////////////////////////////////////////////
    // 寻找可点击节点，从自己开始一直到根节点，寻找可点击节点
    fun AccessibilityNodeInfo.findFirstClickableNode(): AccessibilityNodeInfo? {
        if (this.isClickable == true) return this; // 自己可点击就返回自己

        if (parent == null) return null; // 父类是空就返回null

        if (parent?.isClickable == true) { // 父类可点击，就返回父类
            return parent
        } else { // 否则 继续往上查找
            return parent?.findFirstClickableNode()
        }
    }


}


