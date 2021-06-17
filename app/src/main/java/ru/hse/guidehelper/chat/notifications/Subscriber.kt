package ru.hse.guidehelper.chat.notifications

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import ru.hse.guidehelper.MainActivity
import ru.hse.guidehelper.api.RequestHelper
import ru.hse.guidehelper.chat.MessagesFragment

class Subscriber {
    fun subcribe() {

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            FirebaseService.token = it.token
            if(MainActivity.currentUser != null) {
                RequestHelper.updateToken(MainActivity.currentUser.getId(), it.token)
            }
        }

        FirebaseMessaging.getInstance().subscribeToTopic(MessagesFragment.TOPIC)
    }
}