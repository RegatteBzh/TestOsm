package noopy.client.testosm.model.wind

import org.json.JSONException
import org.json.JSONObject

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by cyrille on 09/12/17.
 */

class WindHeader {
    var dx: Double = 0.0
    var dy: Double = 0.0
    var la1: Double = 0.0
    var lo1: Double = 0.0
    var la2: Double = 0.0
    var lo2: Double = 0.0
    var nx: Int = 0
    var ny: Int = 0
    var parameterCategory: Int = 0
    var parameterNumber: Int = 0
    var parameterNumberName: String
    var parameterUnit: String
    var refTime: Date

    @Throws(JSONException::class) constructor(data: JSONObject) {
        this.dx = if (data.has("dx")) data.getDouble("dx") else 0.0
        this.dy = if (data.has("dy")) data.getDouble("dy") else 0.0
        this.la1 = if (data.has("la1")) data.getDouble("la1") else 0.0
        this.la2 = if (data.has("la2")) data.getDouble("la2") else 0.0
        this.lo1 = if (data.has("lo1")) data.getDouble("lo1") else 0.0
        this.lo2 = if (data.has("lo2")) data.getDouble("lo2") else 0.0
        this.nx = if (data.has("nx")) data.getInt("nx") else 0
        this.ny = if (data.has("ny")) data.getInt("ny") else 0
        this.parameterCategory = if (data.has("parameterCategory")) data.getInt("parameterCategory") else 0
        this.parameterNumber = if (data.has("parameterNumber")) data.getInt("parameterNumber") else 0

        this.parameterNumberName = if (data.has("parameterNumberName")) data.getString("parameterNumberName") else ""
        this.parameterUnit = if (data.has("parameterUnit")) data.getString("parameterUnit") else ""

        if (data.has("refTime")) {
            val timeStr = data.getString("refTime")
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            try {
                this.refTime = format.parse(timeStr.trim { it <= ' ' })
            } catch (e: ParseException) {
                this.refTime = Date()
            }

        } else {
            this.refTime = Date()
        }
    }
}
