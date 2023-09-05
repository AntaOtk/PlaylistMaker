package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.library.data.db.AppDatabase
import com.example.playlistmaker.search.data.local.LocalStorage
import com.example.playlistmaker.search.data.local.SharedPreferenceLocalStorage
import com.example.playlistmaker.search.data.network.ApiSearch
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<ApiSearch> {
        Retrofit.Builder().baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiSearch::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    }

    factory { Gson() }

    factory { MediaPlayer() }

    single<LocalStorage> {
        SharedPreferenceLocalStorage(get(), get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    factory<ExternalNavigator> { ExternalNavigatorImpl(get<Context>()) }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
}
