package com.newki.round_circle_layout.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.newki.round_circle_layout.policy.RoundCircleLayoutShaderPolicy
import com.newki.round_circle_layout.policy.IRoundCirclePolicy
import com.newki.round_circle_layout.policy.RoundCircleLayoutOutlinePolicy
import com.newki.round_circle_layout.view.IRoundCircleView

/**
 * 圆角布局的实现类
 * 内部使用不同的策略来实现，5.0以上和5.0一下不同的实现方式
 */
internal class RoundCircleViewImpl(
    view: View, context: Context, attributeSet: AttributeSet?, attrs: IntArray, attrIndexs: IntArray
) : IRoundCircleView {

    private lateinit var roundCirclePolicy: IRoundCirclePolicy

    init {
        init(view, context, attributeSet, attrs, attrIndexs)
    }

    fun beforeDispatchDraw(canvas: Canvas?) {
        roundCirclePolicy.beforeDispatchDraw(canvas)
    }

    fun afterDispatchDraw(canvas: Canvas?) {
        roundCirclePolicy.afterDispatchDraw(canvas)
    }

    fun onDraw(canvas: Canvas?): Boolean {
        return roundCirclePolicy.onDraw(canvas)
    }

    private fun init(view: View, context: Context, attributeSet: AttributeSet?, attrs: IntArray, attrIndexs: IntArray) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0版本以上与5.0一下的兼容处理

            //判断是否包含自定义圆角
            val typedArray = context.obtainStyledAttributes(attributeSet, attrs)
            val topLeft = typedArray.getDimensionPixelOffset(attrIndexs[2], 0).toFloat()
            val topRight = typedArray.getDimensionPixelOffset(attrIndexs[3], 0).toFloat()
            val bottomLeft = typedArray.getDimensionPixelOffset(attrIndexs[4], 0).toFloat()
            val bottomRight = typedArray.getDimensionPixelOffset(attrIndexs[5], 0).toFloat()
            typedArray.recycle()

            roundCirclePolicy = if (topLeft > 0 || topRight > 0 || bottomLeft > 0 || bottomRight > 0) {
                //自定义圆角使用兼容方案
                RoundCircleLayoutShaderPolicy(view, context, attributeSet, attrs, attrIndexs)
            } else {
                //使用OutLine裁剪方案
                RoundCircleLayoutOutlinePolicy(view, context, attributeSet, attrs, attrIndexs)
            }
        } else {
            // 5.0以下的版本使用兼容方案
            roundCirclePolicy = RoundCircleLayoutShaderPolicy(view, context, attributeSet, attrs, attrIndexs)
        }

    }

    //重写接口的方法，使用不同的策略来实现不同的裁剪
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        roundCirclePolicy.onLayout(left, top, right, bottom)
    }


    override fun setBackground(background: Drawable?) {
        roundCirclePolicy.setBackground(background)
    }

    override fun setBackgroundColor(color: Int) {
        roundCirclePolicy.setBackgroundColor(color)
    }

    override fun setBackgroundResource(resid: Int) {
        roundCirclePolicy.setBackgroundResource(resid)
    }

    override fun setBackgroundDrawable(background: Drawable?) {
        roundCirclePolicy.setBackgroundDrawable(background)
    }

    //XML设置的Drawable配置到此
    fun setNativeDrawable(drawable: Drawable) {
        roundCirclePolicy.setNativeDrawable(drawable)
    }

}