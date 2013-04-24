package com.abplus.secords

import android.app.Activity
import android.os.Bundle
import android.view.View.OnClickListener
import android.view.View
import android.widget.ListView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView
import android.widget.Adapter
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.graphics.PointF
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.content.Context

/**
 * User: kazhida
 * Date: 2012/08/13
 * Time: 10:39
 */
class MainActivity(): Activity() {

    var calcFragment: CalcFragment? = null
    var drawView: DrawingView? = null
    val drawingItems = DrawingItems()
    val drawer = AnnotationDrawer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_p)

        findViewById(R.id.show_palette_button)?.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(p0: View?) {
                findViewById(R.id.palette_panel)?.setVisibility(View.VISIBLE)
                findViewById(R.id.show_palette_button)?.setVisibility(View.GONE)
            }
        })

        findViewById(R.id.hide_palette_button)?.setOnClickListener(object : View.OnClickListener{
            public override fun onClick(p0: View?) {
                findViewById(R.id.show_palette_button)?.setVisibility(View.VISIBLE)
                findViewById(R.id.palette_panel)?.setVisibility(View.GONE)
            }
        })

        val colorPalette = ColorPalette.sharedInstance(this)
        val adapter = colorPalette.getAdapter()
        val palette = findViewById(R.id.color_palette) as ListView
        palette.setAdapter(adapter)
        palette.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            public override fun onItemClick(p0: AdapterView<out Adapter?>?, p1: View?, p2: Int, p3: Long) {
                //                val parent = p0
                //                val view = p1
                val position = p2
                //                val id = p3
                colorPalette.selected = position
                adapter.notifyDataSetChanged()
            }
        })

        //        drawView = DrawingView(this)
        //        drawView?.callback = drawer.callback
        //        drawView?.drawer = drawer
        val view = DrawingView.create(this)
        view.callback = drawer.callback
        view.drawer = drawer
        drawView = view

        (findViewById(R.id.main_layout) as FrameLayout).addView(drawView)

        //  電卓フラグメントの初期化
        calcFragment = getFragmentManager()?.findFragmentById(R.id.calc) as CalcFragment?
        calcFragment?.hideMemoryRow()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater()?.inflate(R.menu.options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.getItemId()) {
            R.id.calc_function_key_visible -> {
                if (item != null) {
                    if (item.isChecked()) {
                        item.setChecked(false)
                        calcFragment?.hideFunctionRow()
                    } else {
                        item.setChecked(true)
                        calcFragment?.showFunctionRow()
                    }
                }
                return true
            }
            R.id.calc_memory_key_visible -> {
                if (item != null) {
                    if (item.isChecked()) {
                        item.setChecked(false)
                        calcFragment?.hideMemoryRow()
                    } else {
                        item.setChecked(true)
                        calcFragment?.showMemoryRow()
                    }
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private class AnnotationDrawer(): DrawingView.Drawer {

        var stroke: Stroke? = null
        var paint: Paint? = null
        val callback = Callback()

        public override fun draw(canvas: Canvas) {
            canvas.drawColor(Color.BLACK)
            if (paint == null) {
                paint = Paint(Paint.ANTI_ALIAS_FLAG)
                paint?.setStyle(Paint.Style.STROKE)
            }

            for (s in drawingItems.annotations) {
                paint?.setColor(s.color)
                var q: PointF? = null
                for (p in s) {
                    if (q != null) {
                        canvas.drawLine(q!!.x, q!!.y, p.x, p.y, paint)
                    }
                    q = p
                }
            }

        }

        private class Callback(): DrawingView.Callback {

            public override fun onDown(position: PointF) {
                var palette = ColorPalette.sharedInstance()
                var color = if (palette != null) palette?.currentColor else Color.RED
                stroke = Stroke(color!!)
                stroke?.add(position)
                drawingItems.annotations.add(stroke!!)
                stroke = stroke
                drawView?.redraw()
            }

            public override fun onUp(position: PointF) {
                if (stroke != null && stroke!!.length < 2) {
                    drawingItems.annotations.remove(stroke)
                }
                stroke = null
            }

            public override fun onScroll(position: PointF, distance: PointF) {
                stroke?.add(position)
                drawView?.redraw();
            }

            public override fun onScale(focus: PointF, scaleFactor: Float) {
                //nop
            }
        }
    }
}