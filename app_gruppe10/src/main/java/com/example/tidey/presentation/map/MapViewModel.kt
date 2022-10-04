package com.example.tidey.presentation.map

import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.tidey.domain.model.GarbageMapPoint
import com.example.tidey.domain.model.tide.Prediction
import com.example.tidey.domain.model.tide.PredictionXmlParser
import com.example.tidey.domain.model.tide.Tide
import com.example.tidey.domain.model.tide.TideXmlParser
import com.example.tidey.domain.repository.AuthRepository
import com.example.tidey.domain.repository.MapRepository
import com.example.tidey.domain.repository.TideRepository
import com.example.tidey.domain.use_case.UseCases
import com.example.tidey.presentation.navigation.Screen
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

@HiltViewModel
class MapViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val mapRepo: MapRepository,
    private val tideRepo: TideRepository,
    private val useCases: UseCases
) : ViewModel() {
    // firebase firestore

    private val _Garbage_mapPointPointState = mutableStateOf<com.example.tidey.domain.model.Response<List<GarbageMapPoint>>>(
        com.example.tidey.domain.model.Response.Loading
    )
    val garbageMapPointPointState: State<com.example.tidey.domain.model.Response<List<GarbageMapPoint>>> = _Garbage_mapPointPointState

    private val _isMapPointAddedState =
        mutableStateOf<com.example.tidey.domain.model.Response<Void?>>(
            com.example.tidey.domain.model.Response.Success(null)
        )

    private val _isMapPointDeletedState =
        mutableStateOf<com.example.tidey.domain.model.Response<Void?>>(
            com.example.tidey.domain.model.Response.Success(null)
        )

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private fun addMapPoint(
        lat: Double,
        lng: Double,
        garbageLevel: String,
        user: String,
        dateReported: String,
        id: String
    ) {
        viewModelScope.launch {
            useCases.addMapPoint(
                lat = lat,
                lng = lng,
                garbageLevel = garbageLevel,
                user = user,
                dateReported = dateReported,
                id = id
            ).collect { response ->
                _isMapPointAddedState.value = response
            }
        }
    }

    private fun deleteMapPoint(Id: String) {
        viewModelScope.launch {
            useCases.deleteMapPoint(Id).collect { response ->
                _isMapPointDeletedState.value = response
            }
        }
    }

    private fun getMapPoints() {
        viewModelScope.launch {
            useCases.getMapPoints().collect { response ->
                _Garbage_mapPointPointState.value = response
            }
        }
    }

    fun addMapPointsToRoom() {
        viewModelScope.launch {
            getMapPoints()
            when (val mapPointResponse = garbageMapPointPointState.value) {
                is com.example.tidey.domain.model.Response.Success -> mapPointResponse.data?.forEach { data ->
                    mapRepo.insertMap(
                        GarbageMapPoint(
                            lat = data.lat,
                            lng = data.lng,
                            dateReported = data.dateReported,
                            garbageLevel = data.garbageLevel,
                            user = data.user,
                            id = data.id!!
                        )
                    )
                }
                else -> {
                    Log.d("gg", "ping pong")
                }
            }
        }
    }

    //Auth
    private val _signOutState = mutableStateOf<com.example.tidey.domain.model.Response<Boolean>>(
        com.example.tidey.domain.model.Response.Success(
            false
        )
    )
    val signOutState: State<com.example.tidey.domain.model.Response<Boolean>> = _signOutState
    val displayName get() = authRepo.getDisplayName()
    val photoUrl get() = authRepo.getPhotoUrl()

    fun currentDate(plusDays: Long, format: String): String {
        return tideRepo.getCurrentDate(plusDays, format)
    }

    //Auth SignOut
    fun signOut(navController: NavController) {
        Log.d("Test3", "SignOut before launch: ${_signOutState.value}")
        viewModelScope.launch {
            authRepo.signOut().collect { response ->
                _signOutState.value = response
                Log.d("Test3", "SignOut after launch: ${_signOutState.value}")
                navController.popBackStack()
                navController.navigate(Screen.Auth.route)
            }
        }
    }

    //Tide Api
    private var _tide = MutableLiveData<Tide?>()
    val tide: LiveData<Tide?> = _tide

    private var _tidePrediction = MutableLiveData<Prediction?>()
    val tidePrediction: LiveData<Prediction?> = _tidePrediction

    var xmlPullParserException = MutableLiveData<Boolean>(false)

    val data: HashMap<String, String> = LinkedHashMap()
    fun insertData(
        lat: String,
        lon: String,
    ): HashMap<String, String> {
        data["lat"] = lat
        data["lon"] = lon
        data["fromtime"] = currentDate(0, "yyyy-MM-dd")
        data["totime"] = currentDate(1, "yyyy-MM-dd")
        data["datatype"] = "tab"
        data["refcode"] = "cd"
        data["place"] = ""
        data["file"] = ""
        data["lang"] = "nb"
        data["interval"] = "10"
        data["dst"] = "0"
        data["tzone"] = ""
        data["tide_request"] = "locationdata"
        return data
    }

    val data2: HashMap<String, String> = LinkedHashMap()
    fun insertData2(
        lat: String,
        lon: String,
    ): HashMap<String, String> {
        data2["lat"] = lat
        data2["lon"] = lon
        data2["fromtime"] = currentDate(0, "yyyy-MM-dd")
        data2["totime"] = currentDate(1, "yyyy-MM-dd")
        data2["datatype"] = "pre"
        data2["refcode"] = "cd"
        data2["place"] = ""
        data2["file"] = ""
        data2["lang"] = "nb"
        data2["interval"] = "10"
        data2["dst"] = "0"
        data2["tzone"] = ""
        data2["tide_request"] = "locationdata"
        return data2
    }

    fun notReady() {
        _tide.value = null
    }

    fun getTide(data: kotlin.collections.Map<String, String>) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(coroutineContext) {
                val response: Response<ResponseBody> = tideRepo.downloadXml(data)
                var parser = TideXmlParser()
                _tide.postValue(parser.parse(response.body()!!.byteStream()))
            }
        }
    }

    fun getTidePredictions(data: kotlin.collections.Map<String, String>) {
        Log.d("Test2", "tide value before launch: ${tidePrediction.value}")
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("Test2", "return tide: $tidePrediction")
            val response: Response<ResponseBody> = tideRepo.downloadXml(data)
            Log.d("Test8", response.toString())
            var parser = PredictionXmlParser()
            var tide2: Prediction?
            tide2 = parser.parse(response.body()!!.byteStream())
            _tidePrediction.postValue(tide2)
        }
    }

    private val currentWaterLevelMap: HashMap<String, String> = LinkedHashMap()
    fun addTestHashMap() {
        try {
            tidePrediction.value?.locationdata?.data?.waterLevelList?.forEach { waterLevel ->
                currentWaterLevelMap[waterLevel.time.toString().drop(11).dropLast(12)] =
                    waterLevel.value.toString()
            }
            var highestLow = 0.0
            tide.value?.locationdata?.data?.waterLevelList?.forEach { waterLevel ->

                if (waterLevel.flag == "low" && highestLow < waterLevel.value?.toDoubleOrNull()!!) {
                    highestLow = waterLevel.value?.toDoubleOrNull()!!
                }
            }
            if (currentWaterLevelMap.get(currentDate(0, "HH"))
                    ?.toDoubleOrNull()!! < (highestLow + (highestLow.div(10)))
            ) {
                _tideText.value = "Det er fjære nå! Perfekt tidspunkt for å plukke søppel!"
            } else {
                _tideText.value = "Har du planer om strandrydding, bør du vente til det er fjære."
            }
        } catch (e: java.lang.NullPointerException) {
            Log.d("e", e.toString())
        }
    }


    //Map
    var state by mutableStateOf(MapState())

    private var _latLng: MutableLiveData<LatLng> = MutableLiveData()
    val latLng: LiveData<LatLng> = _latLng

    private var _markerId: MutableLiveData<Int?> = MutableLiveData()
    val markerId: LiveData<Int?> = _markerId

    private var _marker: MutableLiveData<GarbageMapPoint?> = MutableLiveData()
    val marker: LiveData<GarbageMapPoint?> = _marker

    private var _address: MutableLiveData<String> = MutableLiveData()
    val address: LiveData<String> = _address

    private var _city: MutableLiveData<String> = MutableLiveData()
    val city: LiveData<String> = _city

    private var _county: MutableLiveData<String> = MutableLiveData()
    val county: LiveData<String> = _county

    private var _country: MutableLiveData<String> = MutableLiveData()
    val country: LiveData<String> = _country

    private var _postalCode: MutableLiveData<String> = MutableLiveData()
    val postalCode: LiveData<String> = _postalCode

    private var _tideText: MutableLiveData<String> = MutableLiveData()
    val tideText: LiveData<String> = _tideText

    var garbageLevel: MutableLiveData<String> = MutableLiveData()

    init {
        viewModelScope.launch() {
            mapRepo.getMap().collectLatest { spots ->
                state = state.copy(
                    garbageMapPoint = spots
                )
            }
        }
    }

    fun getInfoFromMarker(geocoder: Geocoder, lat: Double, lng: Double) {
        try {
            var addresses = geocoder.getFromLocation(lat, lng, 1)
            _address.value = addresses[0].getAddressLine(0)
            _country.value = addresses[0].subAdminArea
        } catch (e: NullPointerException) {
            Log.d("error", "getInfoFromMarker: $e")
        }
    }


    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.ToggleDarkMode -> {
                state = state.copy(
                    properties = state.properties.copy(
                        mapStyleOptions = if (state.isDarkMap) {
                            null
                        } else MapStyleOptions(MapStyle.json),
                    ),
                    isDarkMap = !state.isDarkMap
                )
            }
            is MapEvent.OnMapClick -> {
                _latLng.value = LatLng(event.latLng.latitude, event.latLng.longitude)
                _marker.value = null

                try {
                    insertData(
                        event.latLng.latitude.toString(),
                        event.latLng.longitude.toString(),
                    )
                    getTide(data)

                    insertData2(
                        event.latLng.latitude.toString(),
                        event.latLng.longitude.toString(),
                    )
                    getTidePredictions(data2)

                } catch (e: NullPointerException) {
                    Log.d("error", "onEvent: $e")
                }
            }

            is MapEvent.InsertGarbageMapPoint -> {
                viewModelScope.launch {
                    try {
                        addMapPoint(
                            lat = event.latLng.latitude,
                            lng = event.latLng.longitude,
                            garbageLevel = garbageLevel.value!!,
                            user = displayName,
                            dateReported = currentDate(0, "dd-MM-yyyy HH:mm"),
                            id = event.latLng.latitude.toString() + event.latLng.longitude.toString()
                        )
                    } catch (e: NullPointerException) {
                        Log.d("Error!!", e.toString())
                    }
                }
            }

            is MapEvent.OnInfoWindowClick -> {
                viewModelScope.launch {
                    _marker.value = mapRepo.getMarkerById(event.spot.id!!)
                }
            }

            is MapEvent.OnInfoWindowLongClick -> {
                viewModelScope.launch {
                    deleteMapPoint(event.spot.lat.toString() + event.spot.lng.toString())
                    mapRepo.deleteMap(event.spot)
                }
            }
        }
    }
}