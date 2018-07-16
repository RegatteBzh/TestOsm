package noopy.client.testosm

import android.app.Fragment
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import noopy.client.testosm.cache.CacheManager
import noopy.client.testosm.service.MarkerService
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.OverlayItem

class MapFragment : Fragment() {

    private var mapComponent: MapView? = null
    private var currentContext: Context? = null
    private var overlayList = ArrayList<OverlayItem>()

    private var boatMarker: Marker? = null
    private var currentPosition: GeoPoint = GeoPoint(0.0,0.0)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        Configuration.getInstance().osmdroidTileCache = CacheManager.checkForFolder(activity, "tiles")

        val rootView = inflater?.inflate(R.layout.fragment_map, container, false)

        currentContext = rootView?.context

        mapComponent = rootView?.findViewById(R.id.mapView) as MapView

        configureMap()



        updateMarkerAngle(12.5)
        currentPosition = GeoPoint(-49.9, 68.5)
        updateMarkerPosition(currentPosition)
        if (mapComponent!= null) mapComponent!!.getController().setCenter(currentPosition);


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

    private fun configureMap() {
        mapComponent?.setTileSource(object : OnlineTileSourceBase("ArcGis", 0, 18, 256, "",
                arrayOf("https://server.arcgisonline.com/ArcGIS/rest/services/NatGeo_World_Map/MapServer/tile/")) {
            override fun getTileURLString(pMapTileIndex: Long): String {

                return (baseUrl + MapTileIndex.getZoom(pMapTileIndex) + "/" + MapTileIndex.getY(pMapTileIndex) + "/" + MapTileIndex.getX(pMapTileIndex)
                        + mImageFilenameEnding)
            }
        })

        mapComponent?.minZoomLevel = 3.0;
        mapComponent?.controller?.zoomTo(3.0)

        mapComponent?.setBuiltInZoomControls(false);
        mapComponent?.setMultiTouchControls(true);
        addBoatMarker()
    }


    private fun createBoatIcon(angle: Double): BitmapDrawable? {
        if (currentContext != null) {

            var bmp =  MarkerService(currentContext!!).getBmp(R.drawable.fragments_skipper_boat_marker, 80.0)
            var bmp2 = MarkerService(currentContext!!).transformBmp(bmp!!, angle, 1.0)

            return BitmapDrawable(resources, bmp2)

        }
        return null
    }

    private fun addBoatMarker() {
        Marker.ENABLE_TEXT_LABELS_WHEN_NO_IMAGE = true

        boatMarker = Marker(mapComponent)
        boatMarker?.title = "hello world"
        val markerIcon = createBoatIcon(0.0)
        boatMarker?.setIcon(markerIcon)
        boatMarker?.position = GeoPoint(0.0,0.0)
        mapComponent?.overlays?.add(boatMarker)
        mapComponent?.invalidate()

    }

    private fun updateMarkerAngle(angle: Double) {
        val markerIcon = createBoatIcon(angle.toDouble())
        boatMarker?.setIcon(markerIcon)
        mapComponent?.invalidate()
    }

    private fun updateMarkerPosition(pos: GeoPoint) {
        boatMarker?.position = pos;
        mapComponent?.invalidate()
    }



    companion object {

        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }
}