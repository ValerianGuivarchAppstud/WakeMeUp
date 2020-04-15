package com.wakemeup.alarmclock

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wakemeup.AppWakeUp
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*

class AlarmViewModel : ViewModel() {

    private val liveDataReveils = MutableLiveData<SortedMap<Int, Reveil>>()
    private val listeReveils = sortedMapOf<Int, Reveil>()

    init {
        chargement()
    }

    fun getReveils(): LiveData<SortedMap<Int, Reveil>> {
        return liveDataReveils
    }

    fun addReveil(reveil: Reveil, index: Int) {
        Log.e("EDIT", "edit -> " + index)
        listeReveils.put(index, reveil)
        enregistrementReveil()
        liveDataReveils.value = listeReveils
    }

    fun editReveil(reveil: Reveil, index: Int) {
        removeReveil(index)
        addReveil(reveil, index)
    }

    fun removeReveil(index: Int) {
        listeReveils.remove(index)
        enregistrementReveil()
        liveDataReveils.value = listeReveils
    }

    private fun enregistrementReveil() {
        val fileOutput =
            AppWakeUp.appContext.openFileOutput(AppWakeUp.NAME_FILE_REVEIL, Context.MODE_PRIVATE)
        val outputStream = ObjectOutputStream(fileOutput)
        outputStream.writeObject(listeReveils)
    }

    private fun chargement() {
        try {
            val fileInput = AppWakeUp.appContext.openFileInput(AppWakeUp.NAME_FILE_REVEIL)
            val inputStream = ObjectInputStream(fileInput)
            val v = inputStream.readObject() as SortedMap<Int, Reveil>
            listeReveils.putAll(v)
            liveDataReveils.value = listeReveils
            Reveil.chargement(listeReveils)
        } catch (e: FileNotFoundException) {

        }

    }
}