package com.wakemeup.notification

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    @Headers(
        "Content-Type:application/json",
        "Authoriation:AAAATQaqsx0:APA91bGHEJkTknDUFRy2bstprCDA1uHl1dpuNPVjVB3XSpXn1MrqKv8jo73JCRqIqgPlOLa-RmtvO6YC1pjETxLvZmrlfrXCYq2HIBpWpgWgx5T1hZ1mfUUtInmR88rE6Txi_-WkAJvI"
    )

    @POST("fcm/send")
    fun sendNotifications(@Body body: Sender): Call<MyResponse?>?
}