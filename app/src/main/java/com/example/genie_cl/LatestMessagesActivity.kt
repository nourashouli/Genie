//package com.example.genie_cl
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import 	androidx.recyclerview.widget.DividerItemDecoration
//import android.util.Log
//import android.view.Menu
//import androidx.recyclerview.widget.LinearLayoutManager
//import android.view.MenuItem
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.*
//import com.example.genie_cl.Models.ChatMessage
//import com.example.genie_cl.Models.User
//import com.example.genie_cl.R
//import com.xwray.groupie.GroupAdapter
//import com.xwray.groupie.ViewHolder
//import kotlinx.android.synthetic.main.activity_latest_messages.*
//import com.example.genie_cl.views.LatestMessageRow
//class LatestMessagesActivity : AppCompatActivity() {
//
//    companion object {
//        var currentUser: User? = null
//        val TAG = "LatestMessages"
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_latest_messages)
//
//        new_message.setOnClickListener {
//            val intent = Intent(this, messaging::class.java)
//            startActivity(intent)
//        }
//        recyclerview_latest_messages.adapter = adapter
//        recyclerview_latest_messages.addItemDecoration(
//            DividerItemDecoration(
//                this,
//                DividerItemDecoration.VERTICAL
//            )
//        )
//        recyclerview_latest_messages.layoutManager =
//            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
//        // set item click listener on your adapter
//        adapter.setOnItemClickListener { item, view ->
//            Log.d(TAG, "123")
//            val intent = Intent(this, ChatLogActivity::class.java)
//
//            // we are missing the chat partner user
//
//            val row = item as LatestMessageRow
//            intent.putExtra(messaging.USER_KEY, row.chatPartnerUser)
//            startActivity(intent)
//        }
//
//        //setupDummyRows()
//        listenForLatestMessages()
//
//        fetchCurrentUser()
//
//        verifyUserIsLoggedIn()
//    }
//
//    val latestMessagesMap = HashMap<String, ChatMessage>()
//
//    private fun refreshRecyclerViewMessages() {
//        adapter.clear()
//        latestMessagesMap.values.forEach {
//            adapter.add(LatestMessageRow(it))
//        }
//    }
//
//    private fun listenForLatestMessages() {
//        val fromId = FirebaseAuth.getInstance().uid
//        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
//        ref.addChildEventListener(object : ChildEventListener {
//            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
//                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
//                latestMessagesMap[p0.key!!] = chatMessage
//                refreshRecyclerViewMessages()
//            }
//
//            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
//                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
//                latestMessagesMap[p0.key!!] = chatMessage
//                refreshRecyclerViewMessages()
//            }
//
//            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
//
//            }
//
//            override fun onChildRemoved(p0: DataSnapshot) {
//
//            }
//
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//        })
//    }
//
//    val adapter = GroupAdapter<ViewHolder>()
//
////  private fun setupDummyRows() {
////
////
////    adapter.add(LatestMessageRow())
////    adapter.add(LatestMessageRow())
////    adapter.add(LatestMessageRow())
////  }
//
//    private fun fetchCurrentUser() {
//        val uid = FirebaseAuth.getInstance().uid
//        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
//        ref.addListenerForSingleValueEvent(object : ValueEventListener {
//
//            override fun onDataChange(p0: DataSnapshot) {
//                currentUser = p0.getValue(User::class.java)
//                Log.d("LatestMessages", "Current user ${currentUser?.profileImageUrl}")
//            }
//
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//        })
//    }
//
//    private fun verifyUserIsLoggedIn() {
//        val uid = FirebaseAuth.getInstance().uid
//        if (uid == null) {
//            val intent = Intent(this, SignUp::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
//        }
//    }
//}