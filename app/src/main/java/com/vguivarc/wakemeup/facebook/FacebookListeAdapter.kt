package com.vguivarc.wakemeup.facebook


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.connect.UserModel
import com.vguivarc.wakemeup.contact.Contact

class FacebookListeAdapter(
    private val context: Context,
    private var contacts: Map<String, Contact>,
    private var friends: Map<String, UserModel>,
    private val listener: FacebookListAdapterListener?
) : RecyclerView.Adapter<FacebookListeAdapter.ViewHolder>(),
    View.OnClickListener{

    interface FacebookListAdapterListener {
        fun onFriendClicked(friend: UserModel, itemView: View)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card_view_contact = itemView.findViewById<CardView>(R.id.card_view_contact)!!
        var item_friend_image = itemView.findViewById<ImageView>(R.id.item_contact_fb_image)!!
        var item_friend_nom = itemView.findViewById<TextView>(R.id.item_contact_fb_nom)!!
        var bouton_ajout = itemView.findViewById<TextView>(R.id.id_button_ajout_contact_fb)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_contact, parent, false)
        return ViewHolder(viewItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = friends.toList()[position].second
        with(holder) {
            card_view_contact.tag = friend
            if(contacts.containsKey(friend.id)){
                bouton_ajout.setBackgroundResource(R.drawable.ic_check_30)
            } else {
                bouton_ajout.setBackgroundResource(R.drawable.ic_add_30)
                bouton_ajout.setOnClickListener(this@FacebookListeAdapter)
            }
            //item_contact_image.setImageDrawable((, null)))
            item_friend_nom.text = friend.username

            if(friend.imageUrl!="") {
                Glide.with(context)
                    .load(friend.imageUrl)
                    .into(item_friend_image)
            } else {
                item_friend_image.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.empty_picture_profil))
            }
        }
    }

    override fun getItemCount(): Int = contacts.size

    override fun onClick(v: View) {
        listener?.onFriendClicked(v.tag as UserModel, v)
    }
}