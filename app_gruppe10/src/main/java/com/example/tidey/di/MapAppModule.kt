package com.example.tidey.di

import android.app.Application
import androidx.room.Room
import com.example.tidey.common.Constants
import com.example.tidey.data.repository.MapRepositoryImpl
import com.example.tidey.data.local.GarbageMapPointDatabase
import com.example.tidey.data.remote.TideApi
import com.example.tidey.data.repository.TideRepositoryImpl
import com.example.tidey.domain.repository.MapRepository
import com.example.tidey.domain.repository.TideRepository
import com.example.tidey.domain.use_case.AddMapPoint
import com.example.tidey.domain.use_case.DeleteMapPoint
import com.example.tidey.domain.use_case.GetMapPoints
import com.example.tidey.domain.use_case.UseCases
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapAppModule {

    @Singleton
    @Provides
    fun provideMapDatabase(app: Application): GarbageMapPointDatabase {
        return Room.databaseBuilder(
            app,
            GarbageMapPointDatabase::class.java,
            "garbage_spots.db"
        )
            .allowMainThreadQueries()
            .build()
    }

    @Singleton
    @Provides
    fun provideMapRepository(db: GarbageMapPointDatabase, mapPointRef: CollectionReference): MapRepository {
        return MapRepositoryImpl(mapPointRef, db.dao)
    }

    @Singleton
    @Provides
    fun tideApiService(): TideApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(TideApi::class.java)
    }

    @Singleton
    @Provides
    fun provideTideRepository(api: TideApi): TideRepository {
        return TideRepositoryImpl(api)
    }

    //Firebase firestore
    @Provides
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    fun provideMapPointsRef(
        db: FirebaseFirestore
    ) = db.collection("mapPoints")


    @Provides
    fun provideUseCases(
        repo: MapRepository
    ) = UseCases(
        getMapPoints = GetMapPoints(repo),
        addMapPoint = AddMapPoint(repo),
        deleteMapPoint = DeleteMapPoint(repo)
    )
}
