package com.example.hilosdeejecucinysensores

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class Ejercicio22Activity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelSensor: Sensor? = null
    private var magSensor: Sensor? = null

    private val gravityValues = FloatArray(3)
    private val geomagneticValues = FloatArray(3)
    private var hasGravity = false
    private var hasGeomagnetic = false

    private lateinit var imgBrujula: ImageView
    private lateinit var tvAzimut: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio22)

        imgBrujula = findViewById(R.id.imgBrujula)
        tvAzimut = findViewById(R.id.tvAzimut)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    override fun onResume() {
        super.onResume()
        accelSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
        magSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(it.values, 0, gravityValues, 0, 3)
                hasGravity = true
            } else if (it.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(it.values, 0, geomagneticValues, 0, 3)
                hasGeomagnetic = true
            }

            if (hasGravity && hasGeomagnetic) {
                val rMatrix = FloatArray(9)
                val iMatrix = FloatArray(9)
                if (SensorManager.getRotationMatrix(rMatrix, iMatrix, gravityValues, geomagneticValues)) {
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(rMatrix, orientation)

                    val azimutDeg = Math.toDegrees(orientation[0].toDouble()).toFloat()

                    // Rota la imagen de la brújula apuntando hacia el Norte Magnético
                    imgBrujula.rotation = -azimutDeg
                    tvAzimut.text = "Azimut Norte Magnético:\n${azimutDeg.roundToInt()}°"
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
