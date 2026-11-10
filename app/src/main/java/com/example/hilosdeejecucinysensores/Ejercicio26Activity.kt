package com.example.hilosdeejecucinysensores

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt

class Ejercicio26Activity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelSensor: Sensor? = null

    private lateinit var layoutShake: LinearLayout
    private lateinit var tvShakeMensaje: TextView
    private var ultimoTiempoSacudida = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio26)

        layoutShake = findViewById(R.id.layoutShake)
        tvShakeMensaje = findViewById(R.id.tvShakeMensaje)

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
                val x = it.values[0]
                val y = it.values[1]
                val z = it.values[2]

                val gX = x / SensorManager.GRAVITY_EARTH
                val gY = y / SensorManager.GRAVITY_EARTH
                val gZ = z / SensorManager.GRAVITY_EARTH
                val fuerzaG = sqrt(gX * gX + gY * gY + gZ * gZ)

                if (fuerzaG > 2.5f) { // Umbral de sacudida
                    val tiempoActual = System.currentTimeMillis()
                    if (tiempoActual - ultimoTiempoSacudida > 1000) {
                        ultimoTiempoSacudida = tiempoActual
                        tvShakeMensaje.text = "¡¡¡¡¡¡¡¡ ¡AGITADO! !!!!!!!\n\nFuerza G: ${"%.2f".format(fuerzaG)}"
                        layoutShake.setBackgroundColor(Color.YELLOW)
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
