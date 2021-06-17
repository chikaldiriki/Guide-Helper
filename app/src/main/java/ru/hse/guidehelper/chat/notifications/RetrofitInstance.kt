package ru.hse.guidehelper.chat.notifications

import ru.hse.guidehelper.chat.notifications.Constants.Companion.GOOGLE_API_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                    .baseUrl(GOOGLE_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        val api by lazy {
            retrofit.create(NotificationAPI::class.java)
        }
    }
}
