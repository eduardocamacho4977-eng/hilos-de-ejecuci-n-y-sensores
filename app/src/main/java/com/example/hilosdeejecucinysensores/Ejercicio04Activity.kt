package com.example.hilosdeejecucinysensores

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// Clase MiTarea que realiza el cálculo en un hilo secundario
class MiTareaClase(private val n: Long, private val onResultado: (Long) -> Unit) {
    fun ejecutar() {
        kotlin.concurrent.thread {
            var suma = 0L
            for (i in 1..n) {
                suma += i
                Thread.sleep(20) // Simula tiempo de procesamiento
            }
            onResultado(suma)
        }
    }
}

class Ejercicio04Activity : AppCompatActivity() {

    private lateinit var etNumeroN: EditText
    private lateinit var btnCalcular: Button
    private lateinit var tvResultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicio04)

        etNumeroN = findViewById(R.id.etNumeroN)
        btnCalcular = findViewById(R.id.btnCalcular)
        tvResultado = findViewById(R.id.tvResultado)

        btnCalcular.setOnClickListener {
            val n = etNumeroN.text.toString().toLongOrNull() ?: 10L
            tvResultado.text = "Calculando en segundo plano..."

            val tarea = MiTareaClase(n) { resultado ->
                runOnUiThread {
                    tvResultado.text = "La suma de los primeros $n números es: $resultado"
                }
            }
            tarea.ejecutar()
        }
    }
}
