//package com.abplus.dxf.parse.section

//import com.abplus.dxf.parse.Entity
//import java.util.ArrayList
//import java.util.HashMap

/**
 * User: kazhida
 * Date: 2012/08/03
 * Time: 11:41
 */

open class Tables() {
    //    class object {
    //        public val SECTION_NAME: String = "TABLES".intern()
    //        public val APPID: String        = "APPID".intern()
    //        public val BLOCK_RECORD: String = "BLOCK_RECORD".intern()
    //        public val DIMSTYLE: String     = "DIMSTYLE".intern()
    //        public val LAYER: String        = "LAYER".intern()
    //        public val LTYPE: String        = "LTYPE".intern()
    //        public val STYLE: String        = "STYLE".intern()
    //        public val UCS: String          = "UCS".intern()
    //        public val VIEW: String         = "VIEW".intern()
    //        public val VPORT: String        = "VPORT".intern()
    //        public val ENDTAB: String       = "ENDTAB".intern()
    //    }

    //    class Table(): Entity() {
    //
    //    }

    //    private val tables = HashMap<String, Table>()
    //    private val names = ArrayList<String>()
    //    public fun table(name: String): Table? = tables.get(name)

    //    fun toString(): String? {
    //        val builder = StringBuilder();
    //        builder.append("(${SECTION_NAME} . (")
    //        for (name in names) builder.append("(\n${tables.get(name)})\n")
    //        builder.append("))")
    //        return builder.toString()
    //    }

    //    val appID: Table?       get() = table(APPID)
    //    val blockRecord: Table? get() = table(BLOCK_RECORD)
    //    val dimStyle: Table?    get() = table(DIMSTYLE)
    //    val layer: Table?       get() = table(LAYER)
    //    val lineType: Table?    get() = table(LTYPE)
    //    val style: Table?       get() = table(STYLE)
    //    val ucs: Table?         get() = table(UCS)
    //    val view: Table?        get() = table(VIEW)
    //    val viewPort: Table?    get() = table(VPORT)

    //    class Mutable(): Tables() {
    //        private fun add(table: Table): Table? {
    //            val name: String? = table.head.name
    //            if (name != null) {
    //                if (! tables.containsKey(name)) names.add(name)
    //                return tables.put(name, table)
    //            }else {
    //                return null;
    //            }
    //        }
    //        public fun add(head: Entity, records: List<Entity>): Table? = add(Table(head, records))
    //    }
}

//public class Table(val head: Entity, private val records: List<Entity>) {
//    fun get(idx: Int):Entity = records.get(idx)
//    fun size(): Int = records.size()
//    val length: Int get() = size()
//
//    fun toString(): String? {
//        val builder = StringBuilder()
//        builder.append("${head}\n")
//        for (record in records) builder.append("${record}\n")
//        return builder.toString()
//    }
//}

