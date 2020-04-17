package com.wakemeup.reveil

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neocampus.repo.ViewModelFactory
import com.wakemeup.AppWakeUp
import com.wakemeup.R
import com.wakemeup.util.putParcelableExtra
import kotlinx.android.synthetic.main.fragment_reveil_list.view.*

class ReveilsListeFragment : Fragment(), ReveilListeAdapter.ReveilListAdapterListener {


    companion object {
        fun newInstance(): ReveilsListeFragment {
            return ReveilsListeFragment()
        }
    }

    private lateinit var viewModel: ReveilListeViewModel
    private lateinit var adapter: ReveilListeAdapter
    private val reveils = mutableMapOf<Int, ReveilModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory(AppWakeUp.repository)

        viewModel = ViewModelProvider(this, factory).get(ReveilListeViewModel::class.java)
        viewModel.getReveilsListeLiveData().observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { nouvelleListeReveils ->
                updateReveilsListe(
                    nouvelleListeReveils
                )
            })
    }


    private fun updateReveilsListe(nouvelleListeReveils: Map<Int, ReveilModel>) {
        Log.e("Error", "updateReilsListe")
        reveils.clear()
        reveils.putAll(nouvelleListeReveils)
        adapter.notifyDataSetChanged()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode == Activity.RESULT_OK && data != null) {
            val newClock = data.extras!![ReveilModel.REVEIL] as ReveilModel
            newClock.toastNextClock(activity!!)
            when (requestCode) {
                ReveilModel.CREATE_REQUEST_CODE -> {
                    if (resultCode == Activity.RESULT_OK) {
                        viewModel.addReveil(newClock, newClock.idReveil)
                    }
                }
                ReveilModel.EDIT_REQUEST_CODE -> {
                    if (data.hasExtra(ReveilModel.DELETE) && data.getBooleanExtra(
                            ReveilModel.DELETE,
                            true
                        )
                    ) {
                        viewModel.removeReveil(data.extras!![ReveilModel.NUM_REVEIL] as Int)
                    } else {
                        viewModel.editReveil(newClock, data.extras!![ReveilModel.NUM_REVEIL] as Int)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_reveil_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.list_reveil)
        adapter = ReveilListeAdapter(reveils, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        view.bouton_add_reveil.setOnClickListener {
            val intent = Intent(activity, ReveilEdit::class.java)
            intent.putExtra("requestCode", ReveilModel.CREATE_REQUEST_CODE)
            startActivityForResult(
                intent,
                ReveilModel.CREATE_REQUEST_CODE
            )
        }

        return view
    }


    override fun onReveilClicked(reveilModel: ReveilModel, itemView: View) {

        val intent = Intent(activity, ReveilEdit::class.java)
        intent.putExtra(ReveilModel.NUM_REVEIL, reveilModel.idReveil)
        intent.putParcelableExtra(
            ReveilModel.REVEIL, reveilModel
        )
        intent.putExtra("requestCode", ReveilModel.EDIT_REQUEST_CODE)
        startActivityForResult(
            intent,
            ReveilModel.EDIT_REQUEST_CODE
        )
    }

    override fun onReveilSwitched(reveilModel: ReveilModel) {
        viewModel.switchReveil(reveilModel.idReveil)
    }

    override fun onReveilDelete(reveilModel: ReveilModel) {
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Supprimer réveil")
        builder.setMessage("Voulez-vous supprimer ce réveil ?")
        builder.setPositiveButton("Supprimer") { dialog, which ->
            viewModel.removeReveil(reveilModel.idReveil)
        }
        builder.setNeutralButton("Annuler") { _, _ -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}