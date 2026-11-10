package com.example.hilosdeejecucinysensores

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.view.GestureDetectorCompat
import kotlin.math.atan2
import kotlin.math.roundToInt
import kotlin.random.Random

// Ejercicio 9: Vista que cambia de color al tocar
class ColorChangeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {
    private var currentColor = Color.LTGRAY

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(currentColor)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            currentColor = Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
            invalidate()
            return true
        }
        return super.onTouchEvent(event)
    }
}

// Ejercicio 11: Vista para dibujar trazos
class DrawingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {
    private val path = Path()
    private val paths = mutableListOf<Path>()
    private val paint = Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
        strokeWidth = 10f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (p in paths) {
            canvas.drawPath(p, paint)
        }
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
            }
            MotionEvent.ACTION_UP -> {
                paths.add(Path(path))
                path.reset()
            }
            else -> return false
        }
        invalidate()
        return true
    }

    fun clearCanvas() {
        paths.clear()
        path.reset()
        invalidate()
    }
}

// Ejercicio 14: Dibujo multitouch con colores distintos por dedo
class MultiTouchDrawingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {
    private class PointerPath(val path: Path, val paint: Paint)

    private val activePaths = HashMap<Int, PointerPath>()
    private val completedPaths = mutableListOf<PointerPath>()
    private val colors = intArrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.YELLOW)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (pp in completedPaths) {
            canvas.drawPath(pp.path, pp.paint)
        }
        for (pp in activePaths.values) {
            canvas.drawPath(pp.path, pp.paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val actionIndex = event.actionIndex
        val pointerId = event.getPointerId(actionIndex)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val p = Path()
                p.moveTo(event.getX(actionIndex), event.getY(actionIndex))
                val paint = Paint().apply {
                    color = colors[pointerId % colors.size]
                    isAntiAlias = true
                    strokeWidth = 12f
                    style = Paint.Style.STROKE
                    strokeJoin = Paint.Join.ROUND
                }
                activePaths[pointerId] = PointerPath(p, paint)
            }
            MotionEvent.ACTION_MOVE -> {
                for (i in 0 until event.pointerCount) {
                    val id = event.getPointerId(i)
                    activePaths[id]?.path?.lineTo(event.getX(i), event.getY(i))
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL -> {
                activePaths[pointerId]?.let {
                    completedPaths.add(it)
                    activePaths.remove(pointerId)
                }
            }
        }
        invalidate()
        return true
    }

    fun clearCanvas() {
        completedPaths.clear()
        activePaths.clear()
        invalidate()
    }
}

// Ejercicio 16: Vista giratoria con dos dedos
class TwoFingerRotateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {
    private var rotationAngle = 0f
    private var previousAngle = 0f
    private val paint = Paint().apply {
        color = Color.DKGRAY
        isAntiAlias = true
    }
    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 50f
        textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f
        canvas.save()
        canvas.rotate(rotationAngle, cx, cy)
        canvas.drawRect(cx - 200, cy - 100, cx + 200, cy + 100, paint)
        canvas.drawText("Rotación: ${rotationAngle.roundToInt()}°", cx, cy + 15, textPaint)
        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.pointerCount == 2) {
            val x1 = event.getX(0)
            val y1 = event.getY(0)
            val x2 = event.getX(1)
            val y2 = event.getY(1)
            val currentAngle = Math.toDegrees(atan2((y2 - y1).toDouble(), (x2 - x1).toDouble())).toFloat()

            when (event.actionMasked) {
                MotionEvent.ACTION_POINTER_DOWN -> {
                    previousAngle = currentAngle
                }
                MotionEvent.ACTION_MOVE -> {
                    val delta = currentAngle - previousAngle
                    rotationAngle += delta
                    previousAngle = currentAngle
                    invalidate()
                }
            }
            return true
        }
        return super.onTouchEvent(event)
    }
}

// Ejercicio 17: Puntos temporales multitouch
class MultiTouchPointsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {
    private val colors = intArrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.YELLOW, Color.DKGRAY)
    private val paint = Paint().apply { isAntiAlias = true }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        invalidate()
        return true
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        invalidate()
        return super.dispatchTouchEvent(event)
    }

    fun drawActivePoints(canvas: Canvas, event: MotionEvent?) {
        if (event == null) return
        if (event.actionMasked == MotionEvent.ACTION_UP || event.actionMasked == MotionEvent.ACTION_CANCEL) return
        for (i in 0 until event.pointerCount) {
            val id = event.getPointerId(i)
            paint.color = colors[id % colors.size]
            canvas.drawCircle(event.getX(i), event.getY(i), 60f, paint)
        }
    }
}

// Ejercicio 18: Control total de imagen (Drag + Pinch Zoom + Rotate)
class TransformableImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {
    private var posX = 0f
    private var posY = 0f
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var scaleFactor = 1f
    private var rotationAngle = 0f
    private var previousAngle = 0f

    private val paint = Paint().apply { color = Color.parseColor("#3F51B5") }
    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }

    private val scaleDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = scaleFactor.coerceIn(0.5f, 5.0f)
            invalidate()
            return true
        }
    })

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f + posX
        val cy = height / 2f + posY

        canvas.save()
        canvas.translate(cx, cy)
        canvas.scale(scaleFactor, scaleFactor)
        canvas.rotate(rotationAngle)
        canvas.drawRect(-150f, -150f, 150f, 150f, paint)
        canvas.drawText("Arrastra/Zoom/Gira", 0f, 15f, textPaint)
        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleDetector.onTouchEvent(event)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchX = event.x
                lastTouchY = event.y
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount == 2) {
                    val x1 = event.getX(0)
                    val y1 = event.getY(0)
                    val x2 = event.getX(1)
                    val y2 = event.getY(1)
                    previousAngle = Math.toDegrees(atan2((y2 - y1).toDouble(), (x2 - x1).toDouble())).toFloat()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (event.pointerCount == 1 && !scaleDetector.isInProgress) {
                    val dx = event.x - lastTouchX
                    val dy = event.y - lastTouchY
                    posX += dx
                    posY += dy
                    lastTouchX = event.x
                    lastTouchY = event.y
                    invalidate()
                } else if (event.pointerCount == 2) {
                    val x1 = event.getX(0)
                    val y1 = event.getY(0)
                    val x2 = event.getX(1)
                    val y2 = event.getY(1)
                    val currentAngle = Math.toDegrees(atan2((y2 - y1).toDouble(), (x2 - x1).toDouble())).toFloat()
                    val delta = currentAngle - previousAngle
                    rotationAngle += delta
                    previousAngle = currentAngle
                    invalidate()
                }
            }
        }
        return true
    }
}
