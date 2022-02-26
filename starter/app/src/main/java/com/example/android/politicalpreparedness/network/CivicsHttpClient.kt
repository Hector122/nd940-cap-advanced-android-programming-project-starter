package com.example.android.politicalpreparedness.network

import okhttp3.OkHttpClient
import java.util.logging.Level

class CivicsHttpClient : OkHttpClient() {
    
    companion object {
        
        const val API_KEY =
            "AIzaSyCha4kPQqnI7-3hHX1T1yYJ_SafDlxYjMo" //complete: Place your API Key Here
        
        fun getClient(): OkHttpClient {
            return Builder().addInterceptor { chain ->
                val original = chain.request()
                
                val url = original.url()
                    .newBuilder()
                    .addQueryParameter("key", API_KEY)
                    .build()
                
                val request = original.newBuilder()
                    .url(url)
                    .build()
                chain.proceed(request)
                
            }.build()
        }
    }
}
//
//
//// set your desired log level
//logging.setLevel(Level.BODY);
//
//OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//// add your other interceptors …
//
//// add logging as last interceptor
//httpClient.addInterceptor(logging);  // <-- this is the important line!