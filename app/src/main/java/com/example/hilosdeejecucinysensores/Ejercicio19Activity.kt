package com.example.hilosdeejecucinysensores

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Ejercicio19Activity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var proximitySensor: Sensor? = null
    private lateinit var layoutProximidad: RelativeLayout
    private lateinit var tvEstadoProximidad: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio19)

        layoutProximidad = findViewById(R.id.layoutProximidad)
        tvEstadoProximidad = findViewById(R.id.tvEstadoProximidad)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if (proximitySensor == null) {
            tvEstadoProximidad.text = "Sensor de proximidad NO disponible."
        }
    }

    override fun onResume() {
        super.onResume()
        proximitySensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_PROXIMITY) {
                val distancia = it.values[0]
                val maxRange = proximitySensor?.maximumRange ?: 5f

                if (distancia < maxRange) {
                    layoutProximidad.setBackgroundColor(Color.RED) // Objeto CERCA
                    tvEstadoProximidad.text = "¡OBJETO CERCA!\n\nDistancia: $distancia cm"
                } else {
                    layoutProximidad.setBackgroundColor(Color.GREEN) // Objeto LEJOS
                    tvEstadoProximidad.text = "OBJETO LEJOS\n\nDistancia: $distancia cm"
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
