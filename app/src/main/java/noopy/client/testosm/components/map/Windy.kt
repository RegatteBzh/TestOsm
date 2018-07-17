package noopy.client.testosm.components.map

import android.graphics.Canvas
import android.graphics.Rect
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay


class Windy: Overlay {

    val particles: MutableList<Particle> = mutableListOf<Particle>()
    var mapView: MapView? = null
    var width: Int = 0
    var height: Int = 0


    constructor(mapView: MapView?, width: Int, height: Int): super() {
        if (mapView != null) {
            addToMap(mapView!!)
            generateParticles()
        }
        this.width = width
        this.height = height
    }

    override fun draw(canvas: Canvas, map: MapView, shadow: Boolean) {
        if (!isEnabled()) return;
        if (shadow) {
            //draw a shadow if needed, otherwise return
            return;
        }
        map.getProjection().save(canvas, false, false);
        particles.forEach {
            it.draw(canvas)
        }
        map.getProjection().restore(canvas, false);
    }

    fun generateParticles() {
        for (i in 1..200) {
            if (mapView != null) particles.add(Particle.random(width, height, mapView!!.projection))
        }
    }

    private fun addToMap(mapView: MapView) {
        this.mapView = mapView
        mapView.overlays.add(this)
    }
}