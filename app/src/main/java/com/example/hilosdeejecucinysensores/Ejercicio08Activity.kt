package com.example.hilosdeejecucinysensores

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class Ejercicio08Activity : AppCompatActivity() {

    private lateinit var tvCoordenadas: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio08)

        tvCoordenadas = findViewById(R.id.tvCoordenadas)

        tvCoordenadas.setOnTouchListener { _, event ->
            val x = event.x.roundToInt()
            val y = event.y.roundToInt()

            tvCoordenadas.text = "Coordenadas Táctiles:\n\nX: $x px\nY: $y px"
            true
        }
    }
}
