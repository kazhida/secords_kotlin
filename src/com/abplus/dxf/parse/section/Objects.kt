package com.abplus.dxf.parse.section

import java.util.ArrayList
import com.abplus.dxf.parse.Entity

/**
 * Created with IntelliJ IDEA.
 * User: kazhida
 * Date: 2012/08/05
 * Time: 10:46
 * To change this template use File | Settings | File Templates.
 */
open class Objects {
    class object {
        public val SECTION_NAME: String = "OBJECTS".intern()
    }

    private val items = ArrayList<Entity>()
    public fun get(idx: Int): Entity = items.get(idx)
    public fun size(): Int = items.size()
    public val length: Int get() = size();

    fun toString(): String? {
        val builder = StringBuilder();
        builder.append("(${SECTION_NAME} . (")
        for (item in items) builder.append("${item}\n")
        builder.append("))")
        return builder.toString()
    }

    class Mutable(): Objects() {
        public fun add(obj: Entity): Boolean = items.add(obj)
    }
}