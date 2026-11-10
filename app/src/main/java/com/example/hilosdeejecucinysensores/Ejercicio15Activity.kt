package com.example.hilosdeejecucinysensores

import android.os.Bundle
import android.view.ScaleGestureDetector
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Ejercicio15Activity : AppCompatActivity() {

    private lateinit var contenedorPinch: FrameLayout
    private lateinit var imgZoom: ImageView
    private var scaleFactor = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio15)

        contenedorPinch = findViewById(R.id.contenedorPinch)
        imgZoom = findViewById(R.id.imgZoom)

        val scaleDetector = ScaleGestureDetector(this, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scaleFactor *= detector.scaleFactor
                scaleFactor = scaleFactor.coerceIn(0.5f, 4.0f) // Limitar escala
                imgZoom.scaleX = scaleFactor
                imgZoom.scaleY = scaleFactor
                return true
            }
        })

        contenedorPinch.setOnTouchListener { _, event ->
            scaleDetector.onTouchEvent(event)
            true
        }
    }
}
