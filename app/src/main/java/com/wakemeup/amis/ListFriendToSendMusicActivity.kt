package com.wakemeup.amis

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.wakemeup.AppWakeUp
import com.wakemeup.R
import com.wakemeup.notification.*
import com.wakemeup.song.Song
import kotlinx.android.synthetic.main.activity_liste_ami.*
import kotlinx.android.synthetic.main.item_list_video.view.*
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback


class ListFriendToSendMusicActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(view: View) {
        if (view.tag != null) {
            val index = view.tag as Int

            if (listeSelection.contains(index)) {
                listeSelection.remove(index)
                Log.e("remove", index.toString())
            } else {
                listeSelection.add(index)
                Log.e("add", index.toString())
            }
            recycler_list_ami.adapter?.notifyDataSetChanged()
        }

    }

    private val listeSelection: MutableList<Int> = mutableListOf()

    lateinit var apiService: APIService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liste_ami)
        val currentSong = intent.getParcelableExtra<Song>("song")

        header_include_liste_ami.tv_title.text = currentSong!!.title

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService::class.java)


        recycler_list_ami.layoutManager = LinearLayoutManager(this)
        recycler_list_ami.adapter = AmiAdapter(this, AppWakeUp.listeAmis, listeSelection, this)

        list_partage_ami_annuler.setOnClickListener {
            finish()
        }

        list_partage_ami_valider.setOnClickListener {
            Log.e("listeSelection ", listeSelection.toString())
            for (index in listeSelection) {
                Log.e("index", index.toString())


                val reveilMusic = SentMusic(
                    AppWakeUp.auth.currentUser!!.uid,
                    AppWakeUp.listeAmis[index].id,
                    currentSong.id
                )
                val pushReveil = AppWakeUp.database.reference.child("Reveils").push()
                pushReveil.setValue(reveilMusic).addOnSuccessListener {
                    AppWakeUp.database.reference.child("Users")
                        .child(AppWakeUp.auth.currentUser!!.uid).child("sentMusic").push()
                        .setValue(pushReveil.key)
                    AppWakeUp.database.reference.child("Users").child(AppWakeUp.listeAmis[index].id)
                        .child("receivedMusic").push().setValue(pushReveil.key)
                }

                Log.e("TESSSS", "A")
                FirebaseInstanceId.getInstance().instanceId
                    .addOnSuccessListener { instanceIdResult ->
                        Log.e("TESSSS", "B")
                        val deviceToken = instanceIdResult.token
                        Log.e("TESSSS", "C")
                        updateToken(deviceToken, AppWakeUp.listeAmis[index].id)
                        Log.e("TESSSS", "D")
                    }
                    .addOnFailureListener { instanceIdResult ->
                        Log.e("J", "FAIL")
                    }
                Log.e("TESSSS", "E")
                AppWakeUp.database.getReference("Users").child(AppWakeUp.listeAmis[index].id)
                    .addValueEventListener(
                        object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                Log.e("TESSSS", "F")
                                sendNotification(
                                    AppWakeUp.listeAmis[index].id,
                                    AppWakeUp.auth.currentUser!!.uid,
                                    currentSong.id
                                )
                                Log.e("TESSSS", "G")
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Log.e("TESSSS", "H")
                            }
                        }
                    )
                Log.e("TESSSS", "I")
                Toast.makeText(this, "Reveil envoyé !", Toast.LENGTH_SHORT).show()
                finish()

            }
        }
    }


    private fun sendNotification(receiver: String, username: String, song: String) {
        AppWakeUp.database.getReference("Tokens").orderByKey().equalTo(receiver)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (snapshot in dataSnapshot.children) {
                            val token = snapshot.getValue((Token::class.java))!!
                            val data = Data(
                                receiver,
                                R.mipmap.ic_launcher,
                                "$username : vous a envoyé une nouvelle chanson pour vous réveiller!",
                                "Nouveau réveil",
                                AppWakeUp.auth.currentUser!!.uid
                            )
                            val sender = Sender(data, token.token)

                            Log.e("ListFriendToSend", "SEND")
                            apiService.sendNotifications(sender)!!.enqueue(object : Callback,
                                retrofit2.Callback<MyResponse?> {
                                override fun onFailure(call: Call<MyResponse?>, t: Throwable) {
                                    Log.e("ListFriendToSend", "FAIL")
                                }

                                override fun onResponse(
                                    call: Call<MyResponse?>,
                                    response: Response<MyResponse?>
                                ) {
                                    Log.e("ListFriendToSend", "GOOD")
                                }

                            })
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                }
            )


    }

    private fun updateToken(token: String, fuser: String) {
        val token1 = Token(token)
        AppWakeUp.database.getReference("Tokens").child(fuser).setValue(token1)
    }
}

