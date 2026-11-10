package com.example.hilosdeejecucinysensores

import android.os.Bundle
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Ejercicio07Activity : AppCompatActivity() {

    private lateinit var tvEstadoTactil: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio07)

        tvEstadoTactil = findViewById(R.id.tvEstadoTactil)

        // Escuchar eventos de toque sobre el TextView
        tvEstadoTactil.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> tvEstadoTactil.text = "Pulsación iniciada\n(ACTION_DOWN)"
                MotionEvent.ACTION_MOVE -> tvEstadoTactil.text = "Desplazando el dedo\n(ACTION_MOVE)"
                MotionEvent.ACTION_UP -> tvEstadoTactil.text = "Pulsación finalizada\n(ACTION_UP)"
            }
            true
        }
    }
}
