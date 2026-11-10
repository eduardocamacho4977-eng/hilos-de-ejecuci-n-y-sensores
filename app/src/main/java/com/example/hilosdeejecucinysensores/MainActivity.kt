package com.example.hilosdeejecucinysensores

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerEjercicios: Spinner
    private lateinit var btnAbrirIndependiente: Button
    private lateinit var contenedor: FrameLayout
    private lateinit var runner: EjercicioRunner

    private val actividadesEjercicios = arrayOf(
        Ejercicio01Activity::class.java,
        Ejercicio02Activity::class.java,
        Ejercicio03Activity::class.java,
        Ejercicio04Activity::class.java,
        Ejercicio05Activity::class.java,
        Ejercicio06Activity::class.java,
        Ejercicio07Activity::class.java,
        Ejercicio08Activity::class.java,
        Ejercicio09Activity::class.java,
        Ejercicio10Activity::class.java,
        Ejercicio11Activity::class.java,
        Ejercicio12Activity::class.java,
        Ejercicio13Activity::class.java,
        Ejercicio14Activity::class.java,
        Ejercicio15Activity::class.java,
        Ejercicio16Activity::class.java,
        Ejercicio17Activity::class.java,
        Ejercicio18Activity::class.java,
        Ejercicio19Activity::class.java,
        Ejercicio20Activity::class.java,
        Ejercicio21Activity::class.java,
        Ejercicio22Activity::class.java,
        Ejercicio23Activity::class.java,
        Ejercicio24Activity::class.java,
        Ejercicio25Activity::class.java,
        Ejercicio26Activity::class.java,
        Ejercicio27Activity::class.java,
        Ejercicio28Activity::class.java
    )

    private val nombresEjercicios = arrayOf(
        "Ejercicio 1: Hilo básico 1 al 10 (Thread)",
        "Ejercicio 2: Carga progresiva (ProgressBar)",
        "Ejercicio 3: 3 Hilos simultáneos (Pares, Impares, Letras)",
        "Ejercicio 4: Clase MiTarea (Suma 1..N)",
        "Ejercicio 5: Corrutina básica (Mensajes 5s)",
        "Ejercicio 6: Sensor simulado con Corrutinas",
        "Ejercicio 7: Eventos táctiles (Down, Move, Up)",
        "Ejercicio 8: Coordenadas táctiles (X, Y)",
        "Ejercicio 9: Vista cambia de color al tocar",
        "Ejercicio 10: Arrastrar ImageView libremente",
        "Ejercicio 11: Lienzo de dibujo (Trazos)",
        "Ejercicio 12: GestureDetector (Tap, LongPress, Fling)",
        "Ejercicio 13: Multitouch (Conteo y posiciones)",
        "Ejercicio 14: Dibujo Multitouch multicolor",
        "Ejercicio 15: Pinch to Zoom (Escalar imagen)",
        "Ejercicio 16: Rotación con 2 dedos",
        "Ejercicio 17: Puntos táctiles temporales",
        "Ejercicio 18: Control total (Drag + Zoom + Rotate)",
        "Ejercicio 19: Sensor de Proximidad (Cerca/Lejos)",
        "Ejercicio 20: Sensor de Luz (Lux)",
        "Ejercicio 21: Orientación (Azimut, Pitch, Roll)",
        "Ejercicio 22: Brújula magnética",
        "Ejercicio 23: Inclinación (Mover pelota con acelerómetro)",
        "Ejercicio 24: Contador de pasos (Step Detector)",
        "Ejercicio 25: Combo Proximidad + Luz",
        "Ejercicio 26: Detector de Sacudida (Shake)",
        "Ejercicio 27: Posición del dispositivo",
        "Ejercicio 28: Dashboard de Actividad (RecyclerView)"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerEjercicios = findViewById(R.id.spinnerEjercicios)
        btnAbrirIndependiente = findViewById(R.id.btnAbrirIndependiente)
        contenedor = findViewById(R.id.contenedorEjercicios)
        runner = EjercicioRunner(this, contenedor)

        pedirPermisos()

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresEjercicios)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEjercicios.adapter = adapter

        spinnerEjercicios.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                runner.loadEjercicio(position + 1)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnAbrirIndependiente.setOnClickListener {
            val pos = spinnerEjercicios.selectedItemPosition
            if (pos in actividadesEjercicios.indices) {
                val intent = Intent(this, actividadesEjercicios[pos])
                startActivity(intent)
            }
        }
    }

    private fun pedirPermisos() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),
                    100
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        runner.stopSensors()
    }

    override fun onResume() {
        super.onResume()
        if (::spinnerEjercicios.isInitialized) {
            val currentPos = spinnerEjercicios.selectedItemPosition
            if (currentPos >= 0) {
                runner.loadEjercicio(currentPos + 1)
            }
        }
    }
}
