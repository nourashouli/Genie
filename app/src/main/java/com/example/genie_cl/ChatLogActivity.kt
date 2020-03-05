package com.example.genie_cl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.genie_cl.messaging
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.example.genie_cl.Models.ChatMessage
import com.example.genie_cl.Models.User
import com.example.genie_cl.views.ChatFromItem
import com.example.genie_cl.views.ChatToItem
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLogActivity : AppCompatActivity() {

  companion object {
    val TAG = "ChatLog"
  }


  val adapter = GroupAdapter<ViewHolder>()

  var toUser: User? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_chat_log)

    recyclerview_chat_log.adapter = adapter
    recyclerview_chat_log.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL,false)

    toUser = intent.getParcelableExtra<User>(messaging.USER_KEY)

    supportActionBar?.title = toUser?.username

//   setupDummyData()
    listenForMessages()

    send_button_chat_log.setOnClickListener {
      Log.d(TAG, "Attempt to send message....")
      performSendMessage()
    }
  }

  private fun listenForMessages() {
    val fromId = FirebaseAuth.getInstance().uid
    val toId = toUser?.uid
    val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

    ref.addChildEventListener(object: ChildEventListener {

      override fun onChildAdded(p0: DataSnapshot, p1: String?) {
        val chatMessage = p0.getValue(ChatMessage::class.java)

        if (chatMessage != null) {
          Log.d(TAG, chatMessage.text)

          if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
            val currentUser = LatestMessagesActivity.currentUser ?: return
           adapter.add(ChatFromItem(chatMessage.text, currentUser))
          } else {
            adapter.add(ChatToItem(chatMessage.text, toUser!!))
          }
        }

        recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)
        recyclerview_chat_log.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL,false)

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
    val text = edittext_chat_log.text.toString()

    val fromId = FirebaseAuth.getInstance().uid
    val user = intent.getParcelableExtra<User>(messaging.USER_KEY)
    val toId = user.uid

    if (fromId == null) return

//   val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
    val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

    val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

    val chatMessage = ChatMessage(reference.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)

    reference.setValue(chatMessage)
      .addOnSuccessListener {
        Log.d(TAG, "Saved our chat message: ${reference.key}")
        edittext_chat_log.text.clear()
        recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)
        recyclerview_chat_log.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL,false)

      }

    toReference.setValue(chatMessage)

    val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
    latestMessageRef.setValue(chatMessage)

    val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
    latestMessageToRef.setValue(chatMessage)
  }
}
