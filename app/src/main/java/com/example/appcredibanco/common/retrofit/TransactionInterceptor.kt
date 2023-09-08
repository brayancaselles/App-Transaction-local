package com.example.appcredibanco.common.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TransactionInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request =
            chain.request().newBuilder().header("Authorization", "Basic MDAwMTIzMDAwQUJD")
                .addHeader("Content-type", "application/json")
                .build()
        return chain.proceed(request)
    }
}
