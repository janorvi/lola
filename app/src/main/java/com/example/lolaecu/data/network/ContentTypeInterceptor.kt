package com.example.lolaecu.data.network

import okhttp3.Interceptor
import okhttp3.Response

class ContentTypeInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain
            .request()
        val requestBuilder = request.newBuilder()
        requestBuilder.addHeader("Content-Type", "application/json")
        val finalRequest = requestBuilder.build()
        return chain.proceed(finalRequest)
    }

}