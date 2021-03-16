package com.udacity.asteroidradar.api

import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.util.Constants
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.reflect.Type


@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention()
internal annotation class Scalars{}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention()
internal annotation class Moshis{}

object CreateRetrofit {

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(ScalarsOrMoshiConverterFactory.create())
        .client(createHttpClient())
        .build()

    val retrofitService = retrofit.create(AsteroidRetrofitService::class.java)
}

fun createHttpClient(): OkHttpClient{
    val builder = OkHttpClient.Builder()
    if(BuildConfig.DEBUG){
        builder.addInterceptor(OkHttpProfilerInterceptor())
    }
    return builder.build()
}

class ScalarsOrMoshiConverterFactory: Converter.Factory(){
    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        annotations.forEach { annotation ->
            when (annotation) {
                is Scalars -> return ScalarsConverterFactory.create().responseBodyConverter(type, annotations, retrofit)
                is Moshis -> {
                     val moshi = Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()
                    return MoshiConverterFactory.create(moshi)
                        .responseBodyConverter(type, annotations, retrofit)
                }
            }
        }
        return null
    }

    companion object {
        fun create() = ScalarsOrMoshiConverterFactory()
    }

}

interface AsteroidRetrofitService {

    @Scalars
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate:String,
        @Query("api_key") apiKey: String
    ): String

    @Moshis
    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(
        @Query("api_key") apiKey: String
    ): PictureOfDay

}

