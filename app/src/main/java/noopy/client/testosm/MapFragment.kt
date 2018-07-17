package noopy.client.testosm

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import noopy.client.testosm.cache.CacheManager
import noopy.client.testosm.components.map.MapMonitor
import noopy.client.testosm.components.map.Windy
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.OverlayItem

class MapFragment : Fragment() {

    private var mapComponent: MapMonitor? = null
    private var currentContext: Context? = null
    private var windy: Windy? = null

    private var currentPosition: GeoPoint = GeoPoint(0.0,0.0)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        Configuration.getInstance().osmdroidTileCache = CacheManager.checkForFolder(activity, "tiles")

        val rootView = inflater?.inflate(R.layout.fragment_map, container, false)

        currentContext = rootView?.context

        mapComponent = rootView?.findViewById(R.id.mapView) as MapMonitor

        updateMarkerAngle(12.5)
        currentPosition = GeoPoint(-49.9, 68.5)
        updateMarkerPosition(currentPosition)

        mapComponent?.centerTo(currentPosition)

        if (mapComponent != null) windy = Windy(mapComponent!!)

        mapComponent!!.addMapListener(object : MapListener {
            override fun onZoom(evt: ZoomEvent): Boolean {
                Log.i("MAP", "Zoom " + evt.toString())
                return true
            }

            override fun onScroll(evt: ScrollEvent): Boolean {
                Log.i("MAP", "Scroll " + evt.toString())
                return true
            }
        })


        return rootView
    }

    override fun onPause() {
        super.onPause()
        mapComponent?.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapComponent?.onResume()
    }

    private fun updateMarkerAngle(angle: Double) {
        mapComponent?.updateBoatAngle(angle)
    }

    private fun updateMarkerPosition(pos: GeoPoint) {
        mapComponent?.updateBoatPosition(pos)
    }



    companion object {

        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }
}