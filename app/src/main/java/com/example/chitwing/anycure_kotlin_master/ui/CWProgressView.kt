package com.example.chitwing.anycure_kotlin_master.ui

import android.content.Context
import android.content.res.TypedArray
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * TODO: document your custom view class.
 */
class CWProgressView : View {

    /**
     * 画笔
     * */
    private val mPaint by lazy {
        val m = Paint()
        m.isAntiAlias = true
        m.strokeWidth = 4f
        m.style = Paint.Style.STROKE
        m.color = Color.WHITE
        return@lazy m
    }



    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        defaultConfigure()
    }

    private fun defaultConfigure(){
        setLayerType(View.LAYER_TYPE_SOFTWARE,null)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //计算宽高
        Log.e("测试","宽->$width 高->$height")
        val cx:Float = width.toFloat() / 2
        val cy:Float = height.toFloat() / 2
        //画圈
        mPaint.alpha = 150
        mPaint.maskFilter = BlurMaskFilter(10f,BlurMaskFilter.Blur.OUTER)
        canvas.drawCircle(cx,cy,cx - 10,mPaint)

        val p = Paint()
        p.color = Color.WHITE
        p.style = Paint.Style.STROKE
        p.strokeWidth = 4f
        p.isAntiAlias = true
        p.alpha = 200
        canvas.drawCircle(cx,cy,cx - 10,p)

    }
}
