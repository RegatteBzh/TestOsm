package noopy.client.testosm.model.windy

import android.graphics.Point

class XyPoint {

    var x: Double = 0.0
    var y: Double = 0.0

    constructor(x: Double, y: Double) {
        this.x = x
        this.y = y
    }

    constructor(x: Int, y: Int) {
        this.x = x.toDouble()
        this.y = y.toDouble()
    }

    constructor(p: Point) {
        this.x = p.x.toDouble()
        this.y = p.y.toDouble()
    }

    fun toPoint(): Point {
        return Point(x.toInt(), y.toInt())
    }
}