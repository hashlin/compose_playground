package com.linminphyo.meditation.movie.details.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.HttpUrl

class RestAdapter(private val context: Context) {
    fun getRetrofit() : Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(getClient(context))
            .build()

    }

    private fun getClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor.Builder(context).build())
            .addInterceptor(HeaderInterceptor)
            .build()
    }
}

private val HeaderInterceptor = Interceptor { chain ->
    val originalHttpUrl: HttpUrl = chain.request().url()
    val newUrl = originalHttpUrl.newBuilder()
        .addQueryParameter("api_key", "f1bc64eea111cd0dec8d6f88159df07f")
        .build()

    val newRequest = chain.request().newBuilder().url(newUrl).build()
    chain.proceed(newRequest)
}