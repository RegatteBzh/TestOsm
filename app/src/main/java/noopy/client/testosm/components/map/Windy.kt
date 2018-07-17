package noopy.client.testosm.components.map

import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay


class Windy: Overlay {

    var particles: MutableList<Particle> = mutableListOf<Particle>()
    var mapView: MapView? = null


    constructor(mapView: MapView?): super() {
        Log.i("WINDY", "New windy !")
        if (mapView != null) {
            addToMap(mapView!!)
            //generateParticles()
        }
    }

    override fun draw(canvas: Canvas, map: MapView, shadow: Boolean) {
        generateParticles(map.projection.intrinsicScreenRect.width(), map.projection.intrinsicScreenRect.height())
        Log.i("WINDY", "Draw")
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
}