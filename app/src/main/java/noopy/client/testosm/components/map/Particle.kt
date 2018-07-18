package noopy.client.testosm.components.map

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.Log
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.Projection
import java.util.*

class Particle(var pixelPoint: Point, val projection: Projection, var age: Int) {

    var geoPoint: GeoPoint
    get() {
        val latLng = projection.fromPixels(pixelPoint.x, pixelPoint.y)
        return GeoPoint(latLng.latitude, latLng.longitude)
    }
    set(value: GeoPoint) {
        pixelPoint = projection.toPixels(value, pixelPoint)
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
            return Particle(Point(r.nextInt(width), r.nextInt(height)), projection, 50)
        }
    }

}