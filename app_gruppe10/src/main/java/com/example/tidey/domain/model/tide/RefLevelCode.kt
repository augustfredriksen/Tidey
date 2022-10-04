package com.example.tidey.domain.model.tide


import com.example.tidey.data.remote.IParseTide
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

data class RefLevelCode(
    var text: String? = ""
) : IParseTide {

    @Throws(XmlPullParserException::class, IOException::class)
    override suspend fun parse(parser: XmlPullParser) {
        parser.require(XmlPullParser.START_TAG, ns, "reflevelcode")
        if (parser.next() == XmlPullParser.TEXT) {
            text = readText(parser)
        }
        parser.require(XmlPullParser.END_TAG, ns, "reflevelcode")
    }
}
