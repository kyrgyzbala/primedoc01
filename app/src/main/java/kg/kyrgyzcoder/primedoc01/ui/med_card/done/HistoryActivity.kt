package kg.kyrgyzcoder.primedoc01.ui.med_card.done

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
import kotlinx.android.synthetic.main.activity_history.*
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

class HistoryActivity : AppCompatActivity(), KodeinAware, CheckClickListener {

    override val kodein: Kodein by closestKodein()
    private val profileViewModelFactory: ProfileViewModelFactory by instance()

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var adapter: DoneRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        arrBackHistory.setOnClickListener {
            onBackPressed()
        }

        profileViewModel =
            ViewModelProviders.of(this, profileViewModelFactory).get(ProfileViewModel::class.java)

        val sp = getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        val token = sp.getString(USER_ACCESS_TOKEN, "")
        val userId = sp.getInt(USER_ID_KEY, 1)
        val userToken = "Bearer $token"

        initUi(userId, userToken)

        historySwipeRefresh.setOnRefreshListener {
            initUi(userId, userToken)
        }

    }

    private fun initUi(userId: Int, userToken: String) {

        GlobalScope.launch(Dispatchers.Main) {


            try {
                prBarHistory?.visibility = View.VISIBLE
                historySwipeRefresh.isRefreshing = false
                val reservations = profileViewModel.getReservationsDoneAsync(userToken, userId)

                prBarHistory?.visibility = View.GONE
                recyclerViewHistory.setHasFixedSize(true)
                adapter = DoneRecyclerViewAdapter(this@HistoryActivity)
                recyclerViewHistory.adapter = adapter

                adapter.submitList(reservations)

            } catch (e: ConnectException) {
                Log.d("HistoryFragment", "initUi (line 70): $e")
            } catch (e: SocketTimeoutException) {
                Log.d("HistoryFragment", "initUi (line 72): $e")
            }
        }
    }

    override fun onCheckClick(checkUrl: String?) {
        val intent = Intent(this, ReceiptActivity::class.java)
        intent.putExtra(EXTRA_CHECK, checkUrl)
        startActivity(intent)
    }
}