package com.example.hilosdeejecucinysensores

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.sqrt

class Ejercicio28Activity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelSensor: Sensor? = null
    private var stepSensor: Sensor? = null

    private lateinit var rvHistorialActividades: RecyclerView
    private lateinit var btnRegistrarEstado: Button

    private val listaRegistros = mutableListOf<RegistroActividad>()
    private lateinit var adapter: ActividadAdapter

    private var contadorPasos = 0
    private var nivelMovimiento = "Bajo (Reposo)"
    private var orientacionActual = "Desconocida"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio28)

        btnRegistrarEstado = findViewById(R.id.btnRegistrarEstado)
        rvHistorialActividades = findViewById(R.id.rvHistorialActividades)

        adapter = ActividadAdapter(listaRegistros)
        rvHistorialActividades.layoutManager = LinearLayoutManager(this)
        rvHistorialActividades.adapter = adapter

        btnRegistrarEstado.setOnClickListener {
            val horaActual = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            listaRegistros.add(0, RegistroActividad(horaActual, contadorPasos, nivelMovimiento, orientacionActual))
            adapter.notifyItemInserted(0)
            rvHistorialActividades.scrollToPosition(0)
        }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
            ?: sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    override fun onResume() {
        super.onResume()
        accelSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
        stepSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
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
                val mag = sqrt(x * x + y * y + z * z)

                nivelMovimiento = if (mag > 13f) "Alto (Sacudiendo)" else if (mag > 10.5f) "Medio (Caminando)" else "Bajo (Reposo)"

                orientacionActual = when {
                    z > 8.0f -> "Boca Arriba"
                    z < -8.0f -> "Boca Abajo"
                    else -> "Vertical / Inclinado"
                }
            } else if (it.sensor.type == Sensor.TYPE_STEP_DETECTOR || it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                contadorPasos++
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
