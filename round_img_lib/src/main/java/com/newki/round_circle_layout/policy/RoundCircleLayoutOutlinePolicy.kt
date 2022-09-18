package com.newki.round_circle_layout.policy

import android.annotation.TargetApi
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider


internal class RoundCircleLayoutOutlinePolicy(
    view: View, context: Context, attributeSet: AttributeSet?,
    attrs: IntArray,
    attrIndex: IntArray
) : AbsRoundCirclePolicy(view, context, attributeSet, attrs, attrIndex) {

    private var mBitmapShader: BitmapShader? = null
    private var mDrawableRect: RectF
    private var mBitmapPaint: Paint
    private var mShaderMatrix: Matrix
    private var mBitmapWidth = 0
    private var mBitmapHeight = 0

    init {
        mContainer.setWillNotDraw(false)  //运行ViewGroup绘制

        mDrawableRect = RectF()
        mBitmapPaint = Paint()
        mShaderMatrix = Matrix()
    }

    //设置画笔和BitmapShader等
    private fun setupBG() {

        mDrawableRect.set(calculateBounds())

        if (mRoundBackgroundDrawable != null && mRoundBackgroundBitmap != null) {

            mBitmapWidth = mRoundBackgroundBitmap!!.width
            mBitmapHeight = mRoundBackgroundBitmap!!.height

            mBitmapShader = BitmapShader(mRoundBackgroundBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

            if (mRoundBackgroundBitmap!!.width != 2) {
                updateShaderMatrix()
            }

            mBitmapPaint.isAntiAlias = true
            mBitmapPaint.shader = mBitmapShader

        }

    }

    override fun onDraw(canvas: Canvas?): Boolean {
        if (isCircleType) {

            canvas?.drawCircle(
                mDrawableRect.centerX(), mDrawableRect.centerY(),
                Math.min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f), mBitmapPaint
            )

        } else {
            if (mTopLeft > 0 || mTopRight > 0 || mBottomLeft > 0 || mBottomRight > 0) {
                //使用单独的圆角

                val path = Path()
                path.addRoundRect(
                    mDrawableRect, floatArrayOf(mTopLeft, mTopLeft, mTopRight, mTopRight, mBottomRight, mBottomRight, mBottomLeft, mBottomLeft),
                    Path.Direction.CW
                )
                canvas?.drawPath(path, mBitmapPaint)

            } else {
                //使用统一的圆角
                canvas?.drawRoundRect(mDrawableRect, mRoundRadius, mRoundRadius, mBitmapPaint)

            }
        }

        //是否需要super再绘制
        return true
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun beforeDispatchDraw(canvas: Canvas?) {
        //5.0版本以上，采用ViewOutlineProvider来裁剪view
        mContainer.clipToOutline = true
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun afterDispatchDraw(canvas: Canvas?) {
        //5.0版本以上，采用ViewOutlineProvider来裁剪view
        mContainer.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {

                if (isCircleType) {
                    //如果是圆形裁剪圆形
                    val bounds = Rect()
                    calculateBounds().roundOut(bounds)
                    outline.setRoundRect(bounds, bounds.width() / 2.0f)
//                    outline.setOval(0, 0, mContainer.width, mContainer.height);  //两种方法都可以

                } else {
                    //如果是圆角-裁剪圆角
                    if (mTopLeft > 0 || mTopRight > 0 || mBottomLeft > 0 || mBottomRight > 0) {
                        //如果是单独的圆角
                        val path = Path()
                        path.addRoundRect(
                            calculateBounds(),
                            floatArrayOf(mTopLeft, mTopLeft, mTopRight, mTopRight, mBottomRight, mBottomRight, mBottomLeft, mBottomLeft),
                            Path.Direction.CCW
                        )

                        //不支持2阶的曲线
                        outline.setConvexPath(path)

                    } else {
                        //如果是统一圆角
                        outline.setRoundRect(0, 0, mContainer.width, mContainer.height, mRoundRadius)
                    }

                }
            }
        }
    }

    private fun updateShaderMatrix() {
        var scale = 1.0f
        var dx = 0f
        var dy = 0f

        mShaderMatrix.set(null)

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / mBitmapHeight.toFloat()
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f
        } else {
            scale = mDrawableRect.width() / mBitmapWidth.toFloat()
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f
        }

        mShaderMatrix.setScale(scale, scale)

        if (isBGCenterCrop) {
            mShaderMatrix.postTranslate((dx + 0.5f).toInt() + mDrawableRect.left, (dy + 0.5f).toInt() + mDrawableRect.top)
        }

        mBitmapShader?.let {
            it.setLocalMatrix(mShaderMatrix)
        }
    }

    override fun onLayout(left: Int, top: Int, right: Int, bottom: Int) {
        setupBG()
    }

    //手动设置背景的设置
    override fun setBackground(background: Drawable?) {
        setRoundBackgroundDrawable(background)
    }

    override fun setBackgroundColor(color: Int) {
        val drawable = ColorDrawable(color)
        setRoundBackgroundDrawable(drawable)
    }

    override fun setBackgroundResource(resid: Int) {
        val drawable: Drawable = mContainer.context.resources.getDrawable(resid)
        setRoundBackgroundDrawable(drawable)
    }

    override fun setBackgroundDrawable(background: Drawable?) {
        setRoundBackgroundDrawable(background)
    }

    //重新设置Drawable
    private fun setRoundBackgroundDrawable(drawable: Drawable?) {
        mRoundBackgroundDrawable = drawable
        mRoundBackgroundBitmap = getBitmapFromDrawable(mRoundBackgroundDrawable)

        setupBG()

        //重绘
        mContainer.invalidate()
    }

}