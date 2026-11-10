package com.example.hilosdeejecucinysensores

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class Ejercicio13Activity : AppCompatActivity() {

    private lateinit var tvMultitouchInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio13)

        tvMultitouchInfo = findViewById(R.id.tvMultitouchInfo)

        tvMultitouchInfo.setOnTouchListener { _, event ->
            val pointerCount = event.pointerCount
            val sb = StringBuilder("Dedos detectados en pantalla: $pointerCount\n\n")

            for (i in 0 until pointerCount) {
                val id = event.getPointerId(i)
                val x = event.getX(i).roundToInt()
                val y = event.getY(i).roundToInt()
                sb.append("Dedo #$id -> X: $x px, Y: $y px\n")
            }

            tvMultitouchInfo.text = sb.toString()
            true
        }
    }
}
