package com.example.tidey.domain.model.tide

import android.util.Log
import com.example.tidey.data.remote.IParseTide
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException


data class Tide(
    var locationdata: LocationData? = null,
): IParseTide {

    @Throws(XmlPullParserException::class, IOException::class)
    override suspend fun parse(parser: XmlPullParser) {
        try {
            parser.require(XmlPullParser.START_TAG, ns, "tide")
            while (parser.next() != XmlPullParser.END_TAG) {
                // Dersom ikke start-TAG, gå til til toppen av while()
                if (parser.eventType != XmlPullParser.START_TAG)
                    continue
                // Håndterer ulike start-TAGger:
                when (parser.name) {
                    "locationdata" -> {
                        val loc =  LocationData()
                        loc.parse(parser)
                        locationdata = loc
                    }
                    else -> skip(parser)
                }
            }
            parser.require(XmlPullParser.END_TAG, ns, "tide")
        } catch (e: XmlPullParserException) {
            Log.d("Test4", e.toString())
        }


    }
}
