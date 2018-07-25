package noopy.client.testosm.model.windy

import android.graphics.Canvas
import android.graphics.Point
import android.os.Handler
import android.util.Log
import noopy.client.testosm.model.wind.WindMap
import noopy.client.testosm.model.wind.WindSpeed
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay


class Windy: Overlay {

    var particles: MutableList<Particle> = mutableListOf<Particle>()
    var mapView: MapView? = null
    val animationInterval: Long = 500
    private var animationHandler: Handler? = null
    var windMap: WindMap? = null


    constructor(mapView: MapView?): super() {
        Log.i("WINDY", "New windy !")
        if (mapView != null) {
            addToMap(mapView)
            //generateParticles()
        }
    }

    override fun draw(canvas: Canvas, map: MapView, shadow: Boolean) {
        stopAnimation()
        generateParticles(map.projection.intrinsicScreenRect.width(), map.projection.intrinsicScreenRect.height())
        Log.i("WINDY", "Draw")
        if (!isEnabled) return
        if (shadow) {
            //draw a shadow if needed, otherwise return
            return
        }

        map.getProjection().save(canvas, false, false);
        drawParticules(particles, canvas)

        startAnimation(canvas)
        map.getProjection().restore(canvas, false);
    }

    fun drawParticules(particleSet: MutableList<Particle>, canvas: Canvas) {
        particleSet.forEach {
            it.draw(canvas)
        }
    }

    fun computeParticles(particleSet: MutableList<Particle>) {
        particleSet.forEach {
            val point = it.pixelPoint
            val geoPoint = it.geoPoint

            if (windMap!=null) {
                val area = mapView!!.projection.boundingBox.latitudeSpan *  mapView!!.projection.boundingBox.longitudeSpan
                val velocityScale = 0.01 * Math.pow(area, 0.4)
                val speed = windMap!!.getWindAt(geoPoint)
                if (speed != null) {
                    val correctedSpeed = distort(geoPoint, point, velocityScale, speed!!)
                    val newPoint = XyPoint(point.x + correctedSpeed.u, point.y + correctedSpeed.v)
                }
            }

        }
    }

    fun stopAnimation() {
        animationHandler?.removeCallbacksAndMessages(null)
        animationHandler = null
    }

    fun startAnimation(canvas: Canvas) {
        animationHandler = Handler()
        val runnable = object: Runnable{
            override fun run() {
                computeParticles(particles)
                drawParticules(particles, canvas)
                animationHandler?.postDelayed(this, animationInterval)
            }
        }
        animationHandler?.postDelayed(runnable, animationInterval)
    }

    fun generateParticles(width: Int, height: Int) {
        particles = mutableListOf<Particle>()
        Log.i("WINDY", "Generate particles")
        for (i in 1..200) {
            if (mapView != null) particles.add(Particle.random(width, height, mapView!!.projection))
        }
    }

    private fun addToMap(mapView: MapView) {
        Log.i("WINDY", "Add to map")
        this.mapView = mapView
        mapView.getOverlayManager().add(this);
    }

    private fun canvasToMap (pixelPoint: Point): GeoPoint {
        if (mapView != null) {
            val latLng = mapView!!.projection.fromPixels(pixelPoint.x, pixelPoint.y)
            return GeoPoint(latLng.latitude, latLng.latitude)
        }
        return GeoPoint(0.0, 0.0)
    }

    private fun mapToCanvas(latLng: GeoPoint): Point {
        if (mapView != null) {
            return mapView!!.projection.toPixels(latLng, Point())
        }
        return Point()
    }

    private fun distortion(latLng: GeoPoint, point: XyPoint): DoubleArray{
        val tau = 2 * Math.PI
        val H = Math.pow(10.0, -5.2) * 180 / Math.PI
        val hLongitude = if (latLng.longitude<0) H else -H
        val hLatitude = if (latLng.latitude<0) H else -H

        val pLongitude = mapToCanvas(GeoPoint(latLng.latitude, latLng.longitude + hLongitude))
        val pLatitude = mapToCanvas(GeoPoint(latLng.latitude + hLatitude, latLng.longitude))

        val k = Math.cos(latLng.latitude / 360 * tau);

        return doubleArrayOf(
            (pLongitude.x - point.x).toDouble() / hLongitude / k,
            (pLongitude.y - point.y).toDouble() / hLongitude / k,
            (pLatitude.x - point.x).toDouble() / hLatitude,
            (pLatitude.y - point.y).toDouble() / hLatitude
        )
    }

    private fun distort(latLng: GeoPoint, point: XyPoint, scale: Double, wind: WindSpeed): WindSpeed {
        val u = wind.u * scale;
        val v = wind.v * scale;
        val d = distortion(latLng, point);
        return WindSpeed(
            d.get(0) * u + d.get(2) * v,
            d.get(1) * u + d.get(3) * v
        )
    }
}