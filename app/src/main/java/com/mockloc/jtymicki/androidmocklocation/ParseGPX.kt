package com.mockloc.jtymicki.androidmocklocation

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.text.SimpleDateFormat

private const val TAG = "ParseGPX"

class ParseGPX {
    val items = ArrayList<TrackingPoint>()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'")
    var previousPointTimeStamp: Long = 0

/*    class ErrorDialogFragment(message: String) : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(requireContext())
                .setMessage(getString(message))
                .setPositiveButton("OK") { _,_ -> }
                .create()

        companion object {
            const val TAG = "ErrorDialog"
        }
    }*/

    fun showErrorDialog(errorMessage: String){
        AlertDialog.Builder(MainActivity.appContext)
            .setTitle("Error")
            .setMessage(errorMessage)
            .setPositiveButton("OK") { _,_ -> }
            .show()
    }

    fun parse(xmlData: String): Boolean {
        var status = true
        var inItem = false
        var textValue = ""
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val xpp = factory.newPullParser()
            xpp.setInput(xmlData.reader())
            var eventType = xpp.eventType
            var currentRecord = TrackingPoint()
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = xpp.name?.toLowerCase()
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (tagName == "trkpt") {
                            inItem = true
                            currentRecord.lat = xpp.getAttributeValue(null, "lat").toDouble()
                            currentRecord.lon = xpp.getAttributeValue(null, "lon").toDouble()
                        }
                    }
                    XmlPullParser.TEXT -> textValue = xpp.text
                    XmlPullParser.END_TAG -> {
                        if (inItem) {
                            when (tagName) {
                                "trkpt" -> {
                                    items.add(currentRecord)
                                    inItem = false
                                    currentRecord = TrackingPoint()
                                }
                                "ele" -> currentRecord.altitude = textValue.toDouble()
                                "accuracy" -> currentRecord.accuracy = textValue.toFloat()
                                "speed" -> currentRecord.speed = textValue.toFloat()
                                "time" -> {
                                    val time = dateFormat.parse(textValue)
//                                    Log.i(TAG, time.toString())
                                    currentRecord.timestamp = time.time
                                    if (items.size == 0) {
                                        currentRecord.pointDelay = 0
                                    } else {
                                        currentRecord.pointDelay = time.time - previousPointTimeStamp
                                    }
                                    previousPointTimeStamp = time.time
                                }
                            }
                        }
                    }
                }
                eventType = xpp.next()
            }
        }
        catch (e: Exception) {
            Log.e(TAG, e.toString())
            showErrorDialog("Failed to parse GPX: $e")
            status = false
        }
        return status
    }
}