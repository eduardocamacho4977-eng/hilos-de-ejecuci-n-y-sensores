package com.example.hilosdeejecucinysensores

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import kotlin.math.roundToInt

class Ejercicio12Activity : AppCompatActivity() {

    private lateinit var tvGestos: TextView
    private lateinit var detectorGestos: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio12)

        tvGestos = findViewById(R.id.tvGestos)

        detectorGestos = GestureDetectorCompat(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                tvGestos.text = "Gesto Detectado:\n\n¡TOQUE SIMPLE (TAP)!"
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                tvGestos.text = "Gesto Detectado:\n\n¡TOQUE PROLONGADO (LONG PRESS)!"
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                tvGestos.text = "Gesto Detectado:\n\n¡DESLIZAMIENTO RÁPIDO (FLING)!\nVelocidad X: ${velocityX.roundToInt()} px/s"
                return true
            }
        })

        tvGestos.setOnTouchListener { _, event ->
            detectorGestos.onTouchEvent(event)
            true
        }
    }
}
