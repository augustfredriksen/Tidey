package com.example.tidey.domain.model.tide


import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import java.net.SocketTimeoutException


class PredictionXmlParser {
    @Throws(XmlPullParserException::class, IOException::class, SocketTimeoutException::class)
    suspend fun parse(inputStream: InputStream): Prediction {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()

            // Tide
            val prediction = Prediction()
            prediction.parse(parser)
            return prediction
        }
    }
}