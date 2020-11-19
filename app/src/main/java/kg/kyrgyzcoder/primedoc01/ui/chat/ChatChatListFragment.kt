package kg.kyrgyzcoder.primedoc01.ui.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.ui.chat.adapter.ChatListRecyclerViewAdapter
import kg.kyrgyzcoder.primedoc01.ui.chat.model.Chat
import kg.kyrgyzcoder.primedoc01.util.*
import kotlinx.android.synthetic.main.fragment_chat_chat_list.*
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*


class ChatChatListFragment : Fragment(), ChatListRecyclerViewAdapter.ChatListClickListener {

    private var adapter: ChatListRecyclerViewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_chat_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createChatWithAdmin()
    }

    private fun createChatWithAdmin() {
        try {
            val userId = getUserId().toString()
            val sp = requireActivity().getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE)
            val phone = sp.getString(USER_PHONE, "")
            val user = FirebaseAuth.getInstance().currentUser!!
            val db = FirebaseFirestore.getInstance()
            db.collection("chatAdmin").document(userId).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val snapshot = it.result
                    if (snapshot != null && snapshot.exists()) {
                        Log.d("ChatChatListFragment", "createChatWithAdmin (line 52): chat exists")
                    } else {
                        val map = mutableMapOf<String, Any>()
                        map["adminId"] = "a"
                        map["adminPhone"] = ""
                        map["chatStarted"] = true
                        map["clientId"] = userId
                        map["lastMessage"] = "Нажмите чтобы написать админу"
                        map["lastMessageSenderId"] = userId
                        map["lastMessageTime"] = Timestamp(Date())
                        map["name"] = phone!!
                        map["surname"] = "USER"
                        db.collection("chatAdmin").document(userId).set(map)
                            .addOnSuccessListener {
                                Log.d(
                                    "ChatChatListFragment",
                                    "createChatWithAdmin (line 71): success"
                                )
                            }
                    }
                }
            }
        } catch (e: NullPointerException) {

        }

    }

    override fun onStart() {
        super.onStart()

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            initAdminChat(user)
            initRecyclerView(user)
        }
    }

    private fun getUserId(): Int {
        val sp = requireActivity().getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        return sp.getInt(CHAT_ID_KEY, 1)
    }

    private fun initAdminChat(user: FirebaseUser) {

        val userId = getUserId()
        val db = FirebaseFirestore.getInstance()
        Log.d("ChatChatListFragment", "initAdminChat (line 51): ")
        val ref = db.collection("chatAdmin").document(userId.toString())
        ref.get().addOnSuccessListener {
            val lastMessage = it.getString("lastMessage")
            val lastMessageSender = it.getString("lastMessageSenderId")
            val lastMessageTime = it.getTimestamp("lastMessageTime")
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ROOT)
            Log.d("ChatChatListFragment", "initAdminChat (line 58): ${ref.path}aa")
            if (lastMessageTime != null) {
                val time = sdf.format(lastMessageTime.toDate())
                textViewDateChatList?.text = time
            }
            senderNameTextView.text = if (lastMessageSender == userId.toString())
                getString(R.string.you)
            else
                getString(R.string.admin) + ":"
            lastMessageTextView.text = lastMessage
            prBarChatList.visibility = View.GONE
            val path = it.reference.path
            chatAdmin.setOnClickListener {
                val intent = Intent(requireContext(), PrivateChatActivity::class.java)
                intent.putExtra(EXTRA_CHAT_REF, path)
                intent.putExtra(EXTRA_CHAT_TYPE, "admin")
                startActivity(intent)
            }
        }.addOnFailureListener {
            Log.d("ChatChatListFragment", "initAdminChat (line 79): no chat")
        }
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    private fun initRecyclerView(user: FirebaseUser) {
        Log.d("ChatChatListFragment", "initRecyclerView (line 48): ${user.uid}")
        val userId = getUserId().toString()
        val db = FirebaseFirestore.getInstance()
        val query = db.collection("PrimeDocChat").whereEqualTo("clientId", userId)
            .orderBy("lastMessageTime", Query.Direction.DESCENDING)
        val options: FirestoreRecyclerOptions<Chat> =
            FirestoreRecyclerOptions.Builder<Chat>().setQuery(query, Chat::class.java).build()

        adapter = ChatListRecyclerViewAdapter(options, this)
        Log.d("ChatChatListFragment", "initRecyclerView (line 56): ")
        recyclerViewChatChat.adapter = adapter
        adapter?.startListening()
    }

    override fun onClick(position: Int) {
        val ref = adapter!!.snapshots.getSnapshot(position).reference.path
        val intent = Intent(requireContext(), PrivateChatActivity::class.java)
        intent.putExtra(EXTRA_CHAT_REF, ref)
        intent.putExtra(EXTRA_CHAT_TYPE, "doctor")
        startActivity(intent)
    }


}