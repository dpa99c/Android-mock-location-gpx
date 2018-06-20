package com.mockloc.jtymicki.androidmocklocation

import android.util.Log
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.text.SimpleDateFormat

class ParseItems {
    companion object {
        const val TAG = "MainActivity"
    }

    val items = ArrayList<TrackingPoint>()
    var dateFormat = SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss'Z'")

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
                                "ele" -> currentRecord.ele = textValue.toDouble()
                                "time" -> {
                                    val time = dateFormat.parse(textValue)
                                    Log.i(TAG, time.toString())
                                    currentRecord.trackingPointtime = textValue

                                }
                            }
                        }
                    }
                }
                eventType = xpp.next()
            }
        } catch (e: Exception) {
            status = false
        }
        return status
    }
}