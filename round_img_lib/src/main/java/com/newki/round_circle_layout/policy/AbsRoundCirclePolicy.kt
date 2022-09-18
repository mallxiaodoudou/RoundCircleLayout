package com.newki.round_circle_layout.policy

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View


internal abstract class AbsRoundCirclePolicy(
    view: View,
    context: Context,
    attributeSet: AttributeSet?,
    attrs: IntArray,
    attrIndex: IntArray
) : IRoundCirclePolicy {

    var isCircleType = false
    var mRoundRadius = 0f
    var mTopLeft = 0f
    var mTopRight = 0f
    var mBottomLeft = 0f
    var mBottomRight = 0f
    var mRoundBackgroundDrawable: Drawable? = null
    var mRoundBackgroundBitmap: Bitmap? = null
    var isBGCenterCrop = true;

    val mContainer: View = view

    init {
        initialize(context, attributeSet, attrs, attrIndex)
    }

    private fun initialize(context: Context, attributeSet: AttributeSet?, attrs: IntArray, attrIndexs: IntArray) {
        val typedArray = context.obtainStyledAttributes(attributeSet, attrs)

        isCircleType = typedArray.getBoolean(attrIndexs[0], false)

        mRoundRadius = typedArray.getDimensionPixelOffset(attrIndexs[1], 0).toFloat()

        mTopLeft = typedArray.getDimensionPixelOffset(attrIndexs[2], 0).toFloat()
        mTopRight = typedArray.getDimensionPixelOffset(attrIndexs[3], 0).toFloat()
        mBottomLeft = typedArray.getDimensionPixelOffset(attrIndexs[4], 0).toFloat()
        mBottomRight = typedArray.getDimensionPixelOffset(attrIndexs[5], 0).toFloat()

        val roundBackgroundColor = typedArray.getColor(attrIndexs[6], Color.TRANSPARENT)
        mRoundBackgroundDrawable = ColorDrawable(roundBackgroundColor)
        mRoundBackgroundBitmap = getBitmapFromDrawable(mRoundBackgroundDrawable)

        if (typedArray.hasValue(attrIndexs[7])) {
            mRoundBackgroundDrawable = typedArray.getDrawable(attrIndexs[7])
            mRoundBackgroundBitmap = getBitmapFromDrawable(mRoundBackgroundDrawable)
        }

        isBGCenterCrop = typedArray.getBoolean(attrIndexs[8], true)

        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas?): Boolean {
        //是否需要super来绘制
        return true
    }

    override fun setNativeDrawable(drawable: Drawable) {
        mRoundBackgroundDrawable = drawable
        mRoundBackgroundBitmap = getBitmapFromDrawable(mRoundBackgroundDrawable)
    }

    //是否是自定义各自圆角
    override fun isCustomRound(): Boolean {
        return mTopLeft > 0 || mTopRight > 0 || mBottomLeft > 0 || mBottomRight > 0
    }

    protected fun calculateBounds(): RectF {
        // 没有处理Padding的逻辑
        return RectF(0f, 0f, mContainer.width.toFloat(), mContainer.height.toFloat())
    }

    protected fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }
        return if (drawable is BitmapDrawable) {
            drawable.bitmap

        } else try {
            val bitmap: Bitmap =
                if (drawable is ColorDrawable) {
                    Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888)
                } else {
                    Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                }
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}