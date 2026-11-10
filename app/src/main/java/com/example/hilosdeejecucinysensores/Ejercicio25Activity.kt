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

class Ejercicio25Activity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var proximitySensor: Sensor? = null
    private var lightSensor: Sensor? = null

    private lateinit var layoutCombo: RelativeLayout
    private lateinit var tvComboInfo: TextView

    private var ultimaDistancia = 10f
    private var ultimoLux = 500f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio25)

        layoutCombo = findViewById(R.id.layoutCombo)
        tvComboInfo = findViewById(R.id.tvComboInfo)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    override fun onResume() {
        super.onResume()
        proximitySensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
        lightSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_PROXIMITY) ultimaDistancia = it.values[0]
            if (it.sensor.type == Sensor.TYPE_LIGHT) ultimoLux = it.values[0]

            val esOscuro = ultimoLux < 10f
            val esCerca = ultimaDistancia < 5f

            if (esOscuro && esCerca) {
                layoutCombo.setBackgroundColor(Color.BLACK)
                tvComboInfo.setTextColor(Color.WHITE)
                tvComboInfo.text = "Estado: Oscuro y Cerca\n(Fondo NEGRO)\n\nLux: $ultimoLux | Prox: $ultimaDistancia cm"
            } else if (!esOscuro && !esCerca) {
                layoutCombo.setBackgroundColor(Color.WHITE)
                tvComboInfo.setTextColor(Color.BLACK)
                tvComboInfo.text = "Estado: Claro y Lejos\n(Fondo BLANCO)\n\nLux: $ultimoLux | Prox: $ultimaDistancia cm"
            } else {
                layoutCombo.setBackgroundColor(Color.BLUE)
                tvComboInfo.setTextColor(Color.WHITE)
                tvComboInfo.text = "Estado: Intermedio\n(Fondo AZUL)\n\nLux: $ultimoLux | Prox: $ultimaDistancia cm"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
