package kg.kyrgyzcoder.primedoc01.ui.chat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.ui.chat.adapter.MessagesRecyclerViewAdapter
import kg.kyrgyzcoder.primedoc01.ui.chat.model.Message
import kg.kyrgyzcoder.primedoc01.util.*
import kotlinx.android.synthetic.main.activity_private_chat.*
import java.util.*

class PrivateChatActivity : AppCompatActivity(), MessagesRecyclerViewAdapter.MessageClickListener {

    private lateinit var docRef: DocumentReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private lateinit var adapter: MessagesRecyclerViewAdapter
    private var messageType: String = "text"
    private var type: String? = "admin"
    private var ref: String? = ""
    private var image: String = ""
    private var imgUri: Uri? = null
    private var canWrite: Boolean = false
    private var adminOrDoctorId: String? = ""

    private var adminId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_private_chat)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        ref = intent?.getStringExtra(EXTRA_CHAT_REF)
        type = intent?.getStringExtra(EXTRA_CHAT_TYPE)
        docRef = db.document(ref!!)

        if (type == "admin")
            videoCallButton.visibility = View.GONE
        else
            initDoctorOrAdminData()
        val listener = docRef.addSnapshotListener { snapshot, error ->
            if (snapshot != null && snapshot.exists()) {
                val started = snapshot.getBoolean("chatStarted")
                if (started != null && started == true) {
                    canWrite = true
                }
            }
        }
        arrBackChatActual.setOnClickListener {
            hideKeyboard()
            onBackPressed()
        }

        attachButtonChat.setOnClickListener {
            openImageChoose()
        }

        sendMessageButton.setOnClickListener {
            if (canWrite)
                listener.remove()
            if (messageType == "text" && messageEditText.text.toString().isNotEmpty() && canWrite)
                sendMessage()
            else if (canWrite) {
                if (imgUri != null)
                    uploadPhotoToCloud()
            } else {
                Snackbar.make(chatActualMain, getString(R.string.cantWrite), Snackbar.LENGTH_LONG)
                    .setTextColor(Color.parseColor("#FFFFFF"))
                    .show()
            }
        }

    }

    private fun getUserId(): Int {
        val sp = getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        return sp.getInt(CHAT_ID_KEY, 1)
    }

    private fun sendMessage() {
        val sp = getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE)
        val userPhone = sp.getString(USER_PHONE, "")
        imgViewAttach.visibility = View.GONE
        val message = messageEditText.text.toString()
        messageEditText.setText("")
        val user = firebaseAuth.currentUser!!
        val model = Message(getUserId().toString(), adminId, message, messageType, image)
        model.time = Timestamp.now()
        model.senderName = userPhone
        messageType = "text"
        docRef.collection("messages").document().set(model)
            .addOnCompleteListener {
                val map = mutableMapOf<String, Any>()
                map["lastMessage"] = message
                map["lastMessageSenderId"] = getUserId().toString()
                map["lastMessageTime"] = Timestamp.now()
                docRef.set(map, SetOptions.merge()).addOnSuccessListener {
                    Log.d(
                        "ChatActualFragment",
                        "onViewCreated (line 70): success updated lastMessage"
                    )
                }
                adapter.notifyDataSetChanged()
                recyclerViewMessagesChat.smoothScrollToPosition(0)
            }
    }

    private fun openImageChoose() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(
            Intent.createChooser(intent, "Выберите фото профила!"),
            CHOOSE_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == CHOOSE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            val uri = data.data
            if (uri != null) {
                Log.d("Chat", "onActivityResult: $uri")
                imgViewAttach.setImageURI(uri)
                imgViewAttach.visibility = View.VISIBLE
                imgUri = uri
                messageType = "image"
                messageEditText.requestFocus()
                val imm =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(messageEditText, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadPhotoToCloud() {
        prBarChat.visibility = View.VISIBLE
        val user = firebaseAuth.currentUser
        if (user != null) {
            val ref =
                FirebaseStorage.getInstance().getReference("images/" + Date().time + ".jpg")
            ref.putFile(imgUri!!).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    val url = it.toString()
                    if (url.isNotEmpty()) {
                        image = url
                        prBarChat.visibility = View.GONE
                        sendMessage()
                    } else {
                        prBarChat.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun initDoctorOrAdminData() {
        docRef.get().addOnSuccessListener {
            adminId = it.getString("adminId")!!
            adminOrDoctorId = adminId
            Log.d("PrivateChatActivity", "initDoctorOrAdminData (line 188): $adminOrDoctorId $canWrite $type")
            videoCallButton.setOnClickListener {
                if (adminOrDoctorId != null && canWrite && type != "admin")
                    makeVideoCall()
            }
            if (type == "doctor") {
//                getDoctorData(adminId)
                val name = it.getString("doctorName")
                val surName = it.getString("doctorSurname")
                val fullName = "$name $surName"
                textViewUserNameChat.text = fullName
            }
        }
    }

    private fun makeVideoCall() {
        val intent = Intent(this, CallingActivity::class.java)
        intent.putExtra(EXTRA_DOCTOR_ID, adminOrDoctorId)
        intent.putExtra(EXTRA_CALL_TYPE, type)
        intent.putExtra(EXTRA_DOC_NAME, textViewUserNameChat.text.toString())
        startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
        messageEditText.requestFocus()
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(messageEditText, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onStart() {
        super.onStart()
        messageEditText.requestFocus()
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(messageEditText, InputMethodManager.SHOW_IMPLICIT)
        recyclerViewMessagesChat.smoothScrollToPosition(0)
        val user = firebaseAuth.currentUser
        if (user != null)
            initRecyclerView(user)
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    private fun initRecyclerView(user: FirebaseUser) {

        recyclerViewMessagesChat.setHasFixedSize(true)

        val query =
            docRef.collection("messages").orderBy("time", Query.Direction.DESCENDING)
        val options: FirestoreRecyclerOptions<Message> =
            FirestoreRecyclerOptions.Builder<Message>().setQuery(query, Message::class.java)
                .build()

        adapter = MessagesRecyclerViewAdapter(options, this)
        recyclerViewMessagesChat.adapter = adapter

        adapter.startListening()

        val observer = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                recyclerViewMessagesChat.smoothScrollToPosition(0)
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                super.onItemRangeChanged(positionStart, itemCount)
                recyclerViewMessagesChat.smoothScrollToPosition(0)
            }
        }
        adapter.registerAdapterDataObserver(observer)

        recyclerViewMessagesChat.smoothScrollToPosition(0)

        recyclerViewMessagesChat.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                recyclerViewMessagesChat.postDelayed({
                    recyclerViewMessagesChat.smoothScrollToPosition(0)
                }, 100)
            }
        }
    }

    override fun onImageClick(image: String) {
        hideKeyboard()
        val intent = Intent(this, ImageViewerActivity::class.java)
        intent.putExtra(EXTRA_IMG_URL, image)
        intent.putExtra(EXTRA_CHAT_REF, ref)
        intent.putExtra(EXTRA_CHAT_TYPE, type)
        startActivity(intent)
    }
}