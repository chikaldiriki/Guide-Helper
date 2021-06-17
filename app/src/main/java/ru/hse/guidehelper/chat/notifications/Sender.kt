package ru.hse.guidehelper.chat.notifications

import android.content.ContentValues.TAG
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.hse.guidehelper.chat.MessagesFragment

class Sender {

    companion object {
        @JvmStatic
        fun createAndSendNotification(title: String, message: String) {
            PushNotification(
                    NotificationData(title, message),
                    MessagesFragment.TOPIC
            ).also {
                sendNotification(it)
            }
        }


        @JvmStatic
        fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    //Log.d(TAG, "Response: ${Gson().toJson(response)}")
                    Log.d(TAG, "sucsess")
                    Log.d(TAG, (response.message() == "").toString())
                } else {
                    Log.e(TAG, response.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }
}
