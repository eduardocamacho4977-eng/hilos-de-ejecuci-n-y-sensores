package com.example.hilosdeejecucinysensores

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.thread

class Ejercicio01Activity : AppCompatActivity() {

    private lateinit var btnIniciar: Button
    private lateinit var tvSalida: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio01)

        btnIniciar = findViewById(R.id.btnIniciar)
        tvSalida = findViewById(R.id.tvSalida)

        btnIniciar.setOnClickListener {
            btnIniciar.isEnabled = false
            tvSalida.text = "Hilo iniciado...\n"

            // Crear e iniciar un hilo de ejecución secundario (Thread)
            thread {
                for (i in 1..10) {
                    Thread.sleep(1000) // Espera 1 segundo en segundo plano

                    // Actualizar la interfaz de usuario desde el hilo principal
                    runOnUiThread {
                        tvSalida.append("Número: $i\n")
                    }
                }

                runOnUiThread {
                    tvSalida.append("\n¡Hilo finalizado!")
                    btnIniciar.isEnabled = true
                }
            }
        }
    }
}
