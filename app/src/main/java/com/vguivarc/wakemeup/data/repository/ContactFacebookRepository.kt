package com.vguivarc.wakemeup.data.repository

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.vguivarc.wakemeup.data.api.ContactApi
import retrofit2.Retrofit
/*

class ContactFacebookRepository(retrofit: Retrofit) : ContactFacebookService {

    private val _contactFacebookList = MutableLiveData<ContactFacebookService.ContactFacebookListState>()

    private val contactApi = retrofit.create(ContactApi::class.java)

    override fun getContactFacebookList(facebookId: String): LiveData<ContactFacebookService.ContactFacebookListState> {
        val request = GraphRequest.newMeRequest(
            AccessToken.getCurrentAccessToken()
        ) { _, _ ->
            GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/$facebookId/friends",
                null,
                HttpMethod.GET
            ) { response2 ->
                try {
                    val rawName = response2.jsonObject.getJSONArray("data")
                    val listIdFacebook = mutableListOf<String>()
                    for (i in 0 until rawName.length()) {
                        val jsonFacebook =  rawName.getJSONObject(i)
                        listIdFacebook.add(jsonFacebook.getString("id"))
                    }
                    _contactFacebookList.value = ContactFacebookService.ContactFacebookListState(list = contactApi.getContactFacebookList(listIdFacebook).blockingGet())
                    } catch (e: Exception) {
                    e.printStackTrace()
                    _contactFacebookList.value = ContactFacebookService.ContactFacebookListState(error = e)
                }
            }.executeAsync()
        }

        val parameters = Bundle()
        parameters.putString("fields", "id,name,link,email,picture")
        request.parameters = parameters
        request.executeAsync()

        return _contactFacebookList
    }
     /*   val request = GraphRequest.newMeRequest(
            AccessToken.getCurrentAccessToken()
        ) { _, _ ->
            GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "$facebookId/friends",
                null,
                HttpMethod.GET
            ) { response2 ->
                try {
                    val rawName = response2.jsonObject.getJSONArray("data")
                    val listIdFacebook = mutableMapOf<String, JSONObject>()
                    for (i in 0 until rawName.length()) {
                        listIdFacebook[rawName.getJSONObject(i).getString("id")] = rawName.getJSONObject(
                            i
                        )
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }.executeAsync()

        }
        val parameters = Bundle()
        parameters.putString("fields", "id,name,picture")
        request.parameters = parameters
        request.executeAsync()
//        return contactApi.shareRinging(shareRingingRequest)
        return ServerResponse.ok().bodyAndAwait(eob);*/

}
*/