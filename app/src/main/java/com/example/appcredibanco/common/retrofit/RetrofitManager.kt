package com.example.appcredibanco.common.retrofit

import com.example.appcredibanco.common.di.ApiUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val TIME_OUT = 30L

@Module
@InstallIn(SingletonComponent::class)
object RetrofitManager {

    @Provides
    @Singleton
    @ApiUrl
    fun providerApiUrl(): String = "http://10.0.2.2:8080/api/payments/"

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder().addInterceptor(this).addInterceptor(TransactionInterceptor())
            .build()
        /*.protocols(listOf(Protocol.HTTP_2, Protocol.HTTP_1_1))
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)*/
    }

    @Singleton
    @Provides
    fun providerRetrofit(@ApiUrl apiUrl: String, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun providerApiClient(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
