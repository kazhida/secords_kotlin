package com.abplus.secords

import android.content.Context
import android.view.SurfaceView
import android.view.ViewGroup
import android.view.MotionEvent
import android.view.GestureDetector
import android.view.ScaleGestureDetector
import android.view.SurfaceHolder

/**
 * User: kazhida
 * Date: 2012/08/14
 * Time: 14:04
 */
class CustomSurfaceView(context: Context): SurfaceView(context) {

    val initialized = {
        getHolder()?.addCallback(HolderCallback())
        setLayoutParams(ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT
        ))
        setFocusable(true)
        true
    }
    val detector1 = GestureDetector(context, GestureListener())
    val detector2 = ScaleGestureDetector(context, ScaleListener())

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            detector1.onTouchEvent(event)
            detector2.onTouchEvent(event)
            return true
        } else {
            return false
        }
    }

    private class HolderCallback(): SurfaceHolder.Callback {

        public override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
            throw UnsupportedOperationException()
        }
        public override fun surfaceDestroyed(p0: SurfaceHolder?) {
            throw UnsupportedOperationException()
        }
        public override fun surfaceCreated(p0: SurfaceHolder?) {
            throw UnsupportedOperationException()
        }
    }

    private class GestureListener(): GestureDetector.OnGestureListener {
        public override fun onShowPress(p0: MotionEvent?) {
            throw UnsupportedOperationException()
        }
        public override fun onSingleTapUp(p0: MotionEvent?): Boolean {
            throw UnsupportedOperationException()
        }
        public override fun onLongPress(p0: MotionEvent?) {
            throw UnsupportedOperationException()
        }
        public override fun onDown(p0: MotionEvent?): Boolean {
            throw UnsupportedOperationException()
        }
        public override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            throw UnsupportedOperationException()
        }
        public override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            throw UnsupportedOperationException()
        }
    }

    private class ScaleListener(): ScaleGestureDetector.OnScaleGestureListener {
        public override fun onScaleEnd(p0: ScaleGestureDetector?) {
            throw UnsupportedOperationException()
        }
        public override fun onScaleBegin(p0: ScaleGestureDetector?): Boolean {
            throw UnsupportedOperationException()
        }
        public override fun onScale(p0: ScaleGestureDetector?): Boolean {
            throw UnsupportedOperationException()
        }
    }
}
