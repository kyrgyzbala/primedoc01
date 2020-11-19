package kg.kyrgyzcoder.primedoc01.ui.med_card.upcoming

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.ui.med_card.CheckClickListener
import kg.kyrgyzcoder.primedoc01.ui.med_card.ReceiptActivity
import kg.kyrgyzcoder.primedoc01.ui.med_card.model.ProfileViewModel
import kg.kyrgyzcoder.primedoc01.ui.med_card.model.ProfileViewModelFactory
import kg.kyrgyzcoder.primedoc01.util.EXTRA_CHECK
import kg.kyrgyzcoder.primedoc01.util.USER_ACCESS_TOKEN
import kg.kyrgyzcoder.primedoc01.util.USER_CRED
import kg.kyrgyzcoder.primedoc01.util.USER_ID_KEY
import kotlinx.android.synthetic.main.activity_upcoming.*
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

class UpcomingActivity : AppCompatActivity(), KodeinAware, CheckClickListener {


    override val kodein: Kodein by closestKodein()
    private val profileViewModelFactory: ProfileViewModelFactory by instance()

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var adapter: UpcomingRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming)


        arrBackUpcoming.setOnClickListener {
            onBackPressed()
        }

        profileViewModel =
            ViewModelProviders.of(this, profileViewModelFactory).get(ProfileViewModel::class.java)

        val sp = getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        val token = sp.getString(USER_ACCESS_TOKEN, "")
        val userId = sp.getInt(USER_ID_KEY, 1)
        val userToken = "Bearer $token"

        initUi(userId, userToken)

        upcomingSwipeRefresh.setOnRefreshListener {
            initUi(userId, userToken)
        }

    }


    private fun initUi(userId: Int, userToken: String) {

        GlobalScope.launch(Dispatchers.Main) {

            try {
                prBarUpcoming.visibility = View.VISIBLE
                val reservations = profileViewModel.getReservationsUpcomingAsync(userToken, userId)

                adapter = UpcomingRecyclerAdapter(this@UpcomingActivity)
                recyclerViewUpcoming.setHasFixedSize(true)
                recyclerViewUpcoming.adapter = adapter
                upcomingSwipeRefresh.isRefreshing = false
                prBarUpcoming.visibility = View.GONE
                adapter.submitList(reservations)

            } catch (e: ConnectException) {

            } catch (e: SocketTimeoutException) {

            }
        }
    }

    override fun onCheckClick(checkUrl: String?) {
        val intent = Intent(this, ReceiptActivity::class.java)
        intent.putExtra(EXTRA_CHECK, checkUrl)
        startActivity(intent)
    }
}