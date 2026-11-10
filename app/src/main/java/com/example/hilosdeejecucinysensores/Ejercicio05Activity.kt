package com.example.hilosdeejecucinysensores

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Ejercicio05Activity : AppCompatActivity() {

    private lateinit var btnCorrutina: Button
    private lateinit var tvConsola: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio05)

        btnCorrutina = findViewById(R.id.btnCorrutina)
        tvConsola = findViewById(R.id.tvConsola)

        btnCorrutina.setOnClickListener {
            btnCorrutina.isEnabled = false
            tvConsola.text = "Corrutina iniciada:\n\n"

            // Lanzar corrutina vinculada al ciclo de vida del Activity
            lifecycleScope.launch {
                for (i in 1..5) {
                    delay(1000) // Pausa de 1 segundo sin bloquear la interfaz
                    tvConsola.append("Mensaje $i desde la corrutina (Segundo $i)\n")
                }
                tvConsola.append("\n¡Corrutina finalizada exitosamente!")
                btnCorrutina.isEnabled = true
            }
        }
    }
}
