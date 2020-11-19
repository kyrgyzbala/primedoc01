package kg.kyrgyzcoder.primedoc01.ui.clinic.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.data.clinics.model.worktime.ModelWorkTime
import kg.kyrgyzcoder.primedoc01.data.clinics.model.worktime.Reservation
import kg.kyrgyzcoder.primedoc01.ui.clinic.detail.adapters.EducationRecyclerViewAdapter
import kg.kyrgyzcoder.primedoc01.ui.clinic.detail.bottom.CalendarBottomView
import kg.kyrgyzcoder.primedoc01.ui.clinic.detail.bottom.WorkTimes
import kg.kyrgyzcoder.primedoc01.ui.clinic.model.CategoryViewModel
import kg.kyrgyzcoder.primedoc01.ui.clinic.model.CategoryViewModelFactory
import kg.kyrgyzcoder.primedoc01.util.*
import kotlinx.android.synthetic.main.activity_doctor_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import retrofit2.HttpException
import java.net.ConnectException
import java.text.SimpleDateFormat
import java.util.*

class DoctorDetailActivity : AppCompatActivity(), KodeinAware,
    CalendarBottomView.CalendarBottomClickListener, WorkTimes.WorkTimeClickListener {

    override val kodein: Kodein by closestKodein()
    private val categoryViewModelFactory: CategoryViewModelFactory by instance()

    private lateinit var categoryViewModel: CategoryViewModel
    private var catId: Int = 1
    private var docId: Int = 1
    private var doctorUserId: Int = 1
    private lateinit var adapterD: EducationRecyclerViewAdapter
    private var userToken: String = ""
    private var workTime: ModelWorkTime? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_detail)


        categoryViewModel =
            ViewModelProviders.of(this, categoryViewModelFactory).get(CategoryViewModel::class.java)

        catId = intent.getIntExtra(EXTRA_CATEGORY_ID, 1)
        docId = intent.getIntExtra(EXTRA_DOCTOR_ID, 1)

        val sp = getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        val token = sp.getString(USER_ACCESS_TOKEN, "")
        userToken = "Bearer $token"

        arrBackDoctorDetail.setOnClickListener {
            onBackPressed()
        }

        initUI(userToken)
    }


    private fun initUI(userToken: String) {

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val current = categoryViewModel.getDoctorInfo(userToken, docId)
                doctorUserId = current.user_id
                val edit = getSharedPreferences(DOCTOR_DATA, Context.MODE_PRIVATE).edit()
                edit.putString(DOCTOR_NAME_KEY, current.firstName)
                edit.putString(DOCTOR_SURNAME_KEY, current.lastName)
                edit.apply()
                loadWork()
                val fullName = "${current.lastName} ${current.firstName} ${current.patronymic}"
                doctorNameTextViewDoctor.text = fullName
                if (!current.image.isNullOrEmpty())
                    Glide.with(this@DoctorDetailActivity).load(current.image)
                        .into(imgViewAvatarDoctor)
                textViewDoctorCateg.text = current.position
                textViewDoctorExperience.text = current.bio ?: "-"
                aboutDoctorDescTextView.text = current.bio ?: "-"
                pBarDoctorDetail.visibility = View.GONE
                recyclerViewDoctorEdu.setHasFixedSize(true)
                adapterD = EducationRecyclerViewAdapter()
                recyclerViewDoctorEdu.adapter = adapterD

                reserveButtonDoc.setOnClickListener {
                    makeReservation()
                }
                adapterD.submitList(current.information)

            } catch (e: ConnectException) {

            } catch (e: HttpException) {
                Toast.makeText(this@DoctorDetailActivity, "Error $e", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadWork() {

        GlobalScope.launch(Dispatchers.Main) {
            try {
                workTime = categoryViewModel.getDoctorWorkTime(userToken, docId)
                if (!workTime.isNullOrEmpty()) {
                    for ((i, w) in workTime!!.withIndex()) {
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
                        val dateFormat =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.ROOT)
                        val format = SimpleDateFormat("HH:mm:ss", Locale.ROOT)
                        val today = sdf.format(Calendar.getInstance().time)
                        val nowTime = format.format(Calendar.getInstance().time).take(2)
                        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                        val start = dateFormat.parse(w.start)!!
                        val end = dateFormat.parse(w.end)!!

                        workTime!![i].start = sdf.format(start)
                        workTime!![i].end = sdf.format(end)
                        if (workTime!![i].start == today && workTime!![i].reservation.isNotEmpty()) {
                            try {
                                for (res in workTime!![i].reservation) {
                                    if (res.start?.take(2)?.toInt()!! < (nowTime.toInt() + 1))
                                        workTime!![i].reservation.remove(res)
                                }
                            }catch (e: ConcurrentModificationException){

                            }

                        }
                        Log.d(
                            "DoctorDetailActivity",
                            "loadWork (line 124): start2: ${workTime!![i].start} end2: ${workTime!![i].end}"
                        )
                    }
                }
            } catch (e: ConnectException) {
                Log.d("DoctorDetailFragment", "loadWork (line 115): Connection error ${e.message}")
            } catch (e: HttpException) {
                Log.d("DoctorDetailFragment", "loadWork (line 116): ${e.message()}")
            }
        }
    }

    private fun makeReservation() {
        Log.d("DoctorDetailActivity", "makeReservation (line 125): $workTime")
        if (workTime != null && workTime!!.size > 0) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
            val start = dateFormat.parse(getStart())!!

            Log.d("DoctorDetailActivity", "makeReservation (line 132): start $start")
            val count = getCount(start)

            val dialog = CalendarBottomView(this, start, count)
            dialog.show(supportFragmentManager, "calendar")
        } else {
            Toast.makeText(
                this,
                "У данного доктора сейчас нет свободного времени",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun getStart(): String {
        for (w in workTime!!) {
            if (w.reservation.isNotEmpty())
                return w.start
        }
        return workTime!![0].start
    }

    private fun getCount(start: Date): Int {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)

        val calStart = Calendar.getInstance()
        calStart.time = start

        val calEnd = Calendar.getInstance()
        calEnd.time = dateFormat.parse(workTime!!.last().end)!!

        val ret = calEnd.get(Calendar.DAY_OF_MONTH) - calStart.get(Calendar.DAY_OF_MONTH) + 1

        return if (ret < 0)
            ret * -1
        else
            ret
    }

    override fun onClickOk(date: String, datee: String) {
        getWorkTimeOfDoctor(date, datee)
    }

    private fun getWorkTimeOfDoctor(date: String, datee: String) {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
        val dateD = sdf.parse(date)
        val calendar = Calendar.getInstance()
        calendar.time = dateD!!
        val year = calendar.get(Calendar.YEAR)
        val res = mutableListOf<Reservation>()
        for (w in workTime!!) {
            if (w.start.take(10) == date) {
                for (r in w.reservation)
                    res.add(r)
            }
        }

        val dialog = WorkTimes(res, year, datee, this)
        dialog.show(supportFragmentManager, "time")

    }

    override fun onClickCancel() {
        Toast.makeText(this, "Отменено!", Toast.LENGTH_SHORT).show()
    }

    override fun onOkClick(selected: Reservation) {
        val intent = Intent(this, AppointmentActivity::class.java)
        intent.putExtra(EXTRA_RESERVATION, selected)
        val workId = getWorkId(selected)
        intent.putExtra(EXTRA_WORK_ID, workId)
        intent.putExtra(EXTRA_DOCTOR_ID, docId)
        intent.putExtra(EXTRA_CATEGORY_ID, catId)
        intent.putExtra(EXTRA_DOCTOR_USER_ID, doctorUserId)

        startActivity(intent)
    }

    private fun getWorkId(selected: Reservation): Int {
        for (work in workTime!!) {
            if (work.reservation.contains(selected)) {
                return work.id
            }
        }
        return workTime!![0].id
    }

    override fun onCancelClick() {
    }

    override fun onBackClick() {
        makeReservation()
    }

}