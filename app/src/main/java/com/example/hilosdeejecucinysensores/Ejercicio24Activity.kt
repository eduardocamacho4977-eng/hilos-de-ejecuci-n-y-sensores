package com.example.hilosdeejecucinysensores

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Ejercicio24Activity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var totalPasos = 0

    private lateinit var tvConteoPasos: TextView
    private lateinit var btnResetPasos: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio24)

        tvConteoPasos = findViewById(R.id.tvConteoPasos)
        btnResetPasos = findViewById(R.id.btnResetPasos)

        btnResetPasos.setOnClickListener {
            totalPasos = 0
            tvConteoPasos.text = "Pasos Detectados:\n0"
        }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
            ?: sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            tvConteoPasos.text = "Sensor de pasos NO disponible en este dispositivo."
        }
    }

    override fun onResume() {
        super.onResume()
        stepSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            totalPasos++
            tvConteoPasos.text = "Pasos Detectados:\n$totalPasos"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
