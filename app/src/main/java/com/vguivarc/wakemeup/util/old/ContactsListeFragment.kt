package com.vguivarc.wakemeup.util.old

/*
class ContactsListeFragment :
    BaseLceFragment(R.layout.fragment_contact_list),
    ContactListeAdapter.ContactListAdapterListener {

    private lateinit var viewModelSonnerie: SonnerieListeViewModel
    private lateinit var adapter: ContactListeAdapter
    private lateinit var loading: ProgressBar
    private lateinit var textePasDeContact: TextView
    private val contactsList = mutableMapOf<String, UserProfile>()
    private val sonneriesEnvoyeesList = mutableListOf<Ringing>()
    private val sonneriesAttenteList = mutableListOf<Ringing>()
    private val sonneriesPasseList = mutableListOf<Ringing>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // put all Phonecontacts in viewModel
/*
         for (user in getPhoneContacts().values){
                     viewModel.addContact(user)
         }*/

        viewModelSonnerie.getListeAttenteLiveData().observe(
            viewLifecycleOwner,
            {
                sonneriesAttenteList.clear()
                sonneriesAttenteList.addAll(it.values)
                adapter.notifyDataSetChanged()
            }
        )
        viewModelSonnerie.getListePasseesLiveData().observe(
            viewLifecycleOwner,
            {
                sonneriesPasseList.clear()
                sonneriesPasseList.addAll(it.values)
                adapter.notifyDataSetChanged()
            }
        )

        textePasDeContact = view.findViewById(R.id.textPasContact)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_list_contact)
        adapter = ContactListeAdapter(requireContext(), contactsList, sonneriesEnvoyeesList, sonneriesAttenteList, sonneriesPasseList, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onContactClicked(contact: UserProfile, itemView: View) {
       /* val action =
            ContactsListeFragmentDirections.actionContactsListeFragmentToContactFragment(contact)
        findNavController().navigate(action)*/
    }

    // menu recherche
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
}*/
