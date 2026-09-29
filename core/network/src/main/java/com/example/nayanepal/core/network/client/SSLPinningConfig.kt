package com.example.nayanepal.core.network.client

import okhttp3.CertificatePinner
import okhttp3.Interceptor

object SSLPinningConfig {
    fun getNetworkInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
                .build()
            chain.proceed(request)
        }
    }
    
    fun getCertificatePinner(): CertificatePinner {
        return CertificatePinner.Builder().build()
    }
}
