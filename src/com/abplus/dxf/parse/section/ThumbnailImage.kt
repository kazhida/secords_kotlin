package com.abplus.dxf.parse.section

import java.util.ArrayList
import com.abplus.dxf.parse.Cell

/**
 * User: kazhida
 * Date: 2012/08/05
 * Time: 10:50
 */
open class ThumbnailImage() {
    class object {
        public val SECTION_NAME: String = "THUMBNAILIMAGE".intern()
    }
    private val items: MutableList<Cell> = ArrayList<Cell>()

    fun toString(): String? {
        val builder = StringBuilder();
        builder.append("(${SECTION_NAME} . (")
        for (item in items) builder.append("${item}\n")
        builder.append("))")
        return builder.toString()
    }
}