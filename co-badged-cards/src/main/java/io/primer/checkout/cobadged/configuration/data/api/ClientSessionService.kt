package io.primer.checkout.cobadged.configuration.data.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.primer.checkout.cobadged.BuildConfig
import io.primer.checkout.cobadged.configuration.data.model.ClientSessionRequest
import io.primer.checkout.cobadged.configuration.data.model.ClientSessionResponse
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface ClientSessionService {

    @POST
    suspend fun createClientSession(
        @Url url: String,
        @Body request: ClientSessionRequest
    ): ClientSessionResponse

    companion object {

        const val BASE_URL = "http://10.0.2.2:8000"
        private const val DEFAULT_CONTENT_TYPE = "application/json"

        private val json = Json { ignoreUnknownKeys = true }

        fun create(): ClientSessionService {
            val logLevel = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
            val logger =
                HttpLoggingInterceptor().apply { level = logLevel }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            val contentType = DEFAULT_CONTENT_TYPE.toMediaType()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(json.asConverterFactory(contentType))
                .build()
                .create(ClientSessionService::class.java)
        }
    }
}
