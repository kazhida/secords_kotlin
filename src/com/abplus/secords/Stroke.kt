package com.abplus.secords

import jet.MutableList
import java.util.ArrayList
import android.graphics.PointF

/**
 * User: kazhida
 * Date: 2012/08/14
 * Time: 14:11
 */
class Stroke(val color: Int): Iterable<PointF> {
    private val points: MutableList<PointF> = ArrayList<PointF>()

    public fun add(x: Float?, y: Float?, transform: Transform): Boolean {
        if (x != null && y != null) {
            val p = PointF(x, y)
            transform.inverse(p)
            return add(p)
        } else {
            return false
        }
    }
    public fun add(p: PointF): Boolean = points.add(p)
    public fun get(idx: Int): PointF = points.get(idx)
    public fun size(): Int = points.size()
    public val length: Int get() = size()
    public override fun iterator(): Iterator<PointF> = points.iterator()
}
