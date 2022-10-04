package com.example.tidey.domain.model.tide

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.net.SocketTimeoutException

val ns: String? = null

class TideXmlParser {
    @Throws(XmlPullParserException::class, IOException::class, SocketTimeoutException::class)
    suspend fun parse(inputStream: InputStream): Tide {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()

            // Tide
            val tide = Tide()
            tide.parse(parser)
            return tide
        }
    }
}

@Throws(XmlPullParserException::class, IOException::class)
fun readElementText(parser: XmlPullParser, element: String): String? {
    parser.require(XmlPullParser.START_TAG, ns, element)
    val text = readText(parser)
    parser.require(XmlPullParser.END_TAG, ns, element)
    return text
}

// Leser TEXT-verdient til gjeldende tag:
@Throws(IOException::class, XmlPullParserException::class)
fun readText(parser: XmlPullParser): String {
    var result = ""
    if (parser.next() == XmlPullParser.TEXT) {
        result = parser.text
        parser.nextTag()
    }
    return result
}

// skip()
@Throws(XmlPullParserException::class, IOException::class)
fun skip(parser: XmlPullParser) {
    if (parser.eventType != XmlPullParser.START_TAG) {
        throw IllegalStateException()
    }
    var depth = 1
    while (depth != 0) {
        when (parser.next()) {
            XmlPullParser.END_TAG -> depth--
            XmlPullParser.START_TAG -> depth++
        }
    }
}