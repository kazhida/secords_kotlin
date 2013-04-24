package com.abplus.dxf.parse

/**
 * Created with IntelliJ IDEA.
 * User: kazhida
 * Date: 2012/09/25
 * Time: 19:08
 * To change this template use File | Settings | File Templates.
 */
public class Cell(val groupCode: Int, raw: String) {
    private val raw = raw;
    val asString: String    get() = raw;
    val asShort: Short      get() = raw.toShort();
    val asInt: Int          get() = raw.toInt();
    val asLong: Long        get() = raw.toLong();
    val asFloat: Float      get() = raw.toFloat();
    val asDouble: Double    get() = raw.toDouble();
}

