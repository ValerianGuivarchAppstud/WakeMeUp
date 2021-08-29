package com.vguivarc.wakemeup.data.repository

import android.os.Bundle
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.vguivarc.wakemeup.data.api.ContactApi
import com.vguivarc.wakemeup.data.api.FacebookApi
import com.vguivarc.wakemeup.data.entity.ContactRequest
import com.vguivarc.wakemeup.data.entity.FavoriteRequest
import com.vguivarc.wakemeup.data.entity.ShareRingingRequest
import com.vguivarc.wakemeup.domain.entity.Contact
import com.vguivarc.wakemeup.domain.entity.Favorite
import com.vguivarc.wakemeup.domain.entity.Ringing
import com.vguivarc.wakemeup.domain.service.ContactFacebookService
import com.vguivarc.wakemeup.domain.service.ContactService
import com.vguivarc.wakemeup.ui.contactlistfacebook.ContactFacebook
import io.reactivex.Single
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import timber.log.Timber

class ContactFacebookRepository(retrofit: Retrofit) : ContactFacebookService {

    private val contactApi = retrofit.create(FacebookApi::class.java)


    override fun getContactFacebookList(facebookId: String): Single<List<ContactFacebook>> {
        return contactApi.getContacts().map {
            null
//            it.contacts
        }
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
