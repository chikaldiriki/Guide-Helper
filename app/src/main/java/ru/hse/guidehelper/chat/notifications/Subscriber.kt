package ru.hse.guidehelper.chat.notifications

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import ru.hse.guidehelper.chat.MessagesFragment

class Subscriber {
    fun subcribe() {

        //FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseService.token = it.token
            println("it.token = " + it.token)
        }

        FirebaseMessaging.getInstance().subscribeToTopic(MessagesFragment.TOPIC)
    }
}