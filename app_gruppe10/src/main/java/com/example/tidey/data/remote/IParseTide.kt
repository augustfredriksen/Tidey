package com.example.tidey.data.remote

import org.xmlpull.v1.XmlPullParser

interface IParseTide {
    suspend fun parse(parser: XmlPullParser)
}