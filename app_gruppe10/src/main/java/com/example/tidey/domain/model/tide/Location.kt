package com.example.tidey.domain.model.tide

import com.example.tidey.data.remote.IParseTide
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

data class Location(

    var name: String? = null,
    var code: String? = null,
    var latitude: String? = null,
    var longitude: String? = null,
/*    var delay: String? = null,
    var factor: String? = null,
    var obsname: String? = null,
    val obscode: String? = null,
    var descr: String? = null,*/
    var text: String? = ""

): IParseTide {

    @Throws(XmlPullParserException::class, IOException::class)
    override suspend fun parse(parser: XmlPullParser) {
        parser.require(XmlPullParser.START_TAG, ns, "location")
        // Attributter:
        name = parser.getAttributeValue(null, "name")
        code = parser.getAttributeValue(null, "code")
        latitude = parser.getAttributeValue(null, "latitude")
        longitude = parser.getAttributeValue(null, "longitude")
/*        delay = parser.getAttributeValue(null, "delay")
        factor = parser.getAttributeValue(null, "factor")
        obsname = parser.getAttributeValue(null, "obsname")
        descr = parser.getAttributeValue(null, "descr")*/

        if (parser.next() == XmlPullParser.TEXT) {
            text = parser.text
        }

        parser.require(XmlPullParser.END_TAG, ns, "location")
    }
}
