package com.application.brainnforce.network

import android.content.Context
import android.text.TextUtils
import com.application.brainnforce.BuildConfig
import com.application.brainnforce.common.BNFConstants
import com.application.brainnforce.local.PrefUtils
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    private var retrofit: Retrofit? = null
    private val requestTimeout = 120
    private var okHttpClient: OkHttpClient? = null

    fun getClient(context: Context): Retrofit {

        if (okHttpClient == null)
            initOkHttp(context)

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BNFConstants.BASE_API)
                .client(okHttpClient!!)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    private fun initOkHttp(context: Context) {
        val httpClient = OkHttpClient().newBuilder()
            .connectTimeout(requestTimeout.toLong(), TimeUnit.SECONDS)
            .readTimeout(requestTimeout.toLong(), TimeUnit.SECONDS)
            .writeTimeout(requestTimeout.toLong(), TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        httpClient.addInterceptor(interceptor)

        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
//                .addHeader("Accept", "application/json")
//                .addHeader("Content-Type", "application/json")
                .addHeader("deviceType", "A")
                .addHeader("deviceId", "1")
                .addHeader("appVersion", "1.0")


            if (!TextUtils.isEmpty(PrefUtils().getAuthKey(context))) {
                requestBuilder.addHeader("Authorization","Bearer ${PrefUtils().getAuthKey(context)!!}")
            }

        /*     Adding Authorization token (API Key)
             Requests will be denied without API key
            if (!TextUtils.isEmpty(PrefUtils().getAuthKey(context))) {
                requestBuilder.addHeader("Authorization","Bearer ${PrefUtils().getAuthKey(context)!!}")
            }

             Adding FCM token
            if (!TextUtils.isEmpty(PrefUtils().getFirebaseToken(context))) {
                requestBuilder.addHeader("token",PrefUtils().getFirebaseToken(context)!!)
            }
*/

            val request = requestBuilder.build()
            chain.proceed(request)
        }

        okHttpClient = httpClient.build()
    }

}