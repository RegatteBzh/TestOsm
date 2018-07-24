package noopy.client.testosm.model.wind

import org.json.JSONException
import org.json.JSONObject
import org.osmdroid.util.GeoPoint

/**
 * Created by cyrille on 09/12/17.
 */

class WindAxis {
    var data: Array<FloatArray> = arrayOf()
    var header: WindHeader? = null

    @Throws(JSONException::class) constructor(data: JSONObject) {
        if (data.has("header")) {
            this.header = WindHeader(data.getJSONObject("header"))
            this.data = Array(this.header!!.nx) { FloatArray(this.header!!.ny) }

            if (data.has("data")) {
                val dataLst = data.getJSONArray("data")
                for (i in 0 until dataLst.length()) {
                    this.data[i % this.header!!.nx][i / this.header!!.nx] = dataLst.getDouble(i).toFloat()
                }
            }

        }
    }

    fun getWindAt(position: GeoPoint): Float {
        val x = Math.round((position.longitude + 360) % 360 / this.header!!.dx).toInt()
        val y = Math.round((this.header!!.la1 - position.latitude) / this.header!!.dy).toInt()
        return this.data[x][y]
    }
}