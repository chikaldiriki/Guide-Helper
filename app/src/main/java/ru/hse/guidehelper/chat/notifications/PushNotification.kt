package ru.hse.guidehelper.chat.notifications

data class PushNotification(
        val data: NotificationData,
        val to: String
)
