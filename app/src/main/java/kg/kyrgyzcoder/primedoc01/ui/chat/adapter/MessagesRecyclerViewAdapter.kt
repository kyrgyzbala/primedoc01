package kg.kyrgyzcoder.primedoc01.ui.chat.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.ui.chat.model.Message
import kg.kyrgyzcoder.primedoc01.util.CHAT_ID_KEY
import kg.kyrgyzcoder.primedoc01.util.USER_CRED
import kg.kyrgyzcoder.primedoc01.util.USER_ID_KEY
import kotlinx.android.synthetic.main.row_message.view.*
import java.text.SimpleDateFormat
import java.util.*

class MessagesRecyclerViewAdapter(
    options: FirestoreRecyclerOptions<Message>,
    private val listener: MessageClickListener
) :
    FirestoreRecyclerAdapter<Message, MessagesRecyclerViewAdapter.ViewHolder>(
        options
    ) {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(message: Message) {

            val sp = itemView.context.getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
            val userId = sp.getInt(CHAT_ID_KEY, 1).toString()

            if (message.sender == userId) {
                itemView.messageSentRelative.visibility = View.VISIBLE
                itemView.messageReceivedRelative.visibility = View.GONE
                try {
                    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ROOT)
                    val dateStr = sdf.format(message.time.toDate())
                    itemView.timeSentMessage.text = dateStr
                } catch (e: NullPointerException) {
                    Log.d("ViewHolder", "onBind (line 38): null $message")
                }
                if (message.type == "text") {
                    itemView.imgCardViewSent.visibility = View.GONE
                    itemView.textViewMessageSent.text = message.message
                } else {
                    itemView.imgCardViewSent.visibility = View.VISIBLE
                    itemView.textViewMessageSent.text = message.message
                    if (message.image.isNotEmpty()) {
                        Glide.with(itemView).load(message.image).into(itemView.imgViewMessageSent)
                    }
                }
            } else {
                itemView.messageSentRelative.visibility = View.GONE
                itemView.messageReceivedRelative.visibility = View.VISIBLE
                val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ROOT)
                val dateStr = sdf.format(message.time.toDate())
                itemView.textViewMessageReceived.text = message.message
                itemView.timeReceivedMessage.text = dateStr
                if (message.type == "text") {
                    itemView.imgCardViewReceived.visibility = View.GONE
                } else {
                    itemView.imgCardViewReceived.visibility = View.VISIBLE
                    if (message.image.isNotEmpty()) {
                        Glide.with(itemView).load(message.image)
                            .into(itemView.imgViewMessageReceived)
                    }
                }
            }

            itemView.imgViewMessageSent.setOnClickListener {
                listener.onImageClick(message.image)
            }

            itemView.imgViewMessageReceived.setOnClickListener {
                listener.onImageClick(message.image)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_message, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Message) {
        holder.onBind(model)
    }

    interface MessageClickListener {
        fun onImageClick(image: String)
    }
}