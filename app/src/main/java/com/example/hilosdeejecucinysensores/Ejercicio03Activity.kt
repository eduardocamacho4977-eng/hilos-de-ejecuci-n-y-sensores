package com.example.hilosdeejecucinysensores

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.thread

class Ejercicio03Activity : AppCompatActivity() {

    private lateinit var btnEjecutar: Button
    private lateinit var tvSalida: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio03)

        btnEjecutar = findViewById(R.id.btnEjecutar)
        tvSalida = findViewById(R.id.tvSalida)

        btnEjecutar.setOnClickListener {
            tvSalida.text = "Iniciando hilos simultáneos...\n\n"

            // Hilo 1: Números Pares
            thread {
                for (i in 2..10 step 2) {
                    Thread.sleep(300)
                    runOnUiThread { tvSalida.append("[Hilo Pares]: $i\n") }
                }
            }

            // Hilo 2: Números Impares
            thread {
                for (i in 1..9 step 2) {
                    Thread.sleep(400)
                    runOnUiThread { tvSalida.append("[Hilo Impares]: $i\n") }
                }
            }

            // Hilo 3: Letras del Alfabeto
            thread {
                for (ch in 'A'..'E') {
                    Thread.sleep(350)
                    runOnUiThread { tvSalida.append("[Hilo Letras]: $ch\n") }
                }
            }
        }
    }
}
