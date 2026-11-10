package com.example.hilosdeejecucinysensores

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random

// Ejercicio 4: Clase MiTarea para operaciones matemáticas en hilo
class MiTarea(private val n: Long, private val onResult: (Long) -> Unit) {
    fun ejecutar() {
        thread {
            var suma = 0L
            for (i in 1..n) {
                suma += i
                SystemClock.sleep(50)
            }
            onResult(suma)
        }
    }
}

// Ejercicio 28: Data Model y Adapter para RecyclerView
data class RegistroActividad(val timestamp: String, val pasos: Int, val movimiento: String, val orientacion: String)

class ActividadAdapter(private val lista: List<RegistroActividad>) : RecyclerView.Adapter<ActividadAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtInfo: TextView = view.findViewById(android.R.id.text1)
        val txtSub: TextView = view.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = android.view.LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.txtInfo.text = "Pasos: ${item.pasos} | Movimiento: ${item.movimiento}"
        holder.txtSub.text = "${item.timestamp} - Orientación: ${item.orientacion}"
    }

    override fun getItemCount(): Int = lista.size
}

class EjercicioRunner(
    private val activity: AppCompatActivity,
    private val container: ViewGroup
) : SensorEventListener {

    private val sensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var currentEjercicioIndex = -1

    // Sensores
    private var accelSensor: Sensor? = null
    private var magSensor: Sensor? = null
    private var proximitySensor: Sensor? = null
    private var lightSensor: Sensor? = null
    private var stepSensor: Sensor? = null

    // Buffers para cálculo de orientación
    private val gravityValues = FloatArray(3)
    private val geomagneticValues = FloatArray(3)
    private var hasGravity = false
    private var hasGeomagnetic = false

    // Ejercicio 6 job
    private var sensorJob: Job? = null

    // Ejercicio 23 Tilt Ball params
    private var ballView: View? = null
    private var ballX = 0f
    private var ballY = 0f

    // Ejercicio 24 & 28 Step Counters
    private var stepCount = 0
    private var totalMovementLevel = "Bajo"
    private var currentOrientationStr = "Desconocida"
    private val registrosDashboard = mutableListOf<RegistroActividad>()
    private var dashboardAdapter: ActividadAdapter? = null

    // Dynamic views updating callback
    private var dynamicViewUpdate: ((SensorEvent) -> Unit)? = null

    fun stopSensors() {
        sensorManager.unregisterListener(this)
        sensorJob?.cancel()
        dynamicViewUpdate = null
    }

    fun loadEjercicio(index: Int) {
        stopSensors()
        container.removeAllViews()
        currentEjercicioIndex = index

        when (index) {
            1 -> setupEjercicio1()
            2 -> setupEjercicio2()
            3 -> setupEjercicio3()
            4 -> setupEjercicio4()
            5 -> setupEjercicio5()
            6 -> setupEjercicio6()
            7 -> setupEjercicio7()
            8 -> setupEjercicio8()
            9 -> setupEjercicio9()
            10 -> setupEjercicio10()
            11 -> setupEjercicio11()
            12 -> setupEjercicio12()
            13 -> setupEjercicio13()
            14 -> setupEjercicio14()
            15 -> setupEjercicio15()
            16 -> setupEjercicio16()
            17 -> setupEjercicio17()
            18 -> setupEjercicio18()
            19 -> setupEjercicio19()
            20 -> setupEjercicio20()
            21 -> setupEjercicio21()
            22 -> setupEjercicio22()
            23 -> setupEjercicio23()
            24 -> setupEjercicio24()
            25 -> setupEjercicio25()
            26 -> setupEjercicio26()
            27 -> setupEjercicio27()
            28 -> setupEjercicio28()
        }
    }

    // --- EJERCICIOS 1 A 6: HILOS Y CORRUTINAS ---

    private fun setupEjercicio1() {
        val layout = LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }
        val btn = Button(activity).apply { text = "Iniciar Hilo (1 a 10)" }
        val tv = TextView(activity).apply {
            textSize = 20f
            text = "Presiona el botón para comenzar..."
            setPadding(0, 24, 0, 0)
        }
        btn.setOnClickListener {
            btn.isEnabled = false
            tv.text = "Iniciando hilo...\n"
            thread {
                for (i in 1..10) {
                    Thread.sleep(1000)
                    activity.runOnUiThread {
                        tv.append("Número: $i\n")
                    }
                }
                activity.runOnUiThread {
                    tv.append("¡Hilo finalizado!")
                    btn.isEnabled = true
                }
            }
        }
        layout.addView(btn)
        layout.addView(tv)
        container.addView(layout)
    }

    private fun setupEjercicio2() {
        val layout = LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }
        val pb = ProgressBar(activity, null, android.R.attr.progressBarStyleHorizontal).apply {
            max = 100
            progress = 0
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        val tv = TextView(activity).apply {
            textSize = 22f
            text = "Progreso: 0%"
            gravity = Gravity.CENTER
            setPadding(0, 24, 0, 0)
        }
        val btn = Button(activity).apply { text = "Iniciar Carga Progresiva" }
        btn.setOnClickListener {
            btn.isEnabled = false
            pb.progress = 0
            thread {
                for (i in 0..100) {
                    Thread.sleep(100)
                    activity.runOnUiThread {
                        pb.progress = i
                        tv.text = "Progreso: $i%"
                    }
                }
                activity.runOnUiThread {
                    btn.isEnabled = true
                }
            }
        }
        layout.addView(btn)
        layout.addView(pb)
        layout.addView(tv)
        container.addView(layout)
    }

    private fun setupEjercicio3() {
        val layout = LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }
        val btn = Button(activity).apply { text = "Ejecutar 3 Hilos Simultáneos" }
        val sv = ScrollView(activity)
        val tv = TextView(activity).apply {
            textSize = 16f
            text = "Resultados intercalados:\n"
        }
        sv.addView(tv)

        btn.setOnClickListener {
            tv.text = "Ejecutando hilos...\n"
            thread {
                for (i in 2..10 step 2) {
                    Thread.sleep(300)
                    activity.runOnUiThread { tv.append("[Hilo Par]: $i\n") }
                }
            }
            thread {
                for (i in 1..9 step 2) {
                    Thread.sleep(400)
                    activity.runOnUiThread { tv.append("[Hilo Impar]: $i\n") }
                }
            }
            thread {
                for (ch in 'A'..'E') {
                    Thread.sleep(350)
                    activity.runOnUiThread { tv.append("[Hilo Letras]: $ch\n") }
                }
            }
        }
        layout.addView(btn)
        layout.addView(sv)
        container.addView(layout)
    }

    private fun setupEjercicio4() {
        val layout = LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }
        val et = EditText(activity).apply {
            hint = "Ingrese N (ej: 100)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            setText("50")
        }
        val btn = Button(activity).apply { text = "Calcular Suma (MiTarea)" }
        val tv = TextView(activity).apply {
            textSize = 20f
            text = "Resultado..."
            setPadding(0, 24, 0, 0)
        }
        btn.setOnClickListener {
            val n = et.text.toString().toLongOrNull() ?: 10L
            tv.text = "Calculando en segundo plano..."
            MiTarea(n) { resultado ->
                activity.runOnUiThread {
                    tv.text = "Suma de 1 a $n = $resultado"
                }
            }.ejecutar()
        }
        layout.addView(et)
        layout.addView(btn)
        layout.addView(tv)
        container.addView(layout)
    }

    private fun setupEjercicio5() {
        val layout = LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }
        val btn = Button(activity).apply { text = "Lanzar Corrutina (5 Segundos)" }
        val tv = TextView(activity).apply {
            textSize = 18f
            text = "Presione el botón para iniciar corrutina."
            setPadding(0, 24, 0, 0)
        }
        btn.setOnClickListener {
            btn.isEnabled = false
            tv.text = "Corrutina iniciada:\n"
            activity.lifecycleScope.launch {
                for (i in 1..5) {
                    delay(1000)
                    tv.append("Mensaje $i desde Corrutina (Segundo $i)\n")
                }
                tv.append("¡Corrutina finalizada con éxito!")
                btn.isEnabled = true
            }
        }
        layout.addView(btn)
        layout.addView(tv)
        container.addView(layout)
    }

    private fun setupEjercicio6() {
        val layout = LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }
        val btnStart = Button(activity).apply { text = "Iniciar Simulación Sensor" }
        val btnStop = Button(activity).apply {
            text = "Detener Simulación"
            isEnabled = false
        }
        val tv = TextView(activity).apply {
            textSize = 22f
            text = "Valor Sensor: ---"
            gravity = Gravity.CENTER
            setPadding(0, 32, 0, 0)
        }
        btnStart.setOnClickListener {
            btnStart.isEnabled = false
            btnStop.isEnabled = true
            sensorJob = activity.lifecycleScope.launch {
                while (isActive) {
                    delay(500)
                    val valorSimulado = Random.nextDouble(10.0, 100.0)
                    tv.text = "Valor Sensor: ${"%.2f".format(valorSimulado)} units"
                }
            }
        }
        btnStop.setOnClickListener {
            sensorJob?.cancel()
            btnStart.isEnabled = true
            btnStop.isEnabled = false
            tv.text = "Simulación Detenida"
        }
        layout.addView(btnStart)
        layout.addView(btnStop)
        layout.addView(tv)
        container.addView(layout)
    }

    // --- EJERCICIOS 7 A 18: EVENTOS TÁCTILES Y GESTOS ---

    private fun setupEjercicio7() {
        val tv = TextView(activity).apply {
            textSize = 24f
            text = "Toca aquí para probar eventos táctiles"
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#E0F7FA"))
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        tv.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> tv.text = "Pulsación iniciada\n(ACTION_DOWN)"
                MotionEvent.ACTION_MOVE -> tv.text = "Desplazando el dedo\n(ACTION_MOVE)"
                MotionEvent.ACTION_UP -> tv.text = "Pulsación finalizada\n(ACTION_UP)"
            }
            true
        }
        container.addView(tv)
    }

    private fun setupEjercicio8() {
        val tv = TextView(activity).apply {
            textSize = 24f
            text = "Toca o arrastra para ver coordenadas X e Y"
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#FFF3E0"))
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        tv.setOnTouchListener { _, event ->
            tv.text = "Coordenadas Táctiles:\nX: ${event.x.roundToInt()} px\nY: ${event.y.roundToInt()} px"
            true
        }
        container.addView(tv)
    }

    private fun setupEjercicio9() {
        val view = ColorChangeView(activity).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        val tv = TextView(activity).apply {
            text = "Toca cualquier parte de la vista para cambiar de color"
            textSize = 18f
            setPadding(32, 32, 32, 32)
            setTextColor(Color.BLACK)
        }
        val frame = FrameLayout(activity)
        frame.addView(view)
        frame.addView(tv)
        container.addView(frame)
    }

    private fun setupEjercicio10() {
        val frame = FrameLayout(activity).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundColor(Color.WHITE)
        }
        val ball = ImageView(activity).apply {
            setImageResource(android.R.drawable.presence_online)
            layoutParams = FrameLayout.LayoutParams(160, 160).apply {
                leftMargin = 300
                topMargin = 500
            }
        }
        var dX = 0f
        var dY = 0f
        ball.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = v.x - event.rawX
                    dY = v.y - event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    v.animate()
                        .x(event.rawX + dX)
                        .y(event.rawY + dY)
                        .setDuration(0)
                        .start()
                }
            }
            true
        }
        val tv = TextView(activity).apply {
            text = "Arrastra el icono verde libremente por la pantalla"
            textSize = 18f
            setPadding(32, 32, 32, 32)
        }
        frame.addView(ball)
        frame.addView(tv)
        container.addView(frame)
    }

    private fun setupEjercicio11() {
        val layout = LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        val drawView = DrawingView(activity).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f)
        }
        val btnClear = Button(activity).apply { text = "Limpiar Lienzo" }
        btnClear.setOnClickListener { drawView.clearCanvas() }
        layout.addView(drawView)
        layout.addView(btnClear)
        container.addView(layout)
    }

    private fun setupEjercicio12() {
        val tv = TextView(activity).apply {
            textSize = 22f
            text = "Realiza los siguientes gestos aquí:\n\n• Toque simple (Tap)\n• Toque prolongado (Long Press)\n• Deslizamiento rápido (Fling)"
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#F3E5F5"))
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        val detector = GestureDetectorCompat(activity, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                tv.text = "Gesto Detectado:\n\n¡TOQUE SIMPLE (TAP)!"
                return true
            }
            override fun onLongPress(e: MotionEvent) {
                tv.text = "Gesto Detectado:\n\n¡TOQUE PROLONGADO (LONG PRESS)!"
            }
            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                tv.text = "Gesto Detectado:\n\n¡DESLIZAMIENTO RÁPIDO (FLING)!\nVelocidad X: ${velocityX.roundToInt()} px/s"
                return true
            }
        })
        tv.setOnTouchListener { _, event -> detector.onTouchEvent(event); true }
        container.addView(tv)
    }

    private fun setupEjercicio13() {
        val tv = TextView(activity).apply {
            textSize = 20f
            text = "Pon varios dedos sobre la pantalla..."
            setPadding(32, 32, 32, 32)
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#E8F5E9"))
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        tv.setOnTouchListener { _, event ->
            val count = event.pointerCount
            val sb = StringBuilder("Dedos detectados en pantalla: $count\n\n")
            for (i in 0 until count) {
                val id = event.getPointerId(i)
                sb.append("Dedo #$id -> X: ${event.getX(i).roundToInt()} px, Y: ${event.getY(i).roundToInt()} px\n")
            }
            tv.text = sb.toString()
            true
        }
        container.addView(tv)
    }

    private fun setupEjercicio14() {
        val layout = LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        val drawView = MultiTouchDrawingView(activity).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f)
        }
        val btnClear = Button(activity).apply { text = "Limpiar Dibujo Multitouch" }
        btnClear.setOnClickListener { drawView.clearCanvas() }
        layout.addView(drawView)
        layout.addView(btnClear)
        container.addView(layout)
    }

    private fun setupEjercicio15() {
        val frame = FrameLayout(activity).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        val img = ImageView(activity).apply {
            setImageResource(android.R.drawable.ic_menu_gallery)
            layoutParams = FrameLayout.LayoutParams(400, 400, Gravity.CENTER)
        }
        var scaleFactor = 1f
        val scaleDetector = ScaleGestureDetector(activity, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scaleFactor *= detector.scaleFactor
                scaleFactor = scaleFactor.coerceIn(0.5f, 4.0f)
                img.scaleX = scaleFactor
                img.scaleY = scaleFactor
                return true
            }
        })
        frame.setOnTouchListener { _, event -> scaleDetector.onTouchEvent(event); true }
        val tv = TextView(activity).apply {
            text = "Usa el gesto de pellizco (Pinch to Zoom) para escalar la imagen"
            textSize = 18f
            setPadding(32, 32, 32, 32)
        }
        frame.addView(img)
        frame.addView(tv)
        container.addView(frame)
    }

    private fun setupEjercicio16() {
        val rotateView = TwoFingerRotateView(activity).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        container.addView(rotateView)
    }

    private fun setupEjercicio17() {
        val pointsView = object : View(activity) {
            private val colors = intArrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.YELLOW)
            private val paint = Paint().apply { isAntiAlias = true }
            private var currentEvent: MotionEvent? = null

            override fun onDraw(canvas: Canvas) {
                super.onDraw(canvas)
                currentEvent?.let { e ->
                    if (e.actionMasked != MotionEvent.ACTION_UP && e.actionMasked != MotionEvent.ACTION_CANCEL) {
                        for (i in 0 until e.pointerCount) {
                            val id = e.getPointerId(i)
                            paint.color = colors[id % colors.size]
                            canvas.drawCircle(e.getX(i), e.getY(i), 80f, paint)
                        }
                    }
                }
            }

            override fun onTouchEvent(event: MotionEvent): Boolean {
                currentEvent = MotionEvent.obtain(event)
                invalidate()
                return true
            }
        }.apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        container.addView(pointsView)
    }

    private fun setupEjercicio18() {
        val transformView = TransformableImageView(activity).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        container.addView(transformView)
    }

    // --- EJERCICIOS 19 A 28: SENSORES ---

    private fun setupEjercicio19() {
        val layout = RelativeLayout(activity).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundColor(Color.GREEN)
        }
        val tv = TextView(activity).apply {
            textSize = 26f
            setTextColor(Color.WHITE)
            text = "Sensor de Proximidad:\nAcerca la mano a la pantalla..."
            gravity = Gravity.CENTER
            layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        layout.addView(tv)
        container.addView(layout)

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        if (proximitySensor == null) {
            tv.text = "Sensor de proximidad NO disponible en este dispositivo."
            return
        }

        dynamicViewUpdate = { event ->
            if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
                val distance = event.values[0]
                if (distance < (proximitySensor?.maximumRange ?: 5f)) {
                    layout.setBackgroundColor(Color.RED)
                    tv.text = "¡OBJETO CERCA!\nDistancia: $distance cm"
                } else {
                    layout.setBackgroundColor(Color.GREEN)
                    tv.text = "OBJETO LEJOS\nDistancia: $distance cm"
                }
            }
        }
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_UI)
    }

    private fun setupEjercicio20() {
        val layout = RelativeLayout(activity).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundColor(Color.BLACK)
        }
        val tv = TextView(activity).apply {
            textSize = 24f
            setTextColor(Color.WHITE)
            text = "Leyendo Sensor de Luz..."
            gravity = Gravity.CENTER
            layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        layout.addView(tv)
        container.addView(layout)

        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        if (lightSensor == null) {
            tv.text = "Sensor de luz NO disponible en este dispositivo."
            return
        }

        dynamicViewUpdate = { event ->
            if (event.sensor.type == Sensor.TYPE_LIGHT) {
                val lux = event.values[0]
                tv.text = "Intensidad de Luz:\n${"%.1f".format(lux)} LUX"
                val grayVal = (lux / 1000f * 255).coerceIn(0f, 255f).toInt()
                layout.setBackgroundColor(Color.rgb(grayVal, grayVal, grayVal))
                if (grayVal > 128) tv.setTextColor(Color.BLACK) else tv.setTextColor(Color.WHITE)
            }
        }
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_UI)
    }

    private fun setupEjercicio21() {
        val tv = TextView(activity).apply {
            textSize = 22f
            text = "Calculando orientación..."
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        container.addView(tv)

        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        dynamicViewUpdate = { event ->
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, gravityValues, 0, 3)
                hasGravity = true
            } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, geomagneticValues, 0, 3)
                hasGeomagnetic = true
            }
            if (hasGravity && hasGeomagnetic) {
                val R = FloatArray(9)
                val I = FloatArray(9)
                if (SensorManager.getRotationMatrix(R, I, gravityValues, geomagneticValues)) {
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(R, orientation)
                    val azimut = Math.toDegrees(orientation[0].toDouble()).roundToInt()
                    val pitch = Math.toDegrees(orientation[1].toDouble()).roundToInt()
                    val roll = Math.toDegrees(orientation[2].toDouble()).roundToInt()

                    tv.text = "Orientación del Dispositivo:\n\n" +
                            "Azimut (Z): $azimut°\n" +
                            "Pitch (X): $pitch°\n" +
                            "Roll (Y): $roll°"
                }
            }
        }
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, magSensor, SensorManager.SENSOR_DELAY_UI)
    }

    private fun setupEjercicio22() {
        val layout = LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        val compassNeedleView = object : View(activity) {
            private val paintNeedle = Paint().apply {
                color = Color.RED
                isAntiAlias = true
                strokeWidth = 14f
            }
            private val paintCircle = Paint().apply {
                color = Color.DKGRAY
                style = Paint.Style.STROKE
                strokeWidth = 8f
                isAntiAlias = true
            }
            private var angle = 0f

            fun setAngle(newAngle: Float) {
                angle = newAngle
                invalidate()
            }

            override fun onDraw(canvas: Canvas) {
                super.onDraw(canvas)
                val cx = width / 2f
                val cy = height / 2f
                canvas.drawCircle(cx, cy, 200f, paintCircle)
                canvas.save()
                canvas.rotate(-angle, cx, cy)
                canvas.drawLine(cx, cy, cx, cy - 180f, paintNeedle)
                canvas.restore()
            }
        }.apply {
            layoutParams = LinearLayout.LayoutParams(500, 500)
        }

        val tvHeading = TextView(activity).apply {
            textSize = 22f
            text = "Apuntando al Norte: --°"
            setPadding(0, 32, 0, 0)
        }
        layout.addView(compassNeedleView)
        layout.addView(tvHeading)
        container.addView(layout)

        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        dynamicViewUpdate = { event ->
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, gravityValues, 0, 3)
                hasGravity = true
            } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, geomagneticValues, 0, 3)
                hasGeomagnetic = true
            }
            if (hasGravity && hasGeomagnetic) {
                val R = FloatArray(9)
                val I = FloatArray(9)
                if (SensorManager.getRotationMatrix(R, I, gravityValues, geomagneticValues)) {
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(R, orientation)
                    val azimutDeg = Math.toDegrees(orientation[0].toDouble()).toFloat()
                    compassNeedleView.setAngle(azimutDeg)
                    tvHeading.text = "Azimut Norte Magnético:\n${azimutDeg.roundToInt()}°"
                }
            }
        }
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, magSensor, SensorManager.SENSOR_DELAY_UI)
    }

    private fun setupEjercicio23() {
        val frame = FrameLayout(activity).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundColor(Color.parseColor("#ECEFF1"))
        }
        val ball = View(activity).apply {
            setBackgroundColor(Color.RED)
            layoutParams = FrameLayout.LayoutParams(120, 120)
        }
        ballView = ball
        val tv = TextView(activity).apply {
            text = "Inclina el dispositivo para mover el cuadro rojo"
            textSize = 18f
            setPadding(32, 32, 32, 32)
        }
        frame.addView(ball)
        frame.addView(tv)
        container.addView(frame)

        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        dynamicViewUpdate = { event ->
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val ax = event.values[0]
                val ay = event.values[1]
                ballX -= ax * 3f
                ballY += ay * 3f
                ballView?.let { b ->
                    val maxX = (container.width - b.width).toFloat().coerceAtLeast(0f)
                    val maxY = (container.height - b.height).toFloat().coerceAtLeast(0f)
                    ballX = ballX.coerceIn(-200f, maxX)
                    ballY = ballY.coerceIn(-200f, maxY)
                    b.translationX = ballX
                    b.translationY = ballY
                }
            }
        }
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_GAME)
    }

    private fun setupEjercicio24() {
        val layout = LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        val tvSteps = TextView(activity).apply {
            textSize = 32f
            text = "Pasos Detectados:\n$stepCount"
            gravity = Gravity.CENTER
        }
        val btnReset = Button(activity).apply { text = "Reiniciar Contador" }
        btnReset.setOnClickListener {
            stepCount = 0
            tvSteps.text = "Pasos Detectados:\n0"
        }
        layout.addView(tvSteps)
        layout.addView(btnReset)
        container.addView(layout)

        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
            ?: sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            tvSteps.text = "Sensor de pasos no disponible física/emuladamente en este dispositivo."
            return
        }

        dynamicViewUpdate = { event ->
            stepCount++
            tvSteps.text = "Pasos Detectados:\n$stepCount"
        }
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
    }

    private fun setupEjercicio25() {
        val layout = RelativeLayout(activity).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundColor(Color.GRAY)
        }
        val tv = TextView(activity).apply {
            textSize = 22f
            setTextColor(Color.WHITE)
            text = "Combo Sensor Proximidad + Luz..."
            gravity = Gravity.CENTER
            layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        layout.addView(tv)
        container.addView(layout)

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        var lastProx = 10f
        var lastLux = 500f

        dynamicViewUpdate = { event ->
            if (event.sensor.type == Sensor.TYPE_PROXIMITY) lastProx = event.values[0]
            if (event.sensor.type == Sensor.TYPE_LIGHT) lastLux = event.values[0]

            val isDark = lastLux < 10f
            val isNear = lastProx < 5f

            if (isDark && isNear) {
                layout.setBackgroundColor(Color.BLACK)
                tv.setTextColor(Color.WHITE)
                tv.text = "Estado: Oscuro y Cerca\n(Fondo NEGRO)\nLux: $lastLux | Prox: $lastProx cm"
            } else if (!isDark && !isNear) {
                layout.setBackgroundColor(Color.WHITE)
                tv.setTextColor(Color.BLACK)
                tv.text = "Estado: Claro y Lejos\n(Fondo BLANCO)\nLux: $lastLux | Prox: $lastProx cm"
            } else {
                layout.setBackgroundColor(Color.BLUE)
                tv.setTextColor(Color.WHITE)
                tv.text = "Estado: Intermedio\n(Fondo AZUL)\nLux: $lastLux | Prox: $lastProx cm"
            }
        }
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_UI)
    }

    private fun setupEjercicio26() {
        val tv = TextView(activity).apply {
            textSize = 28f
            text = "Sacude el teléfono para detectar movimiento agudo"
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        container.addView(tv)

        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        var lastShakeTime = 0L

        dynamicViewUpdate = { event ->
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val gX = x / SensorManager.GRAVITY_EARTH
                val gY = y / SensorManager.GRAVITY_EARTH
                val gZ = z / SensorManager.GRAVITY_EARTH
                val gForce = sqrt(gX * gX + gY * gY + gZ * gZ)

                if (gForce > 2.5f) {
                    val now = System.currentTimeMillis()
                    if (now - lastShakeTime > 1000) {
                        lastShakeTime = now
                        tv.text = "¡¡¡¡¡¡¡¡¡¡ ¡AGITADO! !!!!!!!!!\n\nFuerza G: ${"%.2f".format(gForce)}"
                        tv.setBackgroundColor(Color.YELLOW)
                    }
                }
            }
        }
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_GAME)
    }

    private fun setupEjercicio27() {
        val tv = TextView(activity).apply {
            textSize = 24f
            text = "Calculando posición espacial del teléfono..."
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        container.addView(tv)

        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        dynamicViewUpdate = { event ->
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val posicion = when {
                    z > 8.0f -> "Boca Arriba"
                    z < -8.0f -> "Boca Abajo"
                    Math.abs(y) > 7.0f || Math.abs(x) > 7.0f -> "Posición Vertical"
                    else -> "Inclinación Diagonal"
                }

                tv.text = "Posición actual del dispositivo:\n\n¡$posicion!\n\n(X: ${"%.1f".format(x)}, Y: ${"%.1f".format(y)}, Z: ${"%.1f".format(z)})"
            }
        }
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_UI)
    }

    private fun setupEjercicio28() {
        val layout = LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        val tvHeader = TextView(activity).apply {
            textSize = 20f
            text = "Dashboard de Actividad (Pasos + Movimiento + Orientación)"
        }
        val rv = RecyclerView(activity).apply {
            layoutManager = LinearLayoutManager(activity)
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f)
        }
        dashboardAdapter = ActividadAdapter(registrosDashboard)
        rv.adapter = dashboardAdapter

        val btnAdd = Button(activity).apply { text = "Registrar Estado Actual" }
        btnAdd.setOnClickListener {
            val hora = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
            registrosDashboard.add(0, RegistroActividad(hora, stepCount, totalMovementLevel, currentOrientationStr))
            dashboardAdapter?.notifyItemInserted(0)
            rv.scrollToPosition(0)
        }

        layout.addView(tvHeader)
        layout.addView(btnAdd)
        layout.addView(rv)
        container.addView(layout)

        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        dynamicViewUpdate = { event ->
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val mag = sqrt(x * x + y * y + z * z)
                totalMovementLevel = if (mag > 13f) "Alto (Sacudiendo)" else if (mag > 10.5f) "Medio (Caminando)" else "Bajo (Reposo)"

                currentOrientationStr = when {
                    z > 8.0f -> "Boca Arriba"
                    z < -8.0f -> "Boca Abajo"
                    else -> "Vertical / Inclinado"
                }
            } else if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR || event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                stepCount++
            }
        }
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let { dynamicViewUpdate?.invoke(it) }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
