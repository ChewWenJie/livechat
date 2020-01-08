package com.example.livechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_too_row.view.*

class chat : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //val username = intent.getStringExtra(NewMessage.USER_KEY)
        val user = intent.getParcelableExtra<User>(NewMessage.USER_KEY)
        supportActionBar?.title = user.username

        dummyData()
    }

    fun dummyData() {
        val adapter = GroupAdapter<ViewHolder>()
        adapter.add(ChatfromItem())
        adapter.add(ChattoItem())
        adapter.add(ChatfromItem())
        adapter.add(ChattoItem())
        adapter.add(ChatfromItem())
        adapter.add(ChattoItem())
        adapter.add(ChatfromItem())
        adapter.add(ChattoItem())

        recycleview_chat.adapter = adapter
    }
}

class ChatfromItem: Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_from_row.text = "asd"
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}
class ChattoItem: Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_too_row.text = "asdasd"
    }

    override fun getLayout(): Int {
        return R.layout.chat_too_row
    }
}