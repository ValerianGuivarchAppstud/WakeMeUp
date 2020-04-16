package com.neocampus.repo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wakemeup.AppWakeUp
import com.wakemeup.connect.UserModel
import com.wakemeup.reveil.ReveilModel
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*

class Repository() {

    /*************************************************/
    /******************** REVEILS ********************/
    /*************************************************/

    private val reveils = sortedMapOf<Int, ReveilModel>()
    private val reveilsLiveData = MutableLiveData<Map<Int, ReveilModel>>()
    fun getReveils(): LiveData<Map<Int, ReveilModel>> = reveilsLiveData

    init {
        chargement()
    }

    fun addReveil(reveilModel: ReveilModel, index: Int) {
        reveils.put(index, reveilModel)
        reveilModel.startAlarm()
        enregistrementReveil()
        reveilsLiveData.value = reveils
    }

    fun editReveil(reveilModel: ReveilModel, index: Int) {
        reveilModel.cancelAlarm()
        removeReveil(index)
        reveilModel.startAlarm()
        addReveil(reveilModel, index)
    }

    fun removeReveil(index: Int) {
        reveils[index]!!.cancelAlarm()
        reveils.remove(index)
        enregistrementReveil()
        reveilsLiveData.value = reveils
    }

    private fun enregistrementReveil() {
        val fileOutput =
            AppWakeUp.appContext.openFileOutput(AppWakeUp.NAME_FILE_REVEIL, Context.MODE_PRIVATE)
        val outputStream = ObjectOutputStream(fileOutput)
        outputStream.writeObject(reveils)
    }

    private fun chargement() {
        try {
            val fileInput = AppWakeUp.appContext.openFileInput(AppWakeUp.NAME_FILE_REVEIL)
            val inputStream = ObjectInputStream(fileInput)
            val listReveil = inputStream.readObject() as SortedMap<Int, ReveilModel>
            reveils.putAll(listReveil)
            reveilsLiveData.value = reveils
            ReveilModel.chargement(reveils)
        } catch (e: FileNotFoundException) {

        }
    }

    fun switchReveil(idReveil: Int) {
        reveils[idReveil]!!.isActif = !reveils[idReveil]!!.isActif
        if (reveils[idReveil]!!.isActif)
            reveils[idReveil]!!.startAlarm()
        else
            reveils[idReveil]!!.cancelAlarm()
    }

    /**************************************************/
    /******************** CONTACTS ********************/
    /**************************************************/
    private val contacts = sortedMapOf<String, UserModel>()
    private val contactsLiveData = MutableLiveData<Map<String, UserModel>>()

    fun getContacts(): LiveData<Map<String, UserModel>> = contactsLiveData

    fun addContact(userModel: UserModel) {
        contacts.put(userModel.username, userModel)
        contactsLiveData.value = contacts
    }

}