package com.example.tidey.domain.model.tide

import com.example.tidey.data.remote.IParseTide
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

data class Data(
    var type: String? = null,
    var unit: String? = null,
    var waterLevelList: MutableList<WaterLevel>? = mutableListOf<WaterLevel>()
) : IParseTide {

    @Throws(XmlPullParserException::class, IOException::class)
    override suspend fun parse(parser: XmlPullParser) {
        parser.require(XmlPullParser.START_TAG, ns, "data")

        // Attributter:
        type = parser.getAttributeValue(null, "type")
        unit = parser.getAttributeValue(null, "unit")

        while (parser.next() != XmlPullParser.END_TAG) {

            // Dersom ikke start-TAG, gå til til toppen av while()
            if (parser.eventType != XmlPullParser.START_TAG)
                continue
            // Håndterer ulike start-TAGger:
            when (parser.name) {
                "waterlevel" -> {
                    val levels = WaterLevel()
                    levels.parse(parser)
                    this.waterLevelList?.add(levels)
                }
                else -> skip(parser)
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "data")
    }
}

