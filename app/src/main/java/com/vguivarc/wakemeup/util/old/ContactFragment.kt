package com.vguivarc.wakemeup.util.old

/*
class ContactFragment : BaseLceFragment() {

    private val authViewModel: AuthViewModel by sharedViewModel()
    private lateinit var favoriButton: Button

    private var currentUser: UserProfile? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       /* val contact = ContactFragmentArgs.fromBundle(requireArguments()).contact
        val view =  inflater.inflate(R.layout.fragment_contact, container, false)

        view.findViewById<TextView>(R.id.id_contact_nom).text = contact.username
        if(contact.imageUrl!="") {
            Glide.with(requireContext())
                .load(contact.imageUrl)
                .into(view.findViewById(R.id.profil_picture))
        } else {
            view.findViewById<ImageView>(R.id.profil_picture).setImageDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.empty_picture_profil)
            )
        }

        val factory = ViewModelFactory(AndroidApplication.repository)
        viewModelContact = ViewModelProvider(this, factory).get(ContactListeViewModel::class.java)
        viewModelSonnerie = ViewModelProvider(this, factory).get(SonnerieListeViewModel::class.java)
        currentUserViewModel =
            ViewModelProvider(this, factory).get(CurrentUserViewModel::class.java)
        favoriButton = view.findViewById(R.id.id_contact_partage_favori)

        currentUserViewModel.getCurrentUserLiveData().observe(requireActivity(), {
            currentUser=it
            if (currentUser==null) {
                favoriButton.visibility= View.GONE
            }
        })

        view.findViewById<Button>(R.id.id_contact_partage_favori).setOnClickListener{
           /* val action =
                ContactFragmentDirections.actionContactFragmentToFavorisShareFragment(contact)
            findNavController().navigate(action)*/
        }


        view.findViewById<Button>(R.id.id_contact_saisie_musique).setOnClickListener{
            val layout: View = inflater.inflate(
                R.layout.dialog_saisie_youtube_manuelle,
                container, false
            )
            if (currentUser==null) {
                layout.findViewById<TextView>(R.id.id_dialog_saisie_manuel_text2).visibility=
                    View.VISIBLE
                layout.findViewById<EditText>(R.id.id_dialog_saisie_manuel_pseudo).visibility=
                    View.VISIBLE
            }
            val urlYT =
                layout.findViewById<View>(R.id.id_dialog_saisie_manuel_url) as EditText
            val builder = AlertDialog.Builder(requireContext())
            builder.setView(layout)
            builder.setPositiveButton(
                "Envoyer"
            ) { dialog, _ ->
                dialog.dismiss()
                if (currentUser==null) {
                    viewModelSonnerie.addSonnerieUrlToUser(requireActivity(), urlYT.text.toString(), contact,
                        layout.findViewById<EditText>(R.id.id_dialog_saisie_manuel_pseudo).text.toString())
                } else {
                    viewModelSonnerie.addSonnerieUrlToUser(requireActivity(), urlYT.text.toString(), contact, null)
                }
            }
            builder.setNegativeButton(
                "Annuler"
            ) { dialog, _ -> dialog.dismiss() }
            val dialog = builder.create()
            dialog.show()
        }

        viewModelSonnerie.getSonnerieStateAddResult().observe(requireActivity(), {
            if(it.error==null){
                Utility.createSimpleToast("Sonnerie envoyée")
                requireActivity().onBackPressed()
            } else {
                Utility.createSimpleToast("Erreur dans l'envoie de la sonnerie")
                Timber.e(it.error.toString())
            }
        })

*/
        return view
    }
}
*/