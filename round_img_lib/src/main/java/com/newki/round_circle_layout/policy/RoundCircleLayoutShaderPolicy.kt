package com.newki.round_circle_layout.policy

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View


internal class RoundCircleLayoutShaderPolicy(
    view: View, context: Context, attributeSet: AttributeSet?,
    attrs: IntArray,
    attrIndex: IntArray
) : AbsRoundCirclePolicy(view, context, attributeSet, attrs, attrIndex) {

    private var mBitmapShader: BitmapShader? = null
    private lateinit var mPath: Path
    private lateinit var mDrawableRect: RectF
    private lateinit var mBitmapPaint: Paint
    private lateinit var mShaderMatrix: Matrix
    private var mBitmapWidth = 0
    private var mBitmapHeight = 0

    init {
        initViewData()
    }

    private fun initViewData() {
        mContainer.setWillNotDraw(false)  //运行ViewGroup绘制

        mDrawableRect = RectF()
        mPath = Path()
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

    override fun beforeDispatchDraw(canvas: Canvas?) {
        canvas?.clipPath(mPath)
    }

    override fun afterDispatchDraw(canvas: Canvas?) {
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

        setupRoundPath()
    }

    //裁剪的路径
    private fun setupRoundPath() {
        mPath.reset()

        if (isCircleType) {

            mPath.addOval(0f, 0f, mContainer.width.toFloat(), mContainer.height.toFloat(), Path.Direction.CCW)

        } else {

            //如果是圆角-裁剪圆角
            if (mTopLeft > 0 || mTopRight > 0 || mBottomLeft > 0 || mBottomRight > 0) {

                mPath.addRoundRect(
                    calculateBounds(),
                    floatArrayOf(mTopLeft, mTopLeft, mTopRight, mTopRight, mBottomRight, mBottomRight, mBottomLeft, mBottomLeft),
                    Path.Direction.CCW
                )

            } else {

                mPath.addRoundRect(mDrawableRect, mRoundRadius, mRoundRadius, Path.Direction.CCW)
            }

        }

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