package noopy.client.testosm.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat

class MarkerService(val context: Context) {



    fun getBmp(resourceId: Int, mHeight: Double): Bitmap? {
        var drawable = ContextCompat.getDrawable(context, resourceId)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable!!).mutate()
        }

        if (drawable != null) {

            val height = drawable!!.intrinsicHeight
            val width = drawable!!.intrinsicWidth

            val ratio = mHeight / height.toFloat()
            val scaledHeight = (height.toFloat() * ratio).toInt()
            val scaledWidth = (width.toFloat() * ratio).toInt()
            val bmp = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmp)
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
            drawable.draw(canvas)

            return bmp

        }
        return null
    }

    fun transformBmp(bmp: Bitmap, angle: Double, scale: Double): Bitmap {
        val matrix = Matrix()
        matrix.postTranslate(-bmp.width.toFloat() / 2, -bmp.height.toFloat() / 2)
        matrix.postRotate(angle.toFloat())
        matrix.postTranslate(bmp.width.toFloat() / 2, bmp.height.toFloat() / 2)
        matrix.postScale(scale.toFloat(), scale.toFloat())

        return Bitmap.createBitmap(bmp, 0, 0, (bmp.width * scale).toInt(), (bmp.height * scale).toInt(), matrix, true);
    }


}