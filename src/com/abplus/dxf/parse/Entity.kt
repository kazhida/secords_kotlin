package com.abplus.dxf.parse

import jet.List
import jet.Iterable
import java.util.ArrayList

/**
    エンティティ
    List<Cell>のラッパーでImmutableにしている


 * User: kazhida
 * Date: 2012/08/03
 * Time: 10:01
 */

open class Entity(src: MutableList<Cell>): Iterable<Cell> {
    //  複製する
    private val list: List<Cell> = src //ArrayList<Cell>(src)

    fun size(): Int = list.size()
    val length: Int get() = size()

    fun get(idx: Int): Cell = list.get(idx)

    fun assoc(groupCode: Int): Cell? {
        for (pair in list) if (pair.groupCode == groupCode) return pair
        return null
    }

    val handle: Long? get() = assoc(5)?.asLong
    val name: String? get() = assoc(2)?.asString

    public override fun iterator(): Iterator<Cell> {
        return list.iterator();
    }
}
