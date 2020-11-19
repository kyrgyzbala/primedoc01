package kg.kyrgyzcoder.primedoc01.ui.clinic.detail.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import kg.kyrgyzcoder.primedoc01.MainActivity
import kg.kyrgyzcoder.primedoc01.R
import kotlinx.android.synthetic.main.activity_handle_server.*

class HandleServerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_handle_server)
        arrBackHandleSend.setOnClickListener {
            goToMain()
        }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        finish()
        startActivity(intent)
    }
}