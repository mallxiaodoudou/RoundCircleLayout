package com.newki.round_circle_layout

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView
import com.newki.circle_round.R
import com.newki.round_circle_layout.view.IRoundCircleView
import com.newki.round_circle_layout.view.RoundCircleViewImpl


class RoundCircleScrollView : ScrollView, IRoundCircleView {

    private lateinit var roundCircleViewImpl: RoundCircleViewImpl

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(this, context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(this, context, attrs)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        roundCircleViewImpl.onLayout(changed, left, top, right, bottom)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        roundCircleViewImpl.beforeDispatchDraw(canvas)
        super.dispatchDraw(canvas)
        roundCircleViewImpl.afterDispatchDraw(canvas)
    }

    override fun onDraw(canvas: Canvas?) {
        if (roundCircleViewImpl.onDraw(canvas)) {
            super.onDraw(canvas)
        }
    }

    private fun init(view: View, context: Context, attributeSet: AttributeSet?) {
        roundCircleViewImpl = RoundCircleViewImpl(
            view,
            context,
            attributeSet,
            R.styleable.RoundCircleScrollView,
            intArrayOf(
                R.styleable.RoundCircleScrollView_is_circle,
                R.styleable.RoundCircleScrollView_round_radius,
                R.styleable.RoundCircleScrollView_topLeft,
                R.styleable.RoundCircleScrollView_topRight,
                R.styleable.RoundCircleScrollView_bottomLeft,
                R.styleable.RoundCircleScrollView_bottomRight,
                R.styleable.RoundCircleScrollView_round_circle_background_color,
                R.styleable.RoundCircleScrollView_round_circle_background_drawable,
                R.styleable.RoundCircleScrollView_is_bg_center_crop,
            )

        )
        nativeBgDrawable?.let {
            roundCircleViewImpl.setNativeDrawable(it)
        }
    }

    private var nativeBgDrawable: Drawable? = null
    override fun setBackground(background: Drawable?) {
        if (!this::roundCircleViewImpl.isInitialized) {
            nativeBgDrawable = background
        } else {
            roundCircleViewImpl.setBackground(background)
        }
    }

    override fun setBackgroundColor(color: Int) {
        if (!this::roundCircleViewImpl.isInitialized) {
            nativeBgDrawable = ColorDrawable(color)
        } else {
            roundCircleViewImpl.setBackground(background)
        }
    }

    override fun setBackgroundResource(resid: Int) {
        if (!this::roundCircleViewImpl.isInitialized) {
            nativeBgDrawable = context.resources.getDrawable(resid)
        } else {
            roundCircleViewImpl.setBackground(background)
        }
    }

    override fun setBackgroundDrawable(background: Drawable?) {
        if (!this::roundCircleViewImpl.isInitialized) {
            nativeBgDrawable = background
        } else {
            roundCircleViewImpl.setBackground(background)
        }
    }
}