package com.abplus.dxf.parse

import com.abplus.dxf.parse.section.Header
import com.abplus.dxf.parse.section.Classes
import com.abplus.dxf.parse.section.Tables
import com.abplus.dxf.parse.section.Blocks
import com.abplus.dxf.parse.section.Entities
import com.abplus.dxf.parse.section.Objects
import com.abplus.dxf.parse.section.ThumbnailImage
import java.util.ArrayList
import java.io.InputStream
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * User: kazhida
 * Date: 2012/08/03
 * Time: 11:17
 */
class Parser() {
    class object {
        public val SECTION: String = "SECTION".intern()
        public val ENDSEC: String = "ENDSEC".intern()
        public val EOF: Cell = Cell(0, "EOF".intern())
        public val FETCH_FAILED: Int = -14; //通常のDXFでは使用していない値を使う
    }

    trait Callback {
        //  HEADERセクション
        fun onBeginHeader(parser: Parser): Boolean
        fun onLoadParameter(parser: Parser, key: String, value: Cell): Boolean
        fun onEndHeader(parser: Parser)

        //  CLASSESセクション
        fun onBeginClasses(parser: Parser): Boolean
        fun onLoadClass(parser: Parser, cls: Entity): Boolean
        fun onEndClasses(parser: Parser)

        //  TABLESセクション
        fun onBeginTables(parser: Parser): Boolean
        fun onBeginTable(parser: Parser, name: String): Boolean
        fun onLoadTableRecord(parser: Parser, name: String, record: Entity): Boolean
        fun onEndTable(parser: Parser, name: String)
        fun onEndTables(parser: Parser)

        //  BLOCKSセクション
        fun onBeginBlocks(parser: Parser): Boolean
        fun onBeginBlock(parser: Parser, name: String): Boolean
        fun onLoadBlockEntity(parser: Parser, name: String, entity: Entity): Boolean
        fun onEndBlock(parser: Parser, name: String)
        fun onEndBlocks(parser: Parser)

        //  ENTITIESセクション
        fun onBeginEntities(parser: Parser): Boolean
        fun onLoadEntity(parser: Parser, entity: Entity): Boolean
        fun onEndEntities(parser: Parser)

        //  OBJECTSセクション
        fun onBeginObjects(parser: Parser): Boolean
        fun onLoadObject(parser: Parser, obj: Entity): Boolean
        fun onEndObjects(parser: Parser)

        //  THUMBNAILIMAGEセクション
        fun onBeginThumbnailImage(parser: Parser): Boolean
        fun onLoadThumbnailImage(parser: Parser, obj: Cell): Boolean
        fun onEndThumbnailImage(parser: Parser)
    }

    trait Lexer {
        fun fetch(): Int
        fun unfetch()
        val groupCode: Int
        val dottedPair: Cell
        val lineNo: Int

        fun fetch(groupCode: Int, value: String): Boolean = (fetch() == groupCode && dottedPair.asString == value)
        fun skipTo(groupCode: Int) {
            while (fetch() != groupCode) {
            }
        }
        fun skipTo(groupCode: Int, value: String) {
            while (fetch() != groupCode || dottedPair.asString != value) {
            }
        }
        fun loadEntity(): Entity {
            val buf = ArrayList<Cell>();

            do {
                buf.add(dottedPair)
            } while (fetch() != 0)
            unfetch()

            return Entity(buf)
        }
    }

    class DXF() {
        val header = Header().Mutable()
        val classes = Classes().Mutable();
        val tables = Tables().Mutable();
        val blocks = Blocks().Mutable();
        val entities = Entities().Mutable();
        val objects = Objects().Mutable();
        val thumbnailimage = ThumbnailImage().Mutable();
    }
    private var dxf: DXF? = null

    val header: Header?                 get() = dxf?.header
    val classes: Classes?               get() = dxf?.classes
    val tables: Tables?                 get() = dxf?.tables
    val blocks: Blocks?                 get() = dxf?.blocks
    val entities: Entities?             get() = dxf?.entities
    val objects: Objects?               get() = dxf?.objects
    val thumbnailimage: ThumbnailImage? get() = dxf?.thumbnailimage

    fun parse(lex: Lexer, callback: Callback? = null) {
        dxf = DXF()
        while (lex.fetch(0, SECTION)) {
            if (lex.fetch() == 2) {
                val name = lex.dottedPair.asString.intern()
                if (name.equals(Header.SECTION_NAME))   parseHeader(lex, callback);
                if (name.equals(Classes.SECTION_NAME))  parseClasses(lex, callback);
                if (name.equals(Tables.SECTION_NAME))   parseTables(lex, callback);
                if (name.equals(Blocks.SECTION_NAME))   parseBlocks(lex, callback);
                if (name.equals(Entities.SECTION_NAME)) parseEntities(lex, callback);
                if (name.equals(Objects.SECTION_NAME))  parseObjects(lex, callback);
                if (name.equals(ThumbnailImage.SECTION_NAME)) parseThumbnailImage(lex, callback);
            }
        }
    }

    private fun parseHeader(lex: Lexer, callback: Callback?) {
        if (callback != null && callback.onBeginHeader(this)) {
            lex.skipTo(0, ENDSEC)
        } else {
            while (!lex.fetch(0, ENDSEC)) {
                val key = lex.dottedPair.asString
                lex.fetch();
                if (callback != null && callback.onLoadParameter(this, key, lex.dottedPair)) {
                    //skip
                } else {
                    dxf?.header?.put(key, lex.dottedPair)
                }
            }
        }
        callback?.onEndHeader(this)
    }

    private fun parseClasses(lex: Lexer, callback: Callback?) {
        if (callback != null && callback.onBeginClasses(this)) {
            lex.skipTo(0, ENDSEC)
        } else {
            while (!lex.fetch(0, ENDSEC)) {
                val entity = lex.loadEntity()
                if (callback != null && callback.onLoadClass(this, entity)) {
                    //skip
                } else {
                    dxf?.classes?.add(entity)
                }
            }
        }
        callback?.onEndClasses(this)
    }

    private fun parseTables(lex: Lexer, callback: Callback?) {
        if (callback != null && callback.onBeginTables(this)) {
            lex.skipTo(0, ENDSEC)
        } else {
            while (!lex.fetch(0, ENDSEC)) {
                val head = lex.loadEntity()
                val name = head.name?.intern()
                if (name == null) {
                    lex.skipTo(0, Tables.ENDTAB)
                } else if (callback != null && callback.onBeginTable(this, name)) {
                    lex.skipTo(0, Tables.ENDTAB)
                } else {
                    val records = ArrayList<Entity>()
                    while (!lex.fetch(0, Tables.ENDTAB)) {
                        val record = lex.loadEntity()
                        if (callback != null && callback.onLoadTableRecord(this, name, record)) {
                            //skip
                        } else {
                            records.add(record)
                        }
                    }
                    dxf?.tables?.add(head, records)
                }
                if (name != null) callback?.onEndTable(this, name)
            }
        }
        callback?.onEndTables(this)
    }

    private fun parseBlocks(lex: Lexer, callback: Callback?) {
        if (callback != null && callback.onBeginBlocks(this)) {
            lex.skipTo(0, ENDSEC)
        } else {
            while (!lex.fetch(0, ENDSEC)) {
                val head = lex.loadEntity()
                val name = head.name?.intern()
                if (name == null) {
                    lex.skipTo(0, Blocks.ENDBLK)
                    lex.loadEntity()
                } else if (callback != null && callback.onBeginBlock(this, name)) {
                    lex.skipTo(0, Blocks.ENDBLK)
                    lex.loadEntity()
                } else {
                    val entities = ArrayList<Entity>();
                    while (!lex.fetch(0, Blocks.ENDBLK)) {
                        val entity = lex.loadEntity()
                        if (callback != null && callback.onLoadBlockEntity(this, name, entity)) {
                            //skip
                        } else {
                            entities.add(entity)
                        }
                    }
                    val tail = lex.loadEntity()
                    dxf?.blocks?.add(head, entities, tail)
                }
                if (name != null) callback?.onEndBlock(this, name)
            }
        }
        callback?.onEndBlocks(this)
    }

    private fun parseEntities(lex: Lexer, callback: Callback?) {
        if (callback != null && callback.onBeginEntities(this)) {
            lex.skipTo(0, ENDSEC)
        } else {
            while (!lex.fetch(0, ENDSEC)) {
                val entity = lex.loadEntity()
                if (callback != null && callback.onLoadEntity(this, entity)) {
                    //skip
                } else {
                    dxf?.entities?.add(entity)
                }
            }
        }
        callback?.onEndEntities(this)
    }

    private fun parseObjects(lex: Lexer, callback: Callback?) {
        if (callback != null && callback.onBeginObjects(this)) {
            lex.skipTo(0, ENDSEC)
        } else {
            while (!lex.fetch(0, ENDSEC)) {
                val entity = lex.loadEntity()
                if (callback != null && callback.onLoadObject(this, entity)) {
                    //skip
                } else {
                    dxf?.objects?.add(entity)
                }
            }
        }
        callback?.onEndObjects(this)
    }

    private fun parseThumbnailImage(lex: Lexer, callback: Callback?) {
        if (callback != null && callback.onBeginThumbnailImage(this)) {
            lex.skipTo(0, ENDSEC)
        } else {
            while (!lex.fetch(0, ENDSEC)) {
                if (callback != null && callback.onLoadThumbnailImage(this, lex.dottedPair)) {
                    //skip
                } else {
                    dxf?.thumbnailimage?.add(lex.dottedPair)
                }
            }
        }
        callback?.onEndThumbnailImage(this)
    }

    class InputStreamLexer(src: InputStream): Lexer {

        //エンコーディングの問題はいわゆるLatin_1で読み込むことで、問題を先送りにする。
        private val reader = BufferedReader(InputStreamReader(src, "8859_1"))
        private var lno: Int = 0
        private var grp: Int = 0
        private var pair: Cell? = EOF

        class unfetchBuffer() {
            var backed: Boolean = false
            var grp: Int = 0
            var pair: Cell? = null

            fun back(grp: Int, pair: Cell?) {
                this.grp = grp
                this.pair = pair
                backed = true
            }
        }
        private val buf = unfetchBuffer()

        private fun readLine(): String? {
            val result = reader.readLine()?.trim()
            lno++
            return result
        }

        override fun fetch(): Int {
            if (buf.backed) {
                //戻されたのがあれば、それを使う
                grp = buf.grp
                pair = buf.pair
                buf.backed = false
            } else {
                val s = readLine()
                if (s == null) {
                    //  なぜかファイルが終わった
                    grp = FETCH_FAILED
                    pair = EOF
                } else if (s.trim().length() == 0) {
                    //value部分で、空行が入ってしまう処理系があるらしいので、その対策
                    return fetch()
                } else {
                    grp = Integer.parseInt(s.trim());
                    pair = null
                }
            }
            return grp
        }

        override fun unfetch() = buf.back(grp, pair)

        override val lineNo: Int get() = lno
        override val groupCode: Int get() = grp
        override val dottedPair: Cell get() {
            if (pair == null) {
                pair = when (Cell.groupCodeToType(grp)) {
                    Cell.SHT_CELL -> loadShort(grp)
                    Cell.INT_CELL -> loadInt(grp)
                    Cell.LNG_CELL -> loadLong(grp)
                    Cell.HND_CELL -> loadHandle(grp)
                    Cell.DBL_CELL -> loadDouble(grp)
                    Cell.STR_CELL -> loadString(grp)
                    Cell.PNT_CELL -> loadPoint(grp)
                    else -> Cell(grp, readLine())
                }
            }
            return pair as Cell
        }

        private fun loadShort(groupCode: Int): Cell {
            val value = readLine().toShort()
            return Cell(groupCode, value)
        }

        private fun loadInt(groupCode: Int): IntPair {
            val value = Integer.parseInt(readLine())
            return IntPair(groupCode, value)
        }

        private fun loadLong(groupCode: Int): LngPair {
            val value = Long.parseLong(readLine())
            return LngPair(groupCode, value)
        }

        private fun loadHandle(groupCode: Int): LngPair {
            val value = Long.parseLong(readLine(), 16)
            return LngPair(groupCode, value)
        }

        private fun loadDouble(groupCode: Int): DblPair {
            val value = Double.parseDouble(readLine())
            return DblPair(groupCode, value)
        }

        private fun loadString(groupCode: Int): StrPair {
            val value = readLine()
            return StrPair(groupCode, value)
        }

        private fun loadPoint(groupCode: Int): PntPair {
            val x = loadDouble(groupCode)
            if (fetch() == groupCode + 10) {
                val y = loadDouble(groupCode + 10)
                if (fetch() != groupCode + 20) {
                    val z = loadDouble(groupCode + 20)
                    return PntPair.fromDblPair(x, y, z)
                } else {
                    unfetch()
                    return PntPair.fromDblPair(x, y)
                }
            } else {
                //  ここにはこないはず
                unfetch()
                return PntPair(groupCode, x.dblValue)
            }
        }
    }
}