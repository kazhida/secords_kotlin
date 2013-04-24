package com.abplus.dxf.parse.section

import com.abplus.dxf.parse.Cell
import java.util.HashMap

/**
 *
 *
 * User: kazhida
 * Date: 2012/08/03
 * Time: 11:18
 */
open class Header() {
    class object {
        public val SECTION_NAME: String = "HEADER".intern()
    }
    val params: Map<String, Cell> = HashMap<String, Cell>()

    /**
     *
     */
    fun toString(): String? {
        val builder = StringBuilder();
        builder.append("(${SECTION_NAME} . (")
        for (key in params.keySet()) {
            builder.append("(${key} . ${params.get(key)})\n")
        }
        builder.append("))")
        return builder.toString()
    }
}