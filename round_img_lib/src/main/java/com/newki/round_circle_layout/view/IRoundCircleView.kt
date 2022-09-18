package com.newki.round_circle_layout.view

import android.graphics.drawable.Drawable


interface IRoundCircleView {

    fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int)

     fun setBackground(background: Drawable?)

     fun setBackgroundColor(color: Int)

     fun setBackgroundResource(resid: Int)

     fun setBackgroundDrawable(background: Drawable?)

}