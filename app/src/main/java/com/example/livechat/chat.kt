package com.example.livechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*

class chat : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.title = "Chat"

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

    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}
class ChattoItem: Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.chat_too_row
    }
}