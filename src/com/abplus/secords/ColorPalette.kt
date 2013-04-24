package com.abplus.secords

import android.content.Context
import android.graphics.Color
import android.widget.BaseAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.util.TypedValue
import android.view.Gravity
import android.graphics.drawable.Drawable
import android.widget.ListView
import android.widget.AbsListView
import android.util.Log

/**
 * User: kazhida
 * Date: 2012/08/13
 * Time: 9:15
 */
class ColorPalette(private val context: Context) {
    class object {
        private var shared: ColorPalette? = null

        fun sharedInstance(context: Context): ColorPalette {
            Log.d("ColorPalette", "sharedInstance allocated.")
            shared = ColorPalette(context)
            return shared as ColorPalette
        }

        fun sharedInstance(): ColorPalette? {
            Log.d("ColorPalette", if (shared == null) "null" else "not null")
            return shared
        }
    }

    class ColorItem(val idx: Int, val color: Int, val name: String, val icon: Drawable)

    val resources = context.getResources()

    private fun colorOf(id: Int): Int {
        return resources?.getColor(id)!!
    }

    private fun stringOf(id: Int): String {
        return resources?.getString(id)!!
    }

    private fun iconOf(id: Int): Drawable {
        return resources?.getDrawable(id)!!
    }

    private val colors = arrayList<ColorItem>(
            ColorItem(0, colorOf(R.color.red), stringOf(R.string.red), iconOf(R.drawable.ic_palette_red)),
            ColorItem(1, colorOf(R.color.yellow), stringOf(R.string.yellow), iconOf(R.drawable.ic_palette_yellow)),
            ColorItem(2, colorOf(R.color.green), stringOf(R.string.green), iconOf(R.drawable.ic_palette_green)),
            ColorItem(3, colorOf(R.color.cyan), stringOf(R.string.cyan), iconOf(R.drawable.ic_palette_cyan)),
            ColorItem(4, colorOf(R.color.blue), stringOf(R.string.blue), iconOf(R.drawable.ic_palette_blue)),
            ColorItem(5, colorOf(R.color.magenta), stringOf(R.string.magenta), iconOf(R.drawable.ic_palette_magenta)),
            ColorItem(6, colorOf(R.color.white), stringOf(R.string.white), iconOf(R.drawable.ic_palette_white)))

    var selected: Int = 0
    val currentColor: Int get() = getColor(selected)
    fun getColor(index: Int): Int = colors[index].color

    fun getAdapter(): Adapter = Adapter()

    class Adapter(): BaseAdapter() {
        public override fun getCount(): Int {
            return colors.size
        }

        public override fun getItem(p0: Int): Any? {
            return colors.get(p0)
        }

        public override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        public override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View? {
            var result: TextView? = if (p1 is TextView) p1 else null

            if (result == null) {
                result = TextView(context)
                result?.setLayoutParams(AbsListView.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ))
                result?.setTextSize(TypedValue.COMPLEX_UNIT_PT, 9.0.toFloat());
                result?.setPadding(4, 8, 4, 8);
                result?.setGravity(Gravity.CENTER_HORIZONTAL);
            }
            val item = colors[p0]
            result?.setCompoundDrawablesWithIntrinsicBounds(item.icon, null, null, null)
            result?.setText(item.name)
            if (p0 == selected) {
                result?.setBackgroundColor(colorOf(R.color.selected))
            } else {
                result?.setBackgroundColor(colorOf(R.color.transparent))
            }

            result?.setTag(item)

            return result
        }
    }
}