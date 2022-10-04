package com.example.tidey.domain.model.tide

import com.example.tidey.data.remote.IParseTide
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

data class WaterLevel(
    var value: String? = null,
    var time: String? = null,
    var flag: String? = null,
    var text: String? = ""
): IParseTide {

    @Throws(XmlPullParserException::class, IOException::class)
    override suspend fun parse(parser: XmlPullParser) {
        parser.require(XmlPullParser.START_TAG, ns, "waterlevel")
        // Attributter:
        value = parser.getAttributeValue(null, "value")
        time = parser.getAttributeValue(null, "time")
        flag = parser.getAttributeValue(null, "flag")
        if (parser.next() == XmlPullParser.TEXT) {
            text = parser.text
        }

        parser.require(XmlPullParser.END_TAG, ns, "waterlevel")
    }
}
