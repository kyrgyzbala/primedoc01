package kg.kyrgyzcoder.primedoc01.ui.chat

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kg.kyrgyzcoder.primedoc01.MainActivity
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.util.*
import kotlinx.android.synthetic.main.activity_calling.*

class CallingActivity : AppCompatActivity() {


    private var adminOrDocId: String = ""

    private var listener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calling)
        adminOrDocId = intent.getStringExtra(EXTRA_DOCTOR_ID)!!
        val docName = intent.getStringExtra(EXTRA_DOC_NAME)
        userNameTextView.text = docName

    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null)
            makePhoneCall(user)
    }

    private fun getUserId(): Int {
        val sp = getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        return sp.getInt(CHAT_ID_KEY, 1)
    }

    private fun makePhoneCall(user: FirebaseUser) {
        val db = FirebaseFirestore.getInstance()
        val ref =
            db.collection("doctors").document(adminOrDocId).collection("call").document("calling")
        ref.get().addOnSuccessListener {
            if (it.exists() && !it.getString("uid").isNullOrEmpty()) {
                Toast.makeText(this, "Доктор сейчас занят! Повторите попозже!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val callData = mutableMapOf<String, Any>()
                callData["uid"] = getUserId().toString()
                callData["receiverId"] = adminOrDocId
                callData["accepted"] = false
                callData["declined"] = false
                ref.set(callData).addOnSuccessListener {
                    addSnapListener(ref)
                    setEndCall(ref)
                }
            }
        }

    }

    private fun setEndCall(ref: DocumentReference) {
        cancelCallButton.setOnClickListener {
            val callData = mutableMapOf<String, Any>()
            callData["uid"] = ""
            callData["receiverId"] = ""
            callData["accepted"] = false
            callData["declined"] = false
            ref.set(callData).addOnSuccessListener {

            }
        }
    }

    private fun addSnapListener(ref: DocumentReference) {

        listener = ref.addSnapshotListener { value, _ ->
            if (value != null && value.exists()) {
                val accepted = value.getBoolean("accepted")
                val declined = value.getBoolean("declined")

                if (accepted != null && accepted == true) {
                    val intent = Intent(this, CallActualActivity::class.java)
                    intent.putExtra(EXTRA_CALL_REF, ref.path)
                    intent.putExtra(EXTRA_DOC_NAME, userNameTextView.text.toString())
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    finish()
                    startActivity(intent)
                }
                if (declined != null && declined == true) {
                    try {
                        Toast.makeText(this, "Звонок отклонен!", Toast.LENGTH_LONG).show()
                        onBackPressed()
                    } catch (e: NullPointerException) {
                        val intent = Intent(this, MainActivity::class.java)
                        finish()
                        startActivity(intent)
                    }
                }

            }

        }
    }
}