package noopy.client.testosm.model.windy

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.Projection
import java.util.*

class Particle(var pixelPoint: XyPoint, val projection: Projection, var age: Int) {

    var geoPoint: GeoPoint
    get() {
        val latLng = projection.fromPixels(Math.round(pixelPoint.x).toInt(), Math.round(pixelPoint.y).toInt())
        return GeoPoint(latLng.latitude, latLng.longitude)
    }
    set(value: GeoPoint) {
        pixelPoint = XyPoint(projection.toPixels(value, pixelPoint.toPoint()))
    }

    fun draw(canvas: Canvas) {
        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = Color.rgb(255, 0,0)
        paint.setStrokeWidth(5.toFloat());
        canvas.drawPoint(pixelPoint.x.toFloat(), pixelPoint.y.toFloat(), paint)
    }

    companion object {
        fun random(width: Int, height: Int, projection: Projection): Particle {
            val r = Random()
            return Particle(XyPoint(r.nextInt(width), r.nextInt(height)), projection, 50)
        }
    }

}