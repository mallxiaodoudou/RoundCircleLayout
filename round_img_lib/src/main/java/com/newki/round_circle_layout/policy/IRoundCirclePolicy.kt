package com.newki.round_circle_layout.policy

import android.graphics.Canvas
import android.graphics.drawable.Drawable

// 策略的接口定义
interface IRoundCirclePolicy {

    fun isCustomRound(): Boolean

    fun beforeDispatchDraw(canvas: Canvas?)

    fun afterDispatchDraw(canvas: Canvas?)

    fun onDraw(canvas: Canvas?): Boolean

    fun onLayout(left: Int, top: Int, right: Int, bottom: Int)

    fun setBackground(background: Drawable?)

    fun setBackgroundColor(color: Int)

    fun setBackgroundResource(resid: Int)

    fun setBackgroundDrawable(background: Drawable?)

    fun setNativeDrawable(drawable: Drawable)

}