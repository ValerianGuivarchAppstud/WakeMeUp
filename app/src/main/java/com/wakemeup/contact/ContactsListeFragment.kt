package com.wakemeup.contact

import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.core.Tag
import com.neocampus.repo.ViewModelFactory
import com.wakemeup.AppWakeUp
import com.wakemeup.R
import com.wakemeup.connect.UserModel
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ContactsListeFragment : Fragment(), ContactListeAdapter.ContactListAdapterListener {

    companion object {
        fun newInstance(): ContactsListeFragment {
            return ContactsListeFragment()
        }
    }

    private lateinit var viewModel: ContactListeViewModel
    private lateinit var adapter: ContactListeAdapter
    private val contacts = mutableMapOf<String, UserModel>()
    private var listeContactInApp = mutableListOf<String>()
    private var listeDansAppli= mutableListOf<String>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory(AppWakeUp.repository)

        viewModel = ViewModelProvider(this, factory).get(ContactListeViewModel::class.java)

         //put all Phonecontacts in viewModel

         for (user in getPhoneContacts().values){
                     viewModel.addContact(user)
         }

        viewModel.getContactsListeLiveData().observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { nouvelleListeContacts ->
                updateContactsListe(
                    nouvelleListeContacts
                )
            })
    }

    private fun updateContactsListe(nouvelleListeContacts: Map<String, UserModel>) {
        contacts.clear()
        contacts.putAll(nouvelleListeContacts)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_contact_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list_contact)
        adapter = ContactListeAdapter(contacts, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        return view
    }

    //menu recherche
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search_contacts, menu)
        val item = menu.findItem(R.id.rechercher_contacts)
        val searchview : androidx.appcompat.widget.SearchView = item.actionView as androidx.appcompat.widget.SearchView
        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
       // setHasOptionsMenu(true)
    }

    override fun onContactClicked(userModel: UserModel, itemView: View) {
        var mBuilder = android.app.AlertDialog.Builder(context)
        val myView = layoutInflater.inflate(R.layout.detail_contact_ami, null)
        val userName = myView.findViewById<TextView>(R.id.userName) as TextView
        val phone = myView.findViewById<TextView>(R.id.userContact) as TextView
        val mail = myView.findViewById<TextView>(R.id.userMail) as TextView
        val buttonDemanderMusique = myView.findViewById<Button>(R.id.ask_musique) as Button
        val buttonPartagerMusique = myView.findViewById<Button>(R.id.share_musique) as Button
        userName.text = userModel.username
        phone.text = userModel.phone
        mail.text = userModel.mail

        buttonDemanderMusique.setOnClickListener(){
            Toast.makeText(context, "${userModel.username}, une musique stp?", Toast.LENGTH_SHORT).show()
        }

        buttonPartagerMusique.setOnClickListener(){
            Toast.makeText(context, "${userModel.username}, prend cette musique", Toast.LENGTH_SHORT).show()
        }

        mBuilder.setView(myView)
        val info = mBuilder.create()
        info.show()
    }


    // my own function
    fun getPhoneContacts(): Map<String, UserModel> {
        var contact = mutableMapOf<String, UserModel>()
        var userModel : UserModel? = null
        var cursor: Cursor? = requireContext().contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, null)
        var from = arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone._ID)
        cursor!!.moveToFirst()

        listeDansAppli.addAll(getContactAmiInAppli()) //recuperer les amis de l'application

        while (cursor.isAfterLast == false) {
            val contactNumber =
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val contactName =
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneContactID =
                cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID))
            userModel = UserModel(""+phoneContactID,"no image",contactNumber, contactName, "nomail@gamil.com")
            if (userModel != null) {
                Log.i("ContactListFragmentAmis", "Nom : ${userModel.username}, Phone : ${userModel.phone}, ID : ${userModel.id}" )
                if(listeDansAppli.contains("${contactNumber}"))// condition pour selectionner
                contact.put("${userModel.id}", userModel)
            }
            userModel = null
            cursor.moveToNext()
        }
        cursor.close()
        cursor = null
        Log.d("END", "Got all Contacts")
        return contact
    }

    //récupérer les contacts de l'appli
    fun getContactAmiInAppli() : List<String>{
        AppWakeUp.database.getReference("Users").addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) { }

                override fun onDataChange(p0: DataSnapshot) {
                    for (snaphot : DataSnapshot in p0.children){
                        val ret2:ValueEventListener = AppWakeUp.database.getReference("Users").child(snaphot.key as String)
                            .addValueEventListener(
                                object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) { }

                                    override fun onDataChange(p0: DataSnapshot) {
                                        for(snaphot: DataSnapshot in p0.children){
                                            if(snaphot.key == "phone"){
                                                Log.i("Snap", snaphot.getValue() as String)
                                                listeContactInApp.add(snaphot.getValue() as String)
                                            }
                                        }
                                    }

                                }
                            )
                    }
                }

            }
        )
        return listeContactInApp
    }
}