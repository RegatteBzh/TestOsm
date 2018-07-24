package noopy.client.testosm.model.wind

import org.json.JSONArray
import org.json.JSONException
import org.osmdroid.util.GeoPoint

import java.util.ArrayList

/**
 * Created by cyrille on 09/12/17.
 */

class WindMap {
    private val data: MutableList<WindAxis> = ArrayList()

    @Throws(JSONException::class) constructor(data: JSONArray) {
        for (i in 0 until data.length()) {
            this.data.add(WindAxis(data.getJSONObject(i)))
        }
    }

    @Throws(JSONException::class) constructor(data: String): this(JSONArray(data))

    val u: WindAxis?
        get() {
            for (i in this.data.indices) {
                if (this.data[i].header!!.parameterNumber == 2) {
                    return this.data[i]
                }
            }
            return null
        }

    val v: WindAxis?
        get() {
            for (i in this.data.indices) {
                if (this.data[i].header!!.parameterNumber == 3) {
                    return this.data[i]
                }
            }
            return null
        }

    fun getWindAt(position: GeoPoint): WindSpeed? {
        if (this.data.size != 2) {
            return null
        }
        val u = this.u
        val v = this.v

        return if (u == null || v == null) {
            null
        } else WindSpeed(u.getWindAt(position).toDouble(), v.getWindAt(position).toDouble())

    }

}
