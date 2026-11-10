package com.example.hilosdeejecucinysensores

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Ejercicio11Activity : AppCompatActivity() {

    private lateinit var vistaDibujo: DrawingView
    private lateinit var btnLimpiar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio11)

        vistaDibujo = findViewById(R.id.vistaDibujo)
        btnLimpiar = findViewById(R.id.btnLimpiar)

        btnLimpiar.setOnClickListener {
            vistaDibujo.clearCanvas()
        }
    }
}
