package com.abplus.dxf.parse.section

import com.abplus.dxf.parse.Entity
import java.util.ArrayList

/**
    クラスをEntityで代用

 * User: kazhida
 * Date: 2012/08/03
 * Time: 14:49
 */
open class Classes() {
    class object {
        public val SECTION_NAME: String = "CLASSES".intern()
    }

    val items: MutableList<Entity> = ArrayList<Entity>()

    fun toString(): String? {
        val builder = StringBuilder();
        builder.append("(${SECTION_NAME} . (")
        for (item in items) builder.append("${item}\n")
        builder.append("))")
        return builder.toString()
    }
}