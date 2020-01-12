package com.example.livechat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.livechat.models.ChatMessage
import com.example.livechat.models.User
import com.example.livechat.view.LatestMessageRow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_activity.*
import kotlinx.android.synthetic.main.latest_message_row.view.*

class latest_activity : AppCompatActivity() {

    companion object {
        var currentUser: User? = null
    }
    val adapter = GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_activity)

        recyclerview_latest_messages.adapter = adapter
        //add a vertical line only
        recyclerview_latest_messages.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this, chat::class.java)

            val row  = item as LatestMessageRow

            intent.putExtra(NewMessage.USER_KEY, row.chatPartnerUser)
            startActivity(intent)
        }

        listenlatest()
        fetchCurrentUser()
        verifyUserLogged()
    }

    val latestMap = HashMap<String, ChatMessage>()
    fun refreshView(){
        adapter.clear()
        latestMap.values.forEach {
            adapter.add(LatestMessageRow(it))
        }
    }

    fun listenlatest(){
        val fromId = FirebaseAuth.getInstance().uid
        val reference = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")


        reference.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatlatestmessage = p0.getValue(ChatMessage::class.java) ?: return

                latestMap[p0.key!!] = chatlatestmessage
                refreshView()

            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatlatestmessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMap[p0.key!!] = chatlatestmessage
                refreshView()

            }

            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }
            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })
    }

    fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
                Log.d("LatestMessages", "Current user ${currentUser}")
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    fun verifyUserLogged() {
        val uid = FirebaseAuth.getInstance().uid
        //here need changes later
        if (uid == null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.menu_new_message -> {
                val intent = Intent(this, NewMessage::class.java)
                startActivity(intent)
            }
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

}
