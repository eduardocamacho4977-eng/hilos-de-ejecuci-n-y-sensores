package com.example.hilosdeejecucinysensores

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs

class Ejercicio27Activity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelSensor: Sensor? = null
    private lateinit var tvPosicionDispositivo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio27)

        tvPosicionDispositivo = findViewById(R.id.tvPosicionDispositivo)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onResume() {
        super.onResume()
        accelSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val x = it.values[0]
                val y = it.values[1]
                val z = it.values[2]

                val posicion = when {
                    z > 8.0f -> "Boca Arriba"
                    z < -8.0f -> "Boca Abajo"
                    abs(y) > 7.0f || abs(x) > 7.0f -> "Posición Vertical"
                    else -> "Inclinación Diagonal"
                }

                tvPosicionDispositivo.text = "Posición del Dispositivo:\n\n¡$posicion!\n\n(X: ${"%.1f".format(x)}, Y: ${"%.1f".format(y)}, Z: ${"%.1f".format(z)})"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
