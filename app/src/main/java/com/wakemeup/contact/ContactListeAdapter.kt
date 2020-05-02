package com.wakemeup.contact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.wakemeup.R
import com.wakemeup.connect.UserModel

class ContactListeAdapter(
    private var contacts: Map<String, UserModel>,
    private val listener: ContactListAdapterListener?
) : RecyclerView.Adapter<ContactListeAdapter.ViewHolder>(),
    View.OnClickListener, Filterable{

    // variable filtrée avec la recherche dans contacts
    var allUsersRecherche = mutableListOf<UserModel>()
    var contactsInitiaux = mutableMapOf<String, UserModel>()

    init {
        allUsersRecherche.addAll(
            contacts.values
        )
        contactsInitiaux.putAll(contacts)
    }

    interface ContactListAdapterListener {
        fun onContactClicked(userModel: UserModel, itemView: View)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card_view_contact = itemView.findViewById<CardView>(R.id.card_view_contact)!!
        var item_contact_image = itemView.findViewById<ImageView>(R.id.item_contact_image)!!
        var item_contact_nom = itemView.findViewById<TextView>(R.id.item_contact_nom)!!
        var item_contact_selection = itemView.findViewById<CheckBox>(R.id.item_contact_selection)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_contact, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contacts.toList()[position].second
        with(holder) {
            card_view_contact.tag = contact
            card_view_contact.setOnClickListener(this@ContactListeAdapter)
            //item_contact_image.setImageDrawable((, null)))
            item_contact_nom.text = contact.username
            item_contact_selection.isChecked = false
        }
    }

    override fun getItemCount(): Int = contacts.size

    override fun onClick(v: View) {
        listener?.onContactClicked(v.tag as UserModel, v)
    }

    override fun getFilter(): Filter {
       return userFilter
    }

    private var userFilter : Filter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            var filteredList: ArrayList<UserModel> = mutableListOf<UserModel>() as ArrayList<UserModel>
            if(charSequence.toString().isEmpty()){
                filteredList.addAll(allUsersRecherche)
            }else{
                for(userModel in allUsersRecherche){
                    if(userModel.username.toLowerCase().contains(charSequence.toString().toLowerCase().trim())){
                        filteredList.add(userModel)
                    }
                }
            }

            var filterResults : FilterResults = FilterResults()
            filterResults.values = filteredList
            return  filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            contactsInitiaux.clear()
            for(user in results?.values as ArrayList<UserModel>){
                contactsInitiaux.put(user.id, user)
            }
            contacts = contactsInitiaux
            notifyDataSetChanged()
        }

    }
}