package com.example.livechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        val TAG = "Chat"
    }

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)


        val user = intent.getParcelableExtra<User>(NewMessage.USER_KEY)
        supportActionBar?.title = user.username

        dummyData()
        //listenForMsg()

        btn_chat.setOnClickListener {
            performSendMessage()
        }
    }


    fun listenForMsg(){
        val ref = FirebaseDatabase.getInstance().getReference("/messages")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)
                if(chatMessage != null){
                    adapter.add(ChatfromItem(chatMessage.text))
                }

            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }
    fun performSendMessage() {
        val text = edittxt_chat.text.toString()

        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessage.USER_KEY)
        val toId = user.uid

        if(fromId == null) return
        val ref = FirebaseDatabase.getInstance().getReference("/message").push()
        val chatMessage = ChatMessage(ref.key!!, text, fromId, toId,
            System.currentTimeMillis() / 1000)
        ref.setValue(chatMessage)

    }

    fun dummyData() {
        val adapter = GroupAdapter<ViewHolder>()
        adapter.add(ChatfromItem("asd"))
        adapter.add(ChattoItem("asdasd"))


        recycleview_chat.adapter = adapter
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