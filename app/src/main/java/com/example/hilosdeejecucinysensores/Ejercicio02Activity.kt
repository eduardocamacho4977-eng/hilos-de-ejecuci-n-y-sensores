package com.example.hilosdeejecucinysensores

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.thread

class Ejercicio02Activity : AppCompatActivity() {

    private lateinit var btnCargar: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvProgreso: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio02)

        btnCargar = findViewById(R.id.btnCargar)
        progressBar = findViewById(R.id.progressBar)
        tvProgreso = findViewById(R.id.tvProgreso)

        btnCargar.setOnClickListener {
            btnCargar.isEnabled = false
            progressBar.progress = 0

            // Hilo secundario para simular progreso
            thread {
                for (i in 0..100) {
                    Thread.sleep(100) // 100 ms por cada porcentaje

                    runOnUiThread {
                        progressBar.progress = i
                        tvProgreso.text = "Progreso: $i%"
                    }
                }

                runOnUiThread {
                    btnCargar.isEnabled = true
                }
            }
        }
    }
}
