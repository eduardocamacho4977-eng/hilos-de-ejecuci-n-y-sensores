package com.example.hilosdeejecucinysensores

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Ejercicio14Activity : AppCompatActivity() {

    private lateinit var vistaDibujoMulti: MultiTouchDrawingView
    private lateinit var btnLimpiarMulti: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio14)

        vistaDibujoMulti = findViewById(R.id.vistaDibujoMulti)
        btnLimpiarMulti = findViewById(R.id.btnLimpiarMulti)

        btnLimpiarMulti.setOnClickListener {
            vistaDibujoMulti.clearCanvas()
        }
    }
}
