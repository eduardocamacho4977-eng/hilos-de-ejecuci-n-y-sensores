package com.example.hilosdeejecucinysensores

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class Ejercicio06Activity : AppCompatActivity() {

    private lateinit var btnIniciarSim: Button
    private lateinit var btnDetenerSim: Button
    private lateinit var tvValorSensor: TextView
    private var jobSensor: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio06)

        btnIniciarSim = findViewById(R.id.btnIniciarSim)
        btnDetenerSim = findViewById(R.id.btnDetenerSim)
        tvValorSensor = findViewById(R.id.tvValorSensor)

        btnIniciarSim.setOnClickListener {
            btnIniciarSim.isEnabled = false
            btnDetenerSim.isEnabled = true

            // Iniciar corrutina periódica cada 500 ms
            jobSensor = lifecycleScope.launch {
                while (isActive) {
                    delay(500)
                    val datoAleatorio = Random.nextDouble(10.0, 100.0)
                    tvValorSensor.text = "Valor Sensor: ${"%.2f".format(datoAleatorio)} units"
                }
            }
        }

        btnDetenerSim.setOnClickListener {
            jobSensor?.cancel() // Cancelar la corrutina
            btnIniciarSim.isEnabled = true
            btnDetenerSim.isEnabled = false
            tvValorSensor.text = "Simulación Detenida"
        }
    }
}
