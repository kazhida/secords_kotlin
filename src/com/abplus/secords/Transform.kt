package com.abplus.secords

import android.graphics.PointF
import android.graphics.RectF
import android.graphics.Canvas

/**
 * User: kazhida
 * Date: 2012/08/14
 * Time: 14:30
 */
class Transform() {
    class object {
        val MIN_ZOOM = 1.toFloat()
        val MAX_ZOOM = 100.toFloat()
    }

    var scale: Float = 1.0.toFloat()
        private set

    var offset: PointF = PointF(0.toFloat(), 0.toFloat())
        private set

    var bounds: RectF = RectF(0.toFloat(), 0.toFloat(), 1.toFloat(), 1.toFloat())
        set(value) {
            $bounds = value
            setOffset(offset.x, offset.y)
        }

    private fun clip(v: Float, min: Float, max: Float): Float {
        if (v < min) return min
        if (v > max) return max
        return v
    }

    public fun zoom(z: Float): Boolean {
        val ns = clip(scale * z, MIN_ZOOM, MAX_ZOOM)

        if (ns != scale) {
            scale = ns
            return true
        } else {
            return false
        }
    }

    public fun zoom(z: Float, x: Float, y: Float): Boolean {
        val z0 = scale
        val x2 = x + offset.x
        val y2 = y + offset.y

        if (zoom(z)) {
            val z3 = scale / z0
            val x3 = x2 * z3
            val y3 = y2 * z3
            setOffset(x3 - x, y3 - y);
            return true;
        } else {
            return false;
        }
    }

    public fun pan(x: Float, y: Float): Boolean = setOffset(offset.x - x, offset.y - y)

    private fun setOffset(x: Float, y: Float): Boolean {
        var result = false

        val nx = clip(x, bounds.left, bounds.right)
        val ny = clip(y, bounds.bottom, bounds.top)

        if (nx != offset.x) {
            offset.x = nx
            result = true
        }
        if (ny != offset.y) {
            offset.y = ny
            result = true
        }

        return result
    }

    public fun inverse(p: PointF) {
        val x = (p.x + offset.x) / scale;
        val y = (p.y + offset.y) / scale;
        p.x = x;
        p.y = y;
    }

    public fun apply(p: PointF) {
        val x = p.x * scale - offset.x
        val y = p.y * scale - offset.y
        p.x = x
        p.y = y
    }

    public fun apply(canvas: Canvas) {
        canvas.translate(-offset.x, -offset.y)
        canvas.scale(scale, scale)
    }
}
