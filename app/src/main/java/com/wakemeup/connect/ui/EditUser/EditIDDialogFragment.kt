package com.wakemeup.connect.ui.EditUser

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.wakemeup.R

class EditIDDialogFragment : DialogFragment() {

    interface EditIDListener{
        fun onDialogPositiveClick(newId : String)
        fun onNegativeClick()
    }
    var listener : EditIDListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
        val input = EditText(context)
        with(input){
            inputType = InputType.TYPE_CLASS_TEXT
        }
        builder.setTitle(getString(R.string.nouvel_id_dialog))
            .setView(input)
            .setPositiveButton("Valider", DialogInterface.OnClickListener{
                _,_ -> listener?.onDialogPositiveClick(input.text.toString())

            })
            .setNegativeButton("Annuler", DialogInterface.OnClickListener
            {
                _,_ -> listener?.onNegativeClick()
            })
        return builder.create()
    }
}