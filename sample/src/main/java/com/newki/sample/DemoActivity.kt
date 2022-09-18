package com.newki.sample

import android.os.Bundle
import android.view.ViewGroup

import androidx.appcompat.app.AppCompatActivity
import com.newki.round_circle_layout.RoundCircleLinearLayout

class DemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_demo)

        val layout_3 = findViewById<RoundCircleLinearLayout>(R.id.layout_3)

        findViewById<ViewGroup>(R.id.layout_2).setOnClickListener {

            val drawable = resources.getDrawable(R.drawable.chengxiao)

            it.background = drawable

            layout_3.background = drawable
        }


    }

}