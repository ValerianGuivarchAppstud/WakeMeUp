package com.vguivarc.wakemeup.facebook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vguivarc.wakemeup.AppWakeUp
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.connect.UserModel
import com.vguivarc.wakemeup.contact.Contact
import com.vguivarc.wakemeup.contact.ContactListeViewModel
import com.vguivarc.wakemeup.repo.ViewModelFactory

class FacebookListeFragment : Fragment(), FacebookListeAdapter.FacebookListAdapterListener {

    //TODO item musique cherché enlever durée

    private lateinit var viewModelContact: ContactListeViewModel
    private lateinit var viewModelFriends: FacebookListeViewModel
    private lateinit var adapter: FacebookListeAdapter
    private lateinit var loading : ProgressBar
    private lateinit var textePasDAmis : TextView
    private val contactsList = mutableMapOf<String, Contact>()
    private val friendsList = mutableMapOf<String, UserModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val factory = ViewModelFactory(AppWakeUp.repository)
        viewModelContact = ViewModelProvider(this, factory).get(ContactListeViewModel::class.java)
        viewModelFriends= ViewModelProvider(this, factory).get(FacebookListeViewModel::class.java)

        viewModelFriends.getFriendsListeLiveData().observe(
            viewLifecycleOwner,
            Observer { it ->
                if(it.error==null) {
                    if(it.friendList.size==0){
                        friendsList.clear()
                        adapter.notifyDataSetChanged()
                        textePasDAmis.visibility = View.VISIBLE
                    } else {
                        friendsList.clear()
                        friendsList.putAll(it.friendList)
                        adapter.notifyDataSetChanged()
                        textePasDAmis.visibility = View.GONE
                    }
                }
                loading.visibility = View.GONE
            })

        viewModelContact.getContactsListeLiveData().observe(
            viewLifecycleOwner,
            Observer { it ->
                if(it.error==null) {
                    if(it.contactList.size==0){
                        contactsList.clear()
                        adapter.notifyDataSetChanged()
                    } else {
                        contactsList.clear()
                        contactsList.putAll(it.contactList)
                        adapter.notifyDataSetChanged()
                    }
                }
            })

        viewModelContact.getContacts()
        viewModelFriends.getFriends()

        val view = inflater.inflate(R.layout.fragment_friend_list, container, false)
        textePasDAmis = view.findViewById(R.id.textPasAmi)
        loading = view.findViewById(R.id.pb_main_loader)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_list_ami)
        adapter = FacebookListeAdapter(requireContext(), contactsList, friendsList, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        return view
    }

    override fun onFriendClicked(friend: UserModel, itemView: View) {
        viewModelContact.addContact(friend)
    }

    //menu recherche
    /*
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
    }*/

/*    override fun onContactClicked(userModel: UserModel, itemView: View) {
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

        //listeDansAppli.addAll(getContactAmiInAppli()) //recuperer les amis de l'application

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
        AppWakeUp.repository.database.getReference("Users").addValueEventListener(
            object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) { }

                override fun onDataChange(p0: DataSnapshot) {
                    for (snaphot : DataSnapshot in p0.children){
                        val ret2:ValueEventListener = AppWakeUp.repository.database.getReference("Users").child(snaphot.key as String)
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
*/
}
