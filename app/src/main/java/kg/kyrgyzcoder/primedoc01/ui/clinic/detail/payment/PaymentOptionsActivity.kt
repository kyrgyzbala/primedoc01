package kg.kyrgyzcoder.primedoc01.ui.clinic.detail.payment

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kg.kyrgyzcoder.primedoc01.MainActivity
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.ui.clinic.detail.adapters.PaymentOptionsRecyclerAdapter
import kg.kyrgyzcoder.primedoc01.ui.clinic.model.PaymentViewModel
import kg.kyrgyzcoder.primedoc01.ui.clinic.model.PaymentViewModelFactory
import kg.kyrgyzcoder.primedoc01.util.*
import kotlinx.android.synthetic.main.activity_payment_options.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.net.ConnectException
import java.net.SocketTimeoutException

class PaymentOptionsActivity : AppCompatActivity(), KodeinAware,
    PaymentOptionsRecyclerAdapter.PaymentOptionsClickListener {

    override val kodein: Kodein by closestKodein()
    private val paymentViewModelFactory: PaymentViewModelFactory by instance()

    private lateinit var paymentViewModel: PaymentViewModel
    private lateinit var adapter: PaymentOptionsRecyclerAdapter
    private var doctorUserId: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_options)

        arrBackPaymentOptions.setOnClickListener {
            onBackPressed()
        }
        val resId = intent.getIntExtra(EXTRA_RES_ID, 1)
        doctorUserId = intent.getIntExtra(EXTRA_DOCTOR_USER_ID, 1)

        visaPaymentCard.setOnClickListener {
            val intent = Intent(this, CardPaymentIntroActivity::class.java)
            intent.putExtra(EXTRA_RES_ID, resId)
            intent.putExtra(EXTRA_DOCTOR_USER_ID, doctorUserId)
            startActivity(intent)
        }

        paymentViewModel =
            ViewModelProviders.of(this, paymentViewModelFactory).get(PaymentViewModel::class.java)

        recyclerViewPaymentOptions.setHasFixedSize(true)

        val sp = getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        val token = sp.getString(USER_ACCESS_TOKEN, "")

        val userToken = "Bearer $token"
        initList(userToken)

    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        finish()
        startActivity(intent)
    }


    private fun initList(userToken: String) {

        GlobalScope.launch(Dispatchers.Main) {

            try {
                val options = paymentViewModel.getPaymentOptions(userToken)
                adapter = PaymentOptionsRecyclerAdapter(this@PaymentOptionsActivity)
                recyclerViewPaymentOptions?.adapter = adapter
                adapter.submitList(options)

                Log.d("PaymentOptionsFragment", "initList (line 75): ${options[1].paymentSteps}")
            } catch (e: ConnectException) {

            } catch (e: SocketTimeoutException) {

            }
        }
    }

    override fun onClick(position: Int) {
        val current = adapter.getItemAtPos(position)
        val intent = Intent(this, PaymentInstructionsActivity::class.java)
        intent.putExtra(EXTRA_PAYMENT_OPTION, current)
        startActivity(intent)
    }
}