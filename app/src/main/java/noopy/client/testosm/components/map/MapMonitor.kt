package noopy.client.testosm.components.map

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import noopy.client.testosm.R
import noopy.client.testosm.service.MarkerService
import org.osmdroid.tileprovider.MapTileProviderBase
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.regex.Pattern

class MapMonitor : MapView {

    private var boatMarker: Marker = Marker(this)

    constructor(context: Context,
                tileProvider: MapTileProviderBase,
                tileRequestCompleteHandler: Handler, attrs: AttributeSet) : super(context, tileProvider, tileRequestCompleteHandler, attrs) {
        init()
    }

    constructor(context: Context,
                tileProvider: MapTileProviderBase,
                tileRequestCompleteHandler: Handler, attrs: AttributeSet, hardwareAccelerated: Boolean) : super(context, tileProvider, tileRequestCompleteHandler, attrs, hardwareAccelerated) {
        init()
    }

    /**
     * Constructor used by XML layout resource (uses default tile source).
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context,
                aTileProvider: MapTileProviderBase) : super(context, aTileProvider) {
        init()
    }

    constructor(context: Context,
                aTileProvider: MapTileProviderBase,
                tileRequestCompleteHandler: Handler) : super(context, aTileProvider, tileRequestCompleteHandler) {
        init()
    }

    fun init() {

        setTileSource(object : OnlineTileSourceBase("ArcGis", 0, 18, 256, "",
               // arrayOf("https://server.arcgisonline.com/ArcGIS/rest/services/NatGeo_World_Map/MapServer/tile/{z}/{y}/{x}")) {
               arrayOf("https://services.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}")) {
            override fun getTileURLString(pMapTileIndex: Long): String {

                return buildTileUrl(baseUrl, MapTileIndex.getX(pMapTileIndex), MapTileIndex.getY(pMapTileIndex), MapTileIndex.getZoom(pMapTileIndex))

            }
        })

        minZoomLevel = 3.0
        controller.zoomTo(3.0)

        setBuiltInZoomControls(false)
        setMultiTouchControls(true)
        addBoatMarker()

    }

    private fun addBoatMarker() {
        Marker.ENABLE_TEXT_LABELS_WHEN_NO_IMAGE = true

        boatMarker.title = "hello world"
        val markerIcon = createBoatIcon(0.0)
        boatMarker.setIcon(markerIcon)
        boatMarker.position = GeoPoint(0.0,0.0)
        overlays.add(boatMarker)
        invalidate()

    }

    private fun createBoatIcon(angle: Double): BitmapDrawable {
        var bmp =  MarkerService(context).getBmp(R.drawable.fragments_skipper_boat_marker, 80.0)
        var bmp2 = MarkerService(context).transformBmp(bmp!!, angle, 1.0)

        return BitmapDrawable(resources, bmp2)
    }

    fun updateBoatAngle(angle: Double) {
        val markerIcon = createBoatIcon(angle)
        boatMarker.setIcon(markerIcon)
        invalidate()
    }

    fun updateBoatPosition(pos: GeoPoint) {
        boatMarker.position = pos
        invalidate()
    }

    fun centerTo(position: GeoPoint) {
        controller.setCenter(position)
    }

    companion object {
        fun buildTileUrl(path: String, x: Int, y: Int, z: Int): String {
            val pattern = Pattern.compile("\\{([^}]*)\\}")
            val output = StringBuffer()
            val matcher = pattern.matcher(path)
            while (matcher.find()) {
                val key = matcher.group(1)
                if (key == "x") {
                    matcher.appendReplacement(output, "$x")
                }
                if (key == "y") {
                    matcher.appendReplacement(output, "$y")
                }
                if (key == "z") {
                    matcher.appendReplacement(output, "$z")
                }
            }
            matcher.appendTail(output)
            Log.i("MAP URL", output.toString())
            return output.toString()
        }
    }
}
