package kg.kyrgyzcoder.primedoc01.ui.clinic.detail

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.data.clinics.model.reservation.ModelMakeReservation
import kg.kyrgyzcoder.primedoc01.data.clinics.model.worktime.Reservation
import kg.kyrgyzcoder.primedoc01.ui.clinic.detail.payment.PaymentOptionsActivity
import kg.kyrgyzcoder.primedoc01.ui.clinic.model.CategoryViewModel
import kg.kyrgyzcoder.primedoc01.ui.clinic.model.CategoryViewModelFactory
import kg.kyrgyzcoder.primedoc01.util.*
import kotlinx.android.synthetic.main.activity_appointment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.net.ConnectException

class AppointmentActivity : AppCompatActivity(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val categoryViewModelFactory: CategoryViewModelFactory by instance()

    private lateinit var categoryViewModel: CategoryViewModel

    private var reservation: Reservation? = null
    private var doctorUserId: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)

        categoryViewModel =
            ViewModelProviders.of(this, categoryViewModelFactory).get(CategoryViewModel::class.java)

        reservation = intent.getSerializableExtra(EXTRA_RESERVATION) as Reservation
        val workId = intent.getIntExtra(EXTRA_WORK_ID, 1)
        doctorUserId = intent.getIntExtra(EXTRA_DOCTOR_USER_ID, 1)
        arrBackAppointment.setOnClickListener {
            onBackPressed()
        }

        val sp = getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        val userId = sp.getInt(USER_ID_KEY, 1)
        val token = sp.getString(USER_ACCESS_TOKEN, "")
        val userToken = "Bearer $token"

        buttonBookAppointment.setOnClickListener {
            if (phoneNumberEditTextAppointment.text.toString().isEmpty()) {
                phoneNumberEditTextAppointment.error = getString(R.string.requiredField)
            } else {
                hideKeyboard()
                makeReservation(workId, userId, userToken)
            }
        }

    }

    private fun makeReservation(workId: Int, userId: Int, userToken: String) {
        pBarAppointment.visibility = View.VISIBLE
        val model = ModelMakeReservation(
            userId,
            commentEditTextAppointment.text.toString(),
            reservation?.end!!,
            phoneNumberEditTextAppointment.text.toString(),
            reservation?.start!!,
            workId
        )
        GlobalScope.launch(Dispatchers.Main) {


            try {
                val response = categoryViewModel.makeReservation(userToken, model)
                if (response.isSuccessful) {
                    val modelR = response.body()
                    val last = modelR?.reservation!!.lastIndex
                    val resId = modelR.reservation[last].id
                    val intent =
                        Intent(this@AppointmentActivity, PaymentOptionsActivity::class.java)
                    intent.putExtra(EXTRA_RES_ID, resId)
                    intent.putExtra(EXTRA_DOCTOR_USER_ID, doctorUserId)
                    startActivity(intent)
                } else {
                    Snackbar.make(
                        appointmentMain,
                        getString(R.string.errorTryAgain),
                        Snackbar.LENGTH_LONG
                    )
                        .setTextColor(Color.parseColor("#FFFFFF"))
                        .show()
                }
            } catch (e: ConnectException) {
                Snackbar.make(
                    appointmentMain,
                    getString(R.string.errorTryAgain),
                    Snackbar.LENGTH_LONG
                )
                    .setTextColor(Color.parseColor("#FFFFFF"))
                    .show()
            }
        }
    }

}