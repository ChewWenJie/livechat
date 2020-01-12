package com.example.livechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.livechat.models.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.example.livechat.models.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class chat : AppCompatActivity() {

    companion object{
        val TAG = "ChatLog"
    }

    val adapter = GroupAdapter<ViewHolder>()

    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        toUser = intent.getParcelableExtra<User>(NewMessage.USER_KEY)

        supportActionBar?.title = toUser?.username

        recyclerview_chat_log.adapter = adapter
        listenForMsg()

        btn_chat.setOnClickListener {
            performSendMessage()
        }
    }


    fun listenForMsg(){
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)

                if (chatMessage != null) {
                    Log.d(TAG, chatMessage.text)

                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        val currentUser = latest_activity.currentUser ?: return
                        adapter.add(ChattoItem(chatMessage.text, currentUser))
                    } else {
                        adapter.add(ChatfromItem(chatMessage.text, toUser!!))
                    }
              }
               recyclerview_chat_log.scrollToPosition(adapter.itemCount-1)

            }
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }


        })
    }
    private fun performSendMessage() {
        // how do we actually send a message to firebase...
        val text = edittxt_chat.text.trim().toString()

        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessage.USER_KEY)
        val toId = user.uid

        if (fromId == null) return
//    val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = ChatMessage(reference.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)
        if (text.isNotEmpty()) {
            reference.setValue(chatMessage)
                .addOnSuccessListener {
                    Log.d(TAG, "Saved our chat message: ${reference.key}")
                    edittxt_chat.text.clear()

                    //position
                    recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)


                    toReference.setValue(chatMessage)

                    val latestMessageRef =
                        FirebaseDatabase.getInstance()
                            .getReference("/latest-messages/$fromId/$toId")
                    chatMessage.toString().isNotEmpty().apply {
                        latestMessageRef.setValue(chatMessage)
                    }
                    val latestMessageToRef =
                        FirebaseDatabase.getInstance()
                            .getReference("/latest-messages/$toId/$fromId")


                    latestMessageToRef.setValue(chatMessage)
//                    Toast.makeText(baseContext, chatMessage.toString(), Toast.LENGTH_LONG).show()
                }
        }
    }
}



class ChatfromItem(val text:String, val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_from_row.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}
class ChattoItem(val text:String, val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_to_row.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}