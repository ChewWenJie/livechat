package com.example.livechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.livechat.models.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.letsbuildthatapp.kotlinmessenger.models.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_too_row.view.*
import java.sql.Timestamp

class chat : AppCompatActivity() {

    companion object{
        val TAG = "ChatLog"
    }

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerview_chat_log.adapter = adapter

        val user = intent.getParcelableExtra<User>(NewMessage.USER_KEY)

        supportActionBar?.title = user.username

        //dummyData()
        listenForMsg()

        btn_chat.setOnClickListener {
            performSendMessage()
        }
    }


    fun listenForMsg(){
        val ref = FirebaseDatabase.getInstance().getReference("/messages")
        recyclerview_chat_log.adapter = adapter

        ref.addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)

                if (chatMessage != null) {
                    Log.d(TAG, chatMessage.text)

                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        adapter.add(ChatfromItem(chatMessage.text))
                    } else {
                        adapter.add(ChattoItem(chatMessage.text))
                    }
              }

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
    fun performSendMessage() {
        // how do we actually send a message to firebase...
        val text = edittxt_chat.text.toString()

        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessage.USER_KEY)
        val toId = user.uid

        if (fromId == null) return

        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()

        val chatMessage = ChatMessage(reference.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message: ${reference.key}")
            }
    }

    fun dummyData() {
        val adapter = GroupAdapter<ViewHolder>()
        adapter.add(ChatfromItem("FROM MESSSSSSSSAAGE"))
        adapter.add(ChattoItem("TO MESSAGE\nTOMESSAGE"))
        adapter.add(ChatfromItem("FROM MESSSSSSSSAAGE"))
        adapter.add(ChattoItem("TO MESSAGE\nTOMESSAGE"))
        adapter.add(ChatfromItem("FROM MESSSSSSSSAAGE"))
        adapter.add(ChattoItem("TO MESSAGE\nTOMESSAGE"))

        recyclerview_chat_log.adapter = adapter
    }
}

class ChatfromItem(val text:String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_from_row.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}
class ChattoItem(val text:String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_too_row.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_too_row
    }
}