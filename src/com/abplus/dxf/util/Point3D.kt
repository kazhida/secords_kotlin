package com.abplus.dxf.basic

/**
    3次元座標あるいは3次元ベクトルを表すクラス

    DXFでは2次元の座標も扱うが、それは、z=0の3次元として扱うことにしています。

    2次元を3次元とは別のクラスとする選択肢もないではないのですが、
    掛ける手間と得られる利点を天秤に掛けたら、それほどのメリットがなく、
    こういう扱いでも、特に問題がないと判断しました。

 * User: kazhida
 * Date: 2012/08/05
 * Time: 9:38
 */
class Point3D(val x: Double, val y: Double, val z: Double = 0.0) {

    /**
    * ノルム
    * @return  ノルム
    */
    val norm: Double  get() = Math.sqrt(x * x + y * y + z * z)

    /**
     * 同じ方向の単位ベクトルを返すメソッド
     * @return  単位方向ベクトル
     */
    fun unit(): Point3D {
        val n = norm
        return Point3D(x / n, y / n, z / n)
    }

    /**
     * 各成分の加算したベクトルを返すメソッド
     * @param p 加算するベクトル
     * @return  加算したベクトル
     */
    fun sum(p: Point3D): Point3D {
        return Point3D(this.x + p.x, this.y + p.y, this.z + p.z)
    }
    //  演算子オーバーロード用
    fun plus(p: Point3D): Point3D = sum(p);


    /**
     * 各成分の減算したベクトルを返すメソッド
     * @param p 減算するベクトル
     * @return  減算したベクトル
     */
    fun difference(p: Point3D): Point3D {
        return Point3D(this.x - p.x, this.y - p.y, this.z - p.z)
    }
    //  演算子オーバーロード用
    fun minus(p: Point3D): Point3D = difference(p);

    /**
     * ドット積を返すメソッド
     * @param p 掛けるベクトル
     * @return  ドット積
     */
    fun dot_product(p: Point3D): Double {
        return this.x * p.x + this.y * p.y + this.z * p.z
    }
    //  演算子オーバーロード用
    fun times(p: Point3D): Double = dot_product(p);

    /**
     * クロス積を返すメソッド
     * @param p 掛けるベクトル
     * @return  クロス積
     */
    fun cross_product(p: Point3D): Point3D {
        val x = this.y * p.z - this.z * p.y
        val y = this.z * p.x - this.x * p.z
        val z = this.x * p.y - this.y * p.x
        return Point3D(x, y, z)
    }

    /**
     *  スケール
     *  @param k 掛ける係数
     *  @return 各成分をk倍したベクトル
     */
    fun scale(k: Double): Point3D {
        return scale(k, k, k)
    }
    //  演算子オーバーロード用
    fun times(k: Double): Point3D = scale(k)

    /**
     *  スケール
     *  @param x X成分に掛ける係数
     *  @param y Y成分に掛ける係数
     *  @param z Z成分に掛ける係数
     *  @return 各成分に指定した係数を掛けたベクトル
     */
    fun scale(x: Double, y: Double, z: Double): Point3D {
        return Point3D(this.x * x, this.y * y, this.z * z)
    }

    /**
     *  移動
     *  @param x X成分に掛ける係数
     *  @param y Y成分に掛ける係数
     *  @param z Z成分に掛ける係数
     *  @return 各成分に指定した係数を掛けたベクトル
     */
    fun shift(x: Double, y: Double, z: Double): Point3D {
        return Point3D(this.x + x, this.y + y, this.z + z)
    }

    fun reverse(): Point3D {
        return Point3D(-x, -y, -z)
    }

    fun copy(): Point3D {
        return Point3D(x, y, z)
    }

    fun toString(): String = "[${x}, ${y}, ${z}]"
}
