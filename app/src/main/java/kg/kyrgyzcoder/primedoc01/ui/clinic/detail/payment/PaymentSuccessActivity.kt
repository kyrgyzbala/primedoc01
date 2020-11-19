package kg.kyrgyzcoder.primedoc01.ui.clinic.detail.payment

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kg.kyrgyzcoder.primedoc01.MainActivity
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.ui.chat.model.Chat
import kg.kyrgyzcoder.primedoc01.util.*
import kotlinx.android.synthetic.main.activity_payment_success.*
import java.util.*

class PaymentSuccessActivity : AppCompatActivity() {

    private var doctorUserId: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_success)

        doctorUserId = intent.getIntExtra(EXTRA_DOCTOR_USER_ID, 1)

        createChat()

    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        finish()
        startActivity(intent)
    }

    override fun onBackPressed() {
        goToMain()
    }

    private fun createChat() {
        val sp = getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        val userUserId = sp.getInt(CHAT_ID_KEY, 1)
        val ref = FirebaseFirestore.getInstance().collection("PrimeDocChat")
            .document("$doctorUserId$userUserId")
        ref.get().addOnSuccessListener {
            if (it.exists() && it.getString("doctorName") != null) {
                Log.d("PaymentSuccessActivity", "createChat (line 56): exists")
            } else {
                createNewChat(userUserId)
            }
        }.addOnFailureListener {
            createNewChat(userUserId)
        }
        arrBackPaymentSuccess.setOnClickListener {
            goToMain()
        }

        buttonToMainSuccess.setOnClickListener {
            goToMain()
        }
    }

    private fun createNewChat(userUserId: Int) {

        val sp = getSharedPreferences(DOCTOR_DATA, Context.MODE_PRIVATE)
        val docName = sp.getString(DOCTOR_NAME_KEY, "")
        val docSurname = sp.getString(DOCTOR_SURNAME_KEY, "")

        val model = Chat(
            userUserId.toString(),
            doctorUserId.toString(),
            "",
            ".....",
            doctorUserId.toString(),
            false,
            Timestamp(Calendar.getInstance().time),
            "",
            docName,
            docSurname
        )

        val ref = FirebaseFirestore.getInstance().collection("PrimeDocChat")
            .document("$doctorUserId$userUserId")

        ref.set(model).addOnSuccessListener {
            Log.d("PaymentSuccessActivity", "createNewChat (line 88): chatCreated")
        }

    }
}