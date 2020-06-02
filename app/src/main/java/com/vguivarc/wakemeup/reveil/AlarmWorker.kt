package com.vguivarc.wakemeup.reveil

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters


class AlarmWorker(val context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val idReveil = inputData.getString("idReveil")!!.toInt()
        /* val i = Intent(context, ReveilSonneService::class.java)
         i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
         i.putExtra("idReveil", idReveil)
         context.startActivity(i)*/

        val i = Intent(context, ReveilSonneActivity::class.java)
        i.putExtra("idReveil", idReveil)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)

        return Result.success()
    }

}
