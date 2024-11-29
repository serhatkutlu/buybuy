package com.example.buybuy.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.example.buybuy.R

class CouponView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val circlePath = Path()

    private val dashPath = Path()
    private val dashPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var middleCircleRadius = getPx(10f)
    private var outerCircleRadius = getPx(20f)


    init {
        setLayerType(View.LAYER_TYPE_HARDWARE, null)


        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CouponView, 0, 0)
            try {
                middleCircleRadius = typedArray.getDimension(
                    (R.styleable.CouponView_middleCircleRadius),
                    10f
                )
                middleCircleRadius=getPx(middleCircleRadius)

                outerCircleRadius = typedArray.getDimension(
                    (R.styleable.CouponView_outerCircleRadius),
                    20f
                )
                outerCircleRadius=getPx(outerCircleRadius)
            } finally {
                typedArray.recycle()
            }
        }
        circlePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        dashPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        dashPaint.pathEffect= DashPathEffect(floatArrayOf(10f, 5f),0f)
        dashPaint.style = Paint.Style.STROKE

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawCircle(canvas)
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.save()
        super.dispatchDraw(canvas)
        canvas.restore()
    }


    private fun drawCircle(canvas: Canvas) {

        circlePath.reset()


        circlePath.addCircle(
            0.toFloat(),
            height/2.toFloat(),
            middleCircleRadius,
            Path.Direction.CW
        )
        circlePath.addCircle(
            width.toFloat(),
            height/2.toFloat(),
            middleCircleRadius,
            Path.Direction.CW)
        circlePath.addCircle(
            0.toFloat(),
            0.toFloat(),
            outerCircleRadius,
            Path.Direction.CW)
        circlePath.addCircle(
            width.toFloat(),
            0.toFloat(),
            outerCircleRadius,
            Path.Direction.CW)
        circlePath.addCircle(
            0f,
            height.toFloat(),
            outerCircleRadius,
            Path.Direction.CW)
        circlePath.addCircle(
            width.toFloat(),
            height.toFloat(),
            outerCircleRadius,
            Path.Direction.CW)

        dashPath.moveTo(middleCircleRadius,height/2.toFloat())
        dashPath.lineTo(width.toFloat()-middleCircleRadius,height/2.toFloat())
        canvas.drawPath(circlePath,circlePaint)
        canvas.drawPath(dashPath,dashPaint)
    }

    private fun getPx(value: Float): Float {
        return when (value) {
            0f -> 0f
            else -> {
                val density = resources.displayMetrics.density
                value * density
            }
        }
    }

}
