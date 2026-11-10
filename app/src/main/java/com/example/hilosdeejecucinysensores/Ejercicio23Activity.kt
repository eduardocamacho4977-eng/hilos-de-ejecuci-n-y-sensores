package com.example.hilosdeejecucinysensores

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

class Ejercicio23Activity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelSensor: Sensor? = null

    private lateinit var contenedorTilt: FrameLayout
    private lateinit var vistaPelotaRed: View
    private var posX = 0f
    private var posY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio23)

        contenedorTilt = findViewById(R.id.contenedorTilt)
        vistaPelotaRed = findViewById(R.id.vistaPelotaRed)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onResume() {
        super.onResume()
        accelSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME) }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val ax = it.values[0]
                val ay = it.values[1]

                posX -= ax * 4f
                posY += ay * 4f

                val maxX = (contenedorTilt.width / 2 - vistaPelotaRed.width / 2).toFloat().coerceAtLeast(0f)
                val maxY = (contenedorTilt.height / 2 - vistaPelotaRed.height / 2).toFloat().coerceAtLeast(0f)

                posX = posX.coerceIn(-maxX, maxX)
                posY = posY.coerceIn(-maxY, maxY)

                vistaPelotaRed.translationX = posX
                vistaPelotaRed.translationY = posY
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
