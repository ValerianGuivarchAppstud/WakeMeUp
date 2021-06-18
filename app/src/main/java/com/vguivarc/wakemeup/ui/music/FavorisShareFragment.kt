package com.vguivarc.wakemeup.ui.music

/*
class FavorisShareFragment : Fragment(), FavorisShareAdaptater.RecyclerItemClickListener {

    private var favorisList: MutableMap<String, Favorite> = mutableMapOf()
    private lateinit var fAdapter: FavorisShareAdaptater

    //private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var currentView: View
    private var selections = mutableListOf<Favorite>()

    private lateinit var viewModelSonnerie: SonnerieListeViewModel


    private lateinit var textePasDeFavori: TextView
    private lateinit var loading: ProgressBar

    private lateinit var viewModelFavori: FavoriteViewModel
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        val factory = ViewModelFactory(AndroidApplication.repository)
        viewModelFavori = ViewModelProvider(this, factory).get(FavoriteViewModel::class.java)
        viewModelSonnerie = ViewModelProvider(this, factory).get(SonnerieListeViewModel::class.java)

        viewModelFavori.getFavoriteList()

        //Initialisation des vues-------------------------------------------------------------
        currentView = inflater.inflate(R.layout.fragment_favori_share, container, false)
        fAdapter = FavorisShareAdaptater(favorisList, selections, this)

        textePasDeFavori = currentView.findViewById(R.id.textPasFavori)
        loading = currentView.findViewById(R.id.pb_main_loader)

        viewModelFavori.favoriteList
            .observe(requireActivity(), {
                if (it.error == null) {
                    if (it.favoriList.isEmpty()) {
                        favorisList.clear()
                        fAdapter.notifyDataSetChanged()
                        textePasDeFavori.visibility = View.VISIBLE
                    } else {
                        favorisList.clear()
                        favorisList.putAll(it.favoriList)
                        fAdapter.notifyDataSetChanged()
                        textePasDeFavori.visibility = View.GONE
                    }
                }
                loading.visibility = View.GONE
            })

        recyclerView = currentView.findViewById(R.id.recycler_list_video)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = fAdapter

      /*  val contact = com.vguivarc.wakemeup.song.favori.FavorisShareFragmentArgs.fromBundle(
            requireArguments()
        ).contact

        currentView.findViewById<Button>(R.id.valid_share_favori_button).setOnClickListener {
            for (f in selections) {
                viewModelSonnerie.addSonnerieToUser(f.song!!, contact)
            }
            if (selections.size > 0) {
                Utility.createSimpleToast("Sonneries envoyées !")
                requireActivity().onBackPressed()
            } else {
                Utility.createSimpleToast("Veuillez sélectionner les musiques à envoyer à votre contact")
            }
        }

        currentView.findViewById<TextView>(R.id.share_contact_name).text = contact.username
        if (contact.imageUrl != "") {
            Glide.with(requireContext())
                .load(contact.imageUrl)
                .into(currentView.findViewById(R.id.share_contact_image))
        } else {
            currentView.findViewById<ImageView>(R.id.share_contact_image).setImageDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.empty_picture_profil)
            )
        }
*/
        return currentView
    }

    override fun onSelect(recherche: Favorite, position: Int) {
        if (selections.contains(recherche)) {
            selections.remove(recherche)
        } else {
            selections.add(recherche)
        }
        fAdapter.notifyDataSetChanged()
    }


}


*/
