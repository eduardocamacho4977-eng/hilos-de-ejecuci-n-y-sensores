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

class Ejercicio20Activity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private lateinit var layoutLuz: RelativeLayout
    private lateinit var tvIntensidadLuz: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio20)

        layoutLuz = findViewById(R.id.layoutLuz)
        tvIntensidadLuz = findViewById(R.id.tvIntensidadLuz)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        if (lightSensor == null) {
            tvIntensidadLuz.text = "Sensor de luz NO disponible."
        }
    }

    override fun onResume() {
        super.onResume()
        lightSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_LIGHT) {
                val lux = it.values[0]
                tvIntensidadLuz.text = "Intensidad de Luz:\n\n${"%.1f".format(lux)} LUX"

                // Ajustar el tono de gris del fondo dinámicamente según los Lux
                val valorGris = (lux / 1000f * 255).coerceIn(0f, 255f).toInt()
                layoutLuz.setBackgroundColor(Color.rgb(valorGris, valorGris, valorGris))

                if (valorGris > 128) {
                    tvIntensidadLuz.setTextColor(Color.BLACK)
                } else {
                    tvIntensidadLuz.setTextColor(Color.WHITE)
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
