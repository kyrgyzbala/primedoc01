package kg.kyrgyzcoder.primedoc01.ui.clinic.detail.payment

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kg.kyrgyzcoder.primedoc01.MainActivity
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.data.payment.ModelPaymentOptions
import kg.kyrgyzcoder.primedoc01.ui.clinic.detail.adapters.PaymentInstRecyclerAdapter
import kg.kyrgyzcoder.primedoc01.util.EXTRA_PAYMENT_OPTION
import kg.kyrgyzcoder.primedoc01.util.MEGA_PAY
import kotlinx.android.synthetic.main.activity_payment_instructions.*

class PaymentInstructionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_instructions)

        arrBackPayInts.setOnClickListener {
            goToMain()
        }

        val paymentOption =
            intent?.getSerializableExtra(EXTRA_PAYMENT_OPTION) as ModelPaymentOptions

        val color = getColorPay(paymentOption.name!!)

        appBarPayInts.setBackgroundColor(Color.parseColor(color))

        nextSteps.text = paymentOption.nextSteps
        if (!paymentOption.logo.isNullOrEmpty())
            Glide.with(this).load(paymentOption.logo).into(imgViewPayInst)

        val adapter = PaymentInstRecyclerAdapter(color)
        recyclerViewPayInst.setHasFixedSize(true)
        recyclerViewPayInst.adapter = adapter
        adapter.submitList(paymentOption.paymentSteps)

    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        finish()
        startActivity(intent)
    }

    private fun getColorPay(name: String): String {
        return if (name.contains("MegaPay"))
            MEGA_PAY
        else if (name.contains("О!Деньги"))
            "#F0047F"
        else if (name.contains("Balance"))
            "#FFC700"
        else
            "#00BDD0"
    }
}