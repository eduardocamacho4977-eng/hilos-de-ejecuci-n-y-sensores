package com.example.hilosdeejecucinysensores

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class Ejercicio21Activity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelSensor: Sensor? = null
    private var magSensor: Sensor? = null

    private val gravityValues = FloatArray(3)
    private val geomagneticValues = FloatArray(3)
    private var hasGravity = false
    private var hasGeomagnetic = false

    private lateinit var tvOrientacionValores: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio21)

        tvOrientacionValores = findViewById(R.id.tvOrientacionValores)

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

                    val azimut = Math.toDegrees(orientation[0].toDouble()).roundToInt()
                    val pitch = Math.toDegrees(orientation[1].toDouble()).roundToInt()
                    val roll = Math.toDegrees(orientation[2].toDouble()).roundToInt()

                    tvOrientacionValores.text = "Orientación del Dispositivo:\n\n" +
                            "Azimut (Z): $azimut°\n" +
                            "Pitch (X): $pitch°\n" +
                            "Roll (Y): $roll°"
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
