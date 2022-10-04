package com.example.tidey.domain.model.tide

import com.example.tidey.data.remote.IParseTide
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

data class NoData(
    var info: String? = null,
    var text: String? = ""
): IParseTide {

    @Throws(XmlPullParserException::class, IOException::class)
    override suspend fun parse(parser: XmlPullParser) {
        parser.require(XmlPullParser.START_TAG, ns, "nodata")
        // Attributter:
        info = parser.getAttributeValue(null, "info")
        if (parser.next() == XmlPullParser.TEXT) {
            text = parser.text
        }

        parser.require(XmlPullParser.END_TAG, ns, "nodata")
    }
}