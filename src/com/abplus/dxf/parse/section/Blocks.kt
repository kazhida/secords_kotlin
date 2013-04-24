package com.abplus.dxf.parse.section

import jet.Map
import jet.List
import java.util.HashMap
import com.abplus.dxf.parse.Entity
import java.util.ArrayList

/**
 * User: kazhida
 * Date: 2012/08/03
 * Time: 14:31
 */

open class Blocks() {
    class object {
        public val SECTION_NAME: String = "BLOCKS".intern()
        public val ENDBLK: String = "ENDBLK".intern()
    }

    private val blocks = HashMap<String, Block>()
    private val names = ArrayList<String>()

    fun block(name: String): Block? = blocks.get(name)

    fun toString(): String? {
        val builder = StringBuilder();
        builder.append("(${SECTION_NAME} . (")
        for (name in names) builder.append("(\n${blocks.get(name)})\n")
        builder.append("))")
        return builder.toString()
    }

    class Mutable(): Blocks() {
        private fun add(block: Block): Block? {
            val name = block.head.name
            if (name != null) {
                if (!blocks.containsKey(name)) names.add(name)
                return blocks.put(name, block)
            } else {
                return null
            }
        }
        public fun add(head: Entity, entities: List<Entity>, tail: Entity): Block? = add(Block(head, entities, tail))
    }
}

class Block(val head: Entity, private val entities: List<Entity>, val tail: Entity) {
    fun get(idx: Int): Entity = entities.get(idx)
    fun size(): Int = entities.size()
    val length: Int get() = size()
    fun toString(): String? {
        val builder = StringBuilder()
        builder.append("${head}\n")
        for (entity in entities) builder.append("${entity}\n")
        builder.append("${tail}\n")
        return builder.toString()
    }
}

