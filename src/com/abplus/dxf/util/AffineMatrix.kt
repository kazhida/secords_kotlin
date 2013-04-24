package com.abplus.dxf.basic

import jet.List
import java.util.ArrayList

/**
    アフィン変換用マトリクス

 * User: kazhida
 * Date: 2012/08/05
 * Time: 10:04
 */
class AffineMatrix(private val x: Point3D,
                   private val y: Point3D,
                   private val z: Point3D,
                   private val w: Point3D = Point3D(0.0, 0.0, 0.0)) {
    /*
     * | x.x  x.y  x.z  w.x |
     * | y.x  y.y  y.z  w.y |
     * | z.x  z.y  z.z  w.z |
     * |   0    0    0    1 |
     */

    //  演算子オーバーロード用
    fun times(a: AffineMatrix): AffineMatrix = product(a);

    fun product(a: AffineMatrix): AffineMatrix {
        val vx = Point3D(a.x.x, a.y.x, a.z.x)
        val vy = Point3D(a.x.y, a.y.y, a.z.y)
        val vz = Point3D(a.x.z, a.y.z, a.z.z)

        val hx = Point3D(x * vx, x * vy, x * vz)
        val hy = Point3D(y * vx, y * vy, y * vz)
        val hz = Point3D(z * vx, z * vy, z * vz)
        val vw = Point3D(x * a.w + w.x, y * a.w + w.y, z * a.w + w.z)

        return AffineMatrix(hx, hy, hz, vw)
    }

    //    private fun product(x: Point3D,
    //                        y: Point3D,
    //                        z: Point3D,
    //                        w: Point3D = Point3D(0.0, 0.0, 0.0)): AffineMatrix {
    //        return product(AffineMatrix(x, y, z, w))
    //    }

    /**
    * 伸縮
    * @param k
    * @return
    */
    fun scale(k: Double): AffineMatrix = scale(k, k, k)

    /**
     * 伸縮
     * @param x
     * @param y
     * @param z
     * @return
     */
    fun scale(x: Double, y: Double, z: Double): AffineMatrix {
        val px = Point3D(x, 0.0, 0.0)
        val py = Point3D(0.0, y, 0.0)
        val pz = Point3D(0.0, 0.0, z)
        return this * AffineMatrix(px, py, pz)
    }

    /**
    * 伸縮
    * @param k
    * @return
    */
    fun scale(k: Point3D): AffineMatrix = scale(k.x, k.y, k.z)

    /**
     * 移動
     * @param x
     * @param y
     * @param z
     * @return
     */
    fun translate(x: Double, y: Double, z: Double): AffineMatrix {
        //        val px = Point3D(1.0, 0.0, 0.0)
        //        val py = Point3D(0.0, 1.0, 0.0)
        //        val pz = Point3D(0.0, 0.0, 1.0)
        //        val pw = Point3D(x, y, z)
        //        return product(px, py, pz, pw)
        return translate(Point3D(x, y, z))
    }

    /**
     * 移動
     * @param d
     * @return
     */
    fun translate(d: Point3D): AffineMatrix = AffineMatrix(x, y, z, w + d)

    //    /**
    //     * 回転（Z軸周り）
    //     * @param a	回転角度(度)
    //     * @return
    //     */
    //    fun rotate(a: Double): AffineMatrix {
    //        val d = a * 180.0 / Math.PI
    //        val c = Math.cos(d)
    //        val s = Math.sin(d)
    //        val px = Point3D(  c,  -s, 0.0)
    //        val py = Point3D(  s,   c, 0.0)
    //        val pz = Point3D(0.0, 0.0, 1.0)
    //        return this * AffineMatrix(px, py, pz);
    //    }

    /**
     *  変換
     * @param p 座標
     * @return 変換後の座標
     */
    fun apply(p: Point3D): Point3D {
        val dx = x * p + w.x
        val dy = y * p + w.y
        val dz = z * p + w.z
        return Point3D(dx, dy, dz)
    }
    //  演算子オーバーロード用
    fun times(p: Point3D): Point3D = apply(p)


    /**
     *  あとで内包表記を使うために、
     *  List<Point3D>に拡張関数を追加
     */
    fun List<Point3D>.map(f: (Point3D) -> Point3D): List<Point3D> {
        val dst = ArrayList<Point3D>()
        for (val p in this) {
            dst.add(f(p))
        }
        return dst
    }

    /**
     *  変換
     *
     * @param ps 座標のリスト
     * @return 変換後の座標
     */
    fun apply(ps: List<Point3D>): List<Point3D> = ps.map({(p: Point3D): Point3D -> apply(p) })
    /*Note::
        関数リテラルの型指定の仕方と、実装時の表記方法が似ているのに、
        微妙に違っているのが変な感じ
    ::Note*/

    val isIdentity: Boolean get() =
    (x.x == 1.0) && (x.y == 0.0) && (x.z == 0.0) && (w.x == 0.0) &&
    (y.x == 0.0) && (y.y == 1.0) && (y.z == 0.0) && (w.y == 0.0) &&
    (z.x == 0.0) && (z.y == 0.0) && (z.z == 1.0) && (w.z == 0.0)

    val getScaleX: Double get() = x.x
    val getScaleY: Double get() = y.y
    val getScaleZ: Double get() = z.z
    val getSkewXY: Double get() = x.y
    val getSkewXZ: Double get() = x.z
    val getSkewYX: Double get() = y.x
    val getSkewYZ: Double get() = y.z
    val getSkewZX: Double get() = z.x
    val getSkewZY: Double get() = z.y
    val getTransX: Double get() = w.x
    val getTransY: Double get() = w.y
    val getTransZ: Double get() = w.z

    fun copy(): AffineMatrix = AffineMatrix(x, y, z, w)

    class object {

        /**
        * 単位行列を作るファクトリ・メソッド
        */
        fun identity(): AffineMatrix {
            val px = Point3D(1.0, 0.0, 0.0)
            val py = Point3D(0.0, 1.0, 0.0)
            val pz = Point3D(0.0, 0.0, 1.0)
            return AffineMatrix(px, py, pz)
        }

        /**
        * 押し出し方向を与えられたときの変換行列を作るファクトリ・メソッド
        * @param extrusion
        */
        fun fromExtrusion(extrusion: Point3D): AffineMatrix {
            val az = extrusion.unit()
            val ax = if (Math.abs(az.x) < 1.0 / 64 && Math.abs(az.y) < 1.0 / 64) {
                Point3D(0.0, 1.0, 0.0).cross_product(az).unit()
            } else {
                Point3D(0.0, 0.0, 1.0).cross_product(az).unit()
            }
            val ay = az.cross_product(ax).unit()

            val px = Point3D(ax.x, ay.x, az.x);
            val py = Point3D(ax.y, ay.y, az.y);
            val pz = Point3D(ax.z, ay.z, az.z);

            return AffineMatrix(px, py, pz)
        }
    }
}
