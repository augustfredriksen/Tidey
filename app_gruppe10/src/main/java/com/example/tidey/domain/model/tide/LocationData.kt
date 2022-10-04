package com.example.tidey.domain.model.tide

import android.icu.text.IDNA
import com.example.tidey.data.remote.IParseTide
import com.example.tidey.domain.model.tide.Data
import com.example.tidey.domain.model.tide.Location
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

data class LocationData(
    var location: Location? = null,
    var data: Data?= null,
    var noData: NoData? = null,
    /*var refLevelCode: RefLevelCode?= null*/
): IParseTide {

    @Throws(XmlPullParserException::class, IOException::class)
    override suspend fun parse(parser: XmlPullParser) {
        parser.require(XmlPullParser.START_TAG, ns, "locationdata")
        while (parser.next() != XmlPullParser.END_TAG) {
            // Dersom ikke start-TAG, gå til til toppen av while()
            if (parser.eventType != XmlPullParser.START_TAG)
                continue
            // Håndterer ulike start-TAGger:
            when (parser.name) {
                "location" -> {
                    val loc =  Location()
                    loc.parse(parser)
                    location = loc
                }
                /*"reflevelcode" -> {
                    val ref =  RefLevelCode()
                    ref.parse(parser)
                    refLevelCode = ref
                }*/
                "data" -> {
                    var data2 = Data()
                    data2.parse(parser)
                    data = data2
                }
                "nodata" -> {
                    var noData2 = NoData()
                    noData2.parse(parser)
                    noData = noData2
                }
                else -> skip(parser)
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "locationdata")
    }
}
