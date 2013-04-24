package com.abplus.secords

import android.view.SurfaceView
import android.content.Context
import android.view.SurfaceHolder
import android.view.ViewGroup
import android.gesture.GestureUtils
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import java.util.ArrayList
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.PointF
import android.graphics.Paint
import android.graphics.Color
import android.util.Log

/**
 * User: kazhida
 * Date: 2012/08/14
 * Time: 13:26
 */
class DrawingView(context: Context): SurfaceView(context) {

    trait Callback {
        fun onDown(position: PointF)
        fun onUp(position: PointF)
        fun onScroll(position: PointF, distance: PointF)
        fun onScale(focus: PointF, scaleFactor: Float)
    }

    trait Drawer {
        fun draw(canvas: Canvas);
    }

    var my_context: Context = context
    var callback: Callback? = null
    var drawer: Drawer? = null
    var detector1: GestureDetector? = null
    var detector2: ScaleGestureDetector? = null

    //    val initialized = {
    //        getHolder()?.addCallback(HolderCallback())
    //        setLayoutParams(ViewGroup.LayoutParams(
    //                ViewGroup.LayoutParams.FILL_PARENT,
    //                ViewGroup.LayoutParams.FILL_PARENT
    //        ))
    //        setFocusable(true)
    ////        detector1 = GestureDetector(my_context, GestureListener())
    ////        detector2 = ScaleGestureDetector(my_context, ScaleListener())
    //        true
    //    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            detector1?.onTouchEvent(event)
            detector2?.onTouchEvent(event)

            if (event.getAction() == MotionEvent.ACTION_UP) {
                callback?.onUp(PointF(event.getX(), event.getY()));
            }

            when (event.getAction()) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("TouchEvent", "Down")
                }
                MotionEvent.ACTION_UP -> {
                    Log.d("TouchEvent", "Up")
                }
                MotionEvent.ACTION_MOVE -> {
                    Log.d("TouchEvent", "Move")
                }
                else -> null
            }

            return true
        } else {
            return false
        }
    }

    public fun redraw() {
        val canvas = getHolder()?.lockCanvas()
        if (canvas != null) {
            try {
                canvas.save()
                drawer?.draw(canvas)
            } finally {
                canvas.restore()
                getHolder()?.unlockCanvasAndPost(canvas)
            }
        }
    }

    private class HolderCallback(): SurfaceHolder.Callback {

        public override fun surfaceCreated(p0: SurfaceHolder?) {
            //nop
        }

        public override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
            redraw()
        }

        public override fun surfaceDestroyed(p0: SurfaceHolder?) {
            //nop
        }
    }

    private class GestureListener(): GestureDetector.OnGestureListener {

        public override fun onDown(p0: MotionEvent?): Boolean {
            if (p0 != null && callback != null) {
                callback?.onDown(PointF(p0.getX(), p0.getY()))
            }
            return true
        }

        public override fun onShowPress(p0: MotionEvent?) {
            //nop
        }

        public override fun onSingleTapUp(p0: MotionEvent?): Boolean {
            return true
        }

        public override fun onLongPress(p0: MotionEvent?) {
            //nop
        }

        public override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            return true
        }

        public override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            if (p1 != null && callback != null) {
                val p = PointF(p1.getX(), p1.getY())
                val d = PointF(p2, p3)
                callback?.onScroll(p, d)
            }
            return true
        }
    }

    private class ScaleListener(): ScaleGestureDetector.OnScaleGestureListener {
        public override fun onScaleBegin(p0: ScaleGestureDetector?): Boolean {
            return true
        }
        public override fun onScale(p0: ScaleGestureDetector?): Boolean {
            return true
        }
        public override fun onScaleEnd(p0: ScaleGestureDetector?) {
            if (p0 != null && callback != null) {
                callback?.onScale(PointF(p0.getFocusX(), p0.getFocusY()), p0.getScaleFactor())
            }
        }
    }

    class object {
        fun create(context: Context): DrawingView {
            val result = DrawingView(context)

            result.getHolder()?.addCallback(result.HolderCallback())
            result.setLayoutParams(ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.FILL_PARENT
            ))
            result.setFocusable(true)

            result.detector1 = GestureDetector(context, result.GestureListener())
            result.detector2 = ScaleGestureDetector(context, result.ScaleListener())

            return result
        }
    }
}