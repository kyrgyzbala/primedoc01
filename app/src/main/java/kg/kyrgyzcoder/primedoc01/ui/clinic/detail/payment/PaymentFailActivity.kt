package kg.kyrgyzcoder.primedoc01.ui.clinic.detail.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.util.EXTRA_RES_ID
import kotlinx.android.synthetic.main.activity_payment_fail.*

class PaymentFailActivity : AppCompatActivity() {

    private var resId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_fail)

        resId = intent.getIntExtra(EXTRA_RES_ID, 1)

        arrowBackPaymentFail.setOnClickListener {
            onBackPressed()
        }

        buttonBackFail.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, PaymentOptionsActivity::class.java)
        intent.putExtra(EXTRA_RES_ID, resId)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
        startActivity(intent)
    }
}