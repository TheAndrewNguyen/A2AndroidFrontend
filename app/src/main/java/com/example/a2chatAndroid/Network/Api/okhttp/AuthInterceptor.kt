package com.example.a2chatAndroid.Network.Api.okhttp

import com.example.a2chatAndroid.Network.Firebase.authGetIdToken
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor {
    suspend fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${authGetIdToken()}")
            .build()
        return chain.proceed(request)
    }
}