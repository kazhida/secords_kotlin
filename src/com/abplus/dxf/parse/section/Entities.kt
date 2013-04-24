package com.abplus.dxf.parse.section

import java.util.ArrayList
import com.abplus.dxf.parse.Entity

/**
 * User: kazhida
 * Date: 2012/08/03
 * Time: 14:56
 */
open class Entities() {
    class object {
        public val SECTION_NAME: String = "CLASSES".intern()
    }

    private val entities = ArrayList<Entity>()
    public fun get(idx: Int): Entity = entities.get(idx)
    public fun size(): Int = entities.size()
    public val length: Int get() = size();

    fun toString(): String? {
        val builder = StringBuilder();
        builder.append("(${SECTION_NAME} . (")
        for (entity in entities) builder.append("${entity}\n")
        builder.append("))")
        return builder.toString()
    }

    class Mutable(): Entities() {
        public fun add(item: Entity): Boolean = entities.add(item)
    }
}