package kg.kyrgyzcoder.primedoc01.ui.chat.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.ui.chat.model.Chat
import kotlinx.android.synthetic.main.row_chat_list.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatListRecyclerViewAdapter(
    options: FirestoreRecyclerOptions<Chat>, private val listener: ChatListClickListener
) : FirestoreRecyclerAdapter<Chat, ChatListRecyclerViewAdapter.ViewHolder>(options) {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(chat: Chat) {
            val last = if (chat.lastMessage.length > 15)
                chat.lastMessage.take(15) + "..."
            else
                chat.lastMessage
            itemView.lastMessageTextView.text = last

            Log.d("ViewHolder", "onBind (line 24): ")
            val dateLast = chat.lastMessageTime.toDate()
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ROOT)
            val lastMessageTimeStr = sdf.format(dateLast)
            itemView.textViewDateChatList.text = lastMessageTimeStr
            itemView.senderNameTextView.text = if (chat.lastMessageSenderId == chat.adminId)
                itemView.context.getString(R.string.doctor) + ":"
            else
                itemView.context.getString(R.string.you)

            val fullName = "${chat.doctorName} ${chat.doctorSurname}"
            itemView.textViewUserName.text = fullName


            itemView.setOnClickListener {
                listener.onClick(adapterPosition)
            }
        }

//        private fun getDoctorData(adminId: String?) {
//            val db = FirebaseFirestore.getInstance()
//            db.collection("doctors").document(adminId!!).get().addOnSuccessListener {
//                val image = it.getString("image")
//                val name = it.getString("name")
//                val fatherName = it.getString("fatherName")
//                if (!image.isNullOrEmpty())
//                    Glide.with(itemView).load(image).into(itemView.imgViewChatList)
//                val fullName = "$name $fatherName"
//
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_chat_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Chat) {
        holder.onBind(model)
    }

    interface ChatListClickListener {
        fun onClick(position: Int)
    }
}