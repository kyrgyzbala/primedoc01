package kg.kyrgyzcoder.primedoc01.ui.clinic.detail.payment

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.ui.about.model.AboutUsViewModel
import kg.kyrgyzcoder.primedoc01.ui.about.model.AboutUsViewModelFactory
import kg.kyrgyzcoder.primedoc01.util.*
import kotlinx.android.synthetic.main.activity_card_payment_intro.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import retrofit2.HttpException

class CardPaymentIntroActivity : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val aboutUsViewModelFactory: AboutUsViewModelFactory by instance()

    private lateinit var aboutUsViewModel: AboutUsViewModel

    private var doctorUserId: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_payment_intro)

        val resId = intent.getIntExtra(EXTRA_RES_ID, 1)
        doctorUserId = intent.getIntExtra(EXTRA_DOCTOR_USER_ID, 1)

        aboutUsViewModel =
            ViewModelProviders.of(this, aboutUsViewModelFactory).get(AboutUsViewModel::class.java)

        initHtml()

        arrBackPaymentIntro.setOnClickListener {
            onBackPressed()
        }

        buttonNextPaymentIntro.setOnClickListener {
            if (checkInputs()) {
                val intent = Intent(this, VisaPaymentActivity::class.java)
                intent.putExtra(EXTRA_RES_ID, resId)
                intent.putExtra(EXTRA_CARD_NUMBER, cardNumberEditText.text.toString())
                intent.putExtra(EXTRA_CARDHOLDER, cardHolderEditText.text.toString())
                intent.putExtra(EXTRA_DOCTOR_USER_ID, doctorUserId)
                startActivity(intent)
            }
        }

    }

    private fun initHtml() {
        val sp1 = getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        val token = sp1.getString(USER_ACCESS_TOKEN, "")
        val userToken = "Bearer $token"

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val html = aboutUsViewModel.getContractOffer(userToken)

                textViewPay.setOnClickListener {
                    val intent =
                        Intent(this@CardPaymentIntroActivity, DocActivity::class.java).apply {
                            putExtra(EXTRA_HTML, html)
                            putExtra(EXTRA_TITLE_DOC, "Условия договора оферты")
                        }
                    startActivity(intent)
                }
            } catch (e: HttpException) {
                Log.d("CardPaymentIntroActivit", "initHtml (line 69): $e")
            }
        }
    }

    private fun checkInputs(): Boolean {
        var ret = true

        if (cardNumberEditText.text.toString()
                .isEmpty() || cardNumberEditText.text.toString().length < 4
        ) {
            ret = false
            cardNumberEditText.error = getString(R.string.cardLengthError2)
        }

        if (cardHolderEditText.text.toString().isEmpty()) {
            ret = false
            cardHolderEditText.error = getString(R.string.requiredField)
        }

        if (!checkBoxPrivacyPay.isChecked) {
            ret = false
            Toast.makeText(
                this,
                getString(R.string.pleaseReadPrivacy2),
                Toast.LENGTH_SHORT
            ).show()
        }

        return ret
    }

}