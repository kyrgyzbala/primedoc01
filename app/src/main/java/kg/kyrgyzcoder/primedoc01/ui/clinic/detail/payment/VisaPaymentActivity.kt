package kg.kyrgyzcoder.primedoc01.ui.clinic.detail.payment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.NetworkOnMainThreadException
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.data.payment.ModelPayment
import kg.kyrgyzcoder.primedoc01.ui.clinic.model.PaymentViewModel
import kg.kyrgyzcoder.primedoc01.ui.clinic.model.PaymentViewModelFactory
import kg.kyrgyzcoder.primedoc01.util.*
import kotlinx.android.synthetic.main.activity_visa_payment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.SocketTimeoutException
import java.net.URL
import java.util.*

class VisaPaymentActivity : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val paymentViewModelFactory: PaymentViewModelFactory by instance()

    private lateinit var paymentViewModel: PaymentViewModel

    private var snackbar: Snackbar? = null

    private var resId = 1
    private var doctorUserId: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visa_payment)

        paymentViewModel =
            ViewModelProviders.of(this, paymentViewModelFactory).get(PaymentViewModel::class.java)

        arrBackVisa.setOnClickListener {
            onBackPressed()
        }

        resId = intent.getIntExtra(EXTRA_RES_ID, 1)
        doctorUserId = intent.getIntExtra(EXTRA_DOCTOR_USER_ID, 1)

        initWebView()

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        snackbar?.dismiss()
        webViewVisa.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: String?
            ): Boolean {
                view?.loadUrl(request!!)

                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                Log.d("VisaPaymentFragment", "shouldOverrideUrlLoading (line 45): $url")
                if (url == "https://www.google.com/") {
                    paymentSuccess()
                }
                if (url == "https://yahoo.com/") {
                    paymentFailed()
                }
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                url: WebResourceRequest?
            ): Boolean {

                try {
                    val aURL = URL(url?.url.toString())
                    val conn = aURL.openConnection()
                    conn.connect()
                    val iS = conn.getInputStream()

                    val reader = InputStreamReader(iS)
                    val bReader = BufferedReader(reader)
                    val sb = StringBuffer()
                    var str = bReader.readLine()
                    while (str != null) {
                        sb.append(str)
                        str = bReader.readLine()
                    }

                    Log.d("Nuriko", "shouldOverrideUrlLoading (line 104): ${sb.toString()}")

                } catch (e: NetworkOnMainThreadException) {
                    Log.d("Nuriko", "shouldOverrideUrlLoading (line 107): fail $e")
                }

                return true
            }
        }

        webViewVisa.settings.loadsImagesAutomatically = true
        webViewVisa.settings.javaScriptEnabled = true
        webViewVisa.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webViewVisa.loadUrl("http://165.22.74.215:8079/info.php")

    }

    private fun paymentFailed() {
        val intent = Intent()
        intent.putExtra(EXTRA_RES_ID, resId)
        startActivity(intent)
    }

    private fun paymentSuccess() {
        prBarVisaPay.visibility = View.VISIBLE
        val sp = getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        val token = sp.getString(USER_ACCESS_TOKEN, "")
        val resId = intent.getIntExtra(EXTRA_RES_ID, 1)
        val name = intent.getStringExtra(EXTRA_CARDHOLDER)
        val cardNumber = intent?.getStringExtra(EXTRA_CARD_NUMBER)

        var firstName = ""
        var index = 0
        for (c in name!!) {
            if (c != ' ') {
                index++
                firstName += c
            } else
                break
        }
        val lastName = name.takeLast(name.length - index)

        val userToken = "Bearer $token"

        val model = ModelPayment(
            "XXXXXX",
            firstName,
            cardNumber!!,
            lastName,
            Date().time.toString(),
            "400",
            "400",
            "Услуги"
        )

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = paymentViewModel.sendPaymentSuccess(userToken, resId, model)
                if (response.isSuccessful) {
                    val intent = Intent(
                        this@VisaPaymentActivity,
                        PaymentSuccessActivity::class.java
                    )
                    intent.putExtra(EXTRA_DOCTOR_USER_ID, doctorUserId)
                    startActivity(intent)
                } else {
                    handleSendServerError()
                }
            } catch (e: SocketTimeoutException) {
                Toast.makeText(
                    this@VisaPaymentActivity,
                    getString(R.string.errorTryAgain),
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }
    }


    private fun handleSendServerError() {
        startActivity(Intent(this, HandleServerActivity::class.java))
    }
}