package com.example.chitwing.anycure_kotlin_master.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.chitwing.anycure_kotlin_master.unit.Unit


/**
 * 自定义进度条
 */
class CWProgressView : View {

    /**
     * 画笔 - 阴影部分
     * */
    private val mShadowPaint by lazy {
        return@lazy createPaint()
    }
    /**
     * 阴影
     * */
    private val mMask = BlurMaskFilter(Unit.dip2px(10f).toFloat(),BlurMaskFilter.Blur.OUTER)

    /**
     * 画笔 -虚 轨道部分
     * */
    private val mStrokePaint by lazy {
        return@lazy createPaint()
    }
    /**
     * 画笔 -实 轨道部分
     * */
    private val mPaint by lazy {
        return@lazy createPaint()
    }

    private val mDotPaint by lazy {
        val m = createPaint()
        m.style = Paint.Style.FILL
        return@lazy m
    }

    private var mMax:Int = 50

    /**
     * 设置最大值
     * */
    fun setMax(arg:Int){
        mMax = arg
        invalidate()
    }

    /**
     * 设置当前值
     * */
    private var mCurrent:Int = 0
    fun setCurrent(arg:Int){
        mCurrent = arg
        invalidate()
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

    /**
     * 创建画笔
     * */
    private fun createPaint() :Paint{
        val m = Paint()
        m.isAntiAlias = true
        m.strokeWidth = Unit.dip2px(4f).toFloat()
        m.style = Paint.Style.STROKE
        m.color = Color.WHITE
        return m
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //计算宽高
        val cx:Float = width.toFloat() / 2
        val cy:Float = height.toFloat() / 2

        drawShadowLayer(canvas,cx,cy)

        drawStroke(canvas,cx,cy)

        drawCircle(canvas,cx,cy)

        drawDotCircle(canvas,cx,cy)

        drawIndicateCircle(canvas,cx,cy)

    }

    private val distance:Float = Unit.dip2px(30f).toFloat()
    /**
     * 画阴影
     * */
    private fun drawShadowLayer(arg: Canvas,x:Float,y: Float){
        //画圈
        arg.save()
        val radius = x - distance
        mShadowPaint.alpha = 100
        mShadowPaint.maskFilter = mMask
        arg.drawCircle(x,y,radius,mShadowPaint)
        arg.restore()
    }

    /**
     * 画轨道-实
     * */
    private val mCircle by lazy {
        val m = createPaint()
        m.alpha = 240
        m.style = Paint.Style.FILL_AND_STROKE
        val mask = BlurMaskFilter(Unit.dip2px(5f).toFloat(),BlurMaskFilter.Blur.SOLID)
        m.maskFilter = mask
        return@lazy m
    }
    private fun drawStroke(arg: Canvas,x:Float,y: Float){
        arg.save()
        mStrokePaint.alpha = 200
        val avg = 360.0 / mMax
        val end = avg * mCurrent
        arg.drawArc(distance,distance,width.toFloat() - distance,height.toFloat() - distance,-90f,-end.toFloat(),false,mStrokePaint)
        arg.restore()
    }

    /**
     * 指示
     * */
    private fun drawIndicateCircle(arg: Canvas,x:Float,y: Float){
        arg.save()
        val avg = Math.PI * 2.0 / mMax
        val end = avg * mCurrent
        val radius = x - distance
        val diffX = Math.sin(end) * radius
        val diffY = Math.cos(end) * radius

        val zx = x - diffX
        val zy = y - diffY

        arg.drawCircle(zx.toFloat(),zy.toFloat(),5f,mCircle)


        arg.restore()
    }

    /**
     * 画轨道-虚
     * */
    private fun drawCircle(arg: Canvas,x:Float,y: Float){
        arg.save()
        val radius = x - distance
        mPaint.alpha = 100
        arg.drawCircle(x,y,radius,mPaint)
        arg.restore()
    }

    /**
     * 画小点点
     * */
    private val dotRadius = Unit.dip2px(2f).toFloat()
    private val dotY = Unit.dip2px(20f).toFloat()
    private fun drawDotCircle(arg:Canvas,x:Float,y: Float){
        val avg = 360.0 / mMax
        arg.save()
        for (i in 0 .. mMax){
            if (i <= mCurrent) {
                mDotPaint.alpha = 255
            }else{
                mDotPaint.alpha = 50
            }
            arg.drawCircle(x,dotY,dotRadius,mDotPaint)
            arg.rotate( -avg.toFloat(),x,y)
        }
        arg.restore()
    }

}
