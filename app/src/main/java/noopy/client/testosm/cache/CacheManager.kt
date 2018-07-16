package noopy.client.testosm.cache

import android.content.Context
import android.util.Log
import java.io.File

class CacheManager {

    companion object {

        fun checkForFolder(context: Context?, name: String): File? {
            if (context == null) {
                return null
            }
            var success = false
            val cache_directory = File(context!!.getFilesDir().toString()
                    + File.separator + "cache" + File.separator + name)
            Log.i("CreateDir", cache_directory.toString())
            if (!cache_directory.exists()) {
                success = cache_directory.mkdirs()
            }
            if (success) {
                Log.i("CreateDir", "Directory created!")
            } else {
                Log.w("CreateDir", "Unable to create dir!")
            }

            return cache_directory
        }

        fun getTileCache(context: Context?): File? {
            return checkForFolder(context, "tiles")
        }

        fun getPolarCache(context: Context?): File? {
            return checkForFolder(context, "polars")
        }
    }
}