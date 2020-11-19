package kg.kyrgyzcoder.primedoc01.ui.chat

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.util.*
import kotlinx.android.synthetic.main.activity_receiving.*

class ReceivingActivity : AppCompatActivity() {

    private lateinit var mp: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiving)

        val uid = intent.getStringExtra(EXTRA_RECEIVE_ID)!!

        getDoctorName(uid)
    }

    private fun getDoctorName(uid: String) {

        val ref = FirebaseFirestore.getInstance().collection("doctors").document(uid)
        ref.get().addOnSuccessListener {
            val name = it.getString("name")
            val fatherName = it.getString("fatherName")
            val image = it.getString("image")
            val fullName = "$name $fatherName"
            userNameTextViewReceive.text = fullName

            if (!image.isNullOrEmpty())
                Glide.with(this).load(image).into(avatarUserReceive)

            setClicks()
        }
    }

    private fun getUserId(): Int {
        val sp = getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        return sp.getInt(CHAT_ID_KEY, 1)
    }

    private fun setClicks() {
        val user = FirebaseAuth.getInstance().currentUser!!
        val ref =
            FirebaseFirestore.getInstance().collection("users").document(getUserId().toString())
                .collection("call").document("calling")

        mp = MediaPlayer.create(applicationContext, R.raw.ring)
        mp.start()

        acceptCallButtonReceive.setOnClickListener {
            val map = mutableMapOf<String, Boolean>()
            map["accepted"] = true
            ref.set(map, SetOptions.merge()).addOnSuccessListener {
                mp.reset()
                mp.release()
                val intent = Intent(this, CallActualActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra(EXTRA_CALL_REF, ref.path)
                intent.putExtra(EXTRA_DOC_NAME, userNameTextViewReceive.text.toString())
                finish()
                startActivity(intent)
            }
        }

        cancelCallButtonReceive.setOnClickListener {
            mp.reset()
            mp.release()
            val map = mutableMapOf<String, Any>()
            map["accepted"] = false
            map["declined"] = true
            map["uid"] = ""
            map["receiverId"] = ""
            ref.set(map, SetOptions.merge()).addOnSuccessListener {
                onBackPressed()
            }
        }
    }


}