package com.faurecia.http

import android.util.Base64
import android.util.Log
import android.webkit.URLUtil
import com.cainiao.base.dialog.ProgressDialog
import com.cainiao.base.http.Api
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.URLDecoder
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/**
 * @Auther: pxt
 * @Date: 2021/5/30 16:31
 * @Description: com.faurecia.http
 * @Version: 1.0
 */
object RetrofitUtil {
    val api: Api by lazy {
        get("http://192.168.1.106:8890", Api::class.java, 5000, false)
//        get("http://norskytech:8890", Api::class.java, 5000, true)
    }
    val apiLoading: Api by lazy {
        get("http://192.168.1.106:8890", Api::class.java, 5000, true)
//        get("http://norskytech:8890", Api::class.java, 5000, true)
    }

    fun <T> get(url1: String?, t: Class<T>?, timeout: Int, showLoading: Boolean): T {
        var url = url1
//        val sslSocketFactory: HttpsUtils.SSLParams = HttpsUtils.getSslSocketFactory()
        val builder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor {
//            if (BuildConfig.DEBUG) {
                Log.e("Network", it)
//            }
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        //val client = builder.sslSocketFactory(sslSocketFactory.sSLSocketFactory, sslSocketFactory.trustManager)
        val client = builder.connectTimeout(timeout.toLong(), TimeUnit.SECONDS)
            .writeTimeout(timeout.toLong(), TimeUnit.SECONDS)
            .readTimeout(timeout.toLong(), TimeUnit.SECONDS)
            .addNetworkInterceptor(loggingInterceptor)
            .addInterceptor(Interceptor { chain ->
                try {
                    if (showLoading) {
                        showLoading()
                    }
                    val newBuilder: Request.Builder = chain.request().newBuilder()
                    val request: Request = newBuilder.build()
                    val proceed = chain.proceed(request)
                    return@Interceptor proceed
                } catch (e: Exception) {
                    throw IOException(e)
                } finally {
                    if (showLoading) {
                        dismissLoading()
                    }
                }
            })
            .build()

        val rxAdapter = RxJava3CallAdapterFactory.create()
        if (!URLUtil.isValidUrl(url)) {
            url = URLDecoder.decode(String(Base64.decode(url, Base64.DEFAULT), Charset.defaultCharset()))
        }
        return Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addCallAdapterFactory(rxAdapter)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(t) as T
    }

    fun showLoading() {
        ProgressDialog.show()
    }
    fun dismissLoading(){
        ProgressDialog.dismiss()
    }
}