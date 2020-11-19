package kg.kyrgyzcoder.primedoc01.ui.clinic.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.ui.clinic.detail.adapters.DoctorsRecyclerViewAdapter
import kg.kyrgyzcoder.primedoc01.ui.clinic.model.CategoryViewModel
import kg.kyrgyzcoder.primedoc01.ui.clinic.model.CategoryViewModelFactory
import kg.kyrgyzcoder.primedoc01.util.EXTRA_CATEGORY_ID
import kg.kyrgyzcoder.primedoc01.util.EXTRA_DOCTOR_ID
import kg.kyrgyzcoder.primedoc01.util.USER_ACCESS_TOKEN
import kg.kyrgyzcoder.primedoc01.util.USER_CRED
import kotlinx.android.synthetic.main.activity_clinic_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.net.ConnectException

class ClinicDetailActivity : AppCompatActivity(), KodeinAware,
    DoctorsRecyclerViewAdapter.DoctorClickListener {

    override val kodein: Kodein by closestKodein()
    private val categoryViewModelFactory: CategoryViewModelFactory by instance()

    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var adapterD: DoctorsRecyclerViewAdapter
    private var idU: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clinic_detail)

        categoryViewModel =
            ViewModelProviders.of(this, categoryViewModelFactory).get(CategoryViewModel::class.java)

        idU = intent.getIntExtra(EXTRA_CATEGORY_ID, 1)

        arrBackClinicDetail.setOnClickListener {
            onBackPressed()
        }

        val sp = getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        val token = sp.getString(USER_ACCESS_TOKEN, "")
        val userToken = "Bearer $token"

        recyclerViewClinicDetail.setHasFixedSize(true)
        adapterD = DoctorsRecyclerViewAdapter(this)
        recyclerViewClinicDetail.adapter = adapterD
        initUI(idU, userToken)
    }

    private fun initUI(id: Int, token: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val current = categoryViewModel.getCategoryDetail(token, id)

                if (!current.image.isNullOrEmpty())
                    Glide.with(this@ClinicDetailActivity).load(current.image)
                        .into(imgViewBannerClinicDetail)
                positionClinicDetail.text = current.name
                detailWhatHeDoesTextView.text = current.description
                pBarDoctorList.visibility = View.GONE
                adapterD.submitList(current.doctors)
            } catch (e: ConnectException) {

            }
        }
    }

    override fun onClick(position: Int) {
        val current = adapterD.getItemAtPos(position)

        val intent = Intent(this, DoctorDetailActivity::class.java)
        intent.putExtra(EXTRA_CATEGORY_ID, idU)
        intent.putExtra(EXTRA_DOCTOR_ID, current.id)
        startActivity(intent)
    }

}