package kg.kyrgyzcoder.primedoc01

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.iid.FirebaseInstanceId
import io.grpc.InternalChannelz.id
import kg.kyrgyzcoder.primedoc01.data.register.fcmtoken.ModelUpdateToken
import kg.kyrgyzcoder.primedoc01.ui.chat.ReceivingActivity
import kg.kyrgyzcoder.primedoc01.ui.login.model.RegisterViewModel
import kg.kyrgyzcoder.primedoc01.ui.login.model.RegisterViewModelFactory
import kg.kyrgyzcoder.primedoc01.util.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val registerViewModelFactory: RegisterViewModelFactory by instance()

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerViewModel =
            ViewModelProviders.of(this, registerViewModelFactory).get(RegisterViewModel::class.java)

        nav_bottom_view.setOnNavigationItemSelectedListener(this)
        val sectionPagerAdapter = MainPagerAdapter(this)
        view_pager.adapter = sectionPagerAdapter
//        view_pager.isUserInputEnabled = false

        if (intent.hasExtra("FROM_EDIT")) {
            nav_bottom_view.menu.findItem(R.id.medCardNav).isChecked = true
            view_pager.currentItem = 1
        }

        view_pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        nav_bottom_view.menu.findItem(R.id.clinicsNav).isChecked = true
                    }
                    1 -> nav_bottom_view.menu.findItem(R.id.medCardNav).isChecked = true
                    2 -> nav_bottom_view.menu.findItem(R.id.chatNav).isChecked = true
                    3 -> nav_bottom_view.menu.findItem(R.id.faqNav).isChecked = true
                    else -> nav_bottom_view.menu.findItem(R.id.aboutUsNav).isChecked = true
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
        })

        if (intent.hasExtra(EXTRA_FROM_NOTIF)) {
            nav_bottom_view.menu.findItem(R.id.chatNav).isChecked = true
            view_pager.currentItem = R.id.chatNav
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        view_pager.currentItem = when (item.itemId) {
            R.id.clinicsNav -> 0
            R.id.medCardNav -> 1
            R.id.chatNav -> 2
            R.id.faqNav -> 3
            else -> 4
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        val spp = getSharedPreferences(USER_STATUS, Context.MODE_PRIVATE).edit()
        spp.putString(USER_STATUS_KEY, "done")
        spp.apply()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            setUserOnline(user)
            addCallListener(user)
            sendDeviceTokenToServer()
        }
    }

    private fun getUserId(): Int {
        val sp = getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        return sp.getInt(CHAT_ID_KEY, 1)
    }

    private fun sendDeviceTokenToServer() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("MainActivity", "getInstanceId failed", task.exception)
                return@addOnCompleteListener
            }

            val fcmToken = task.result?.token
            val map = mutableMapOf<String, String>()
            map["token"] = fcmToken!!
            FirebaseFirestore.getInstance().collection("fcmTokens").document(getUserId().toString())
                .set(map)
                .addOnSuccessListener {
                    sendToServer(fcmToken)
                }

        }
    }

    private fun sendToServer(fcmToken: String) {

        val sp = getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        val token = sp.getString(USER_ACCESS_TOKEN, "")
        val sp2 = getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE)
        val userPhone = sp2.getString(USER_PHONE, "")
        val userToken = "Bearer $token"

        val map = ModelUpdateToken(fcmToken, userPhone!!)

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = registerViewModel.updateFcmToken(userToken, map)
                if (response.isSuccessful) {
                    Log.d("MainActivity", "sendToServer (line 152): success fcm server")
                } else {
                    Log.d("MainActivity", "sendToServer (line 154): fail $response")
                }
            } catch (e: SocketTimeoutException) {
                Log.d("MainActivity", "sendToServer (line 152): error")
            }
        }

    }

    private fun addCallListener(user: FirebaseUser) {

        val sp = getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        val id = sp.getInt(CHAT_ID_KEY, 1)

        val ref =
            FirebaseFirestore.getInstance().collection("users").document(id.toString())
                .collection("call").document("calling")
        ref.addSnapshotListener { value, _ ->
            if (value != null && value.exists()) {
                val uid = value.getString("uid")
                if (uid != null && uid != "") {
                    val intent = Intent(this, ReceivingActivity::class.java)
                    intent.putExtra(EXTRA_RECEIVE_ID, uid)
                    startActivity(intent)
                }
            }
        }
    }

    private fun setUserOnline(user: FirebaseUser) {
        val db = FirebaseFirestore.getInstance()
        val map = mutableMapOf<String, Any>()
        map["isOnline"] = true
        db.collection("users").document(user.uid).set(map, SetOptions.merge())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(
                        "PinConfirmActivity",
                        "signInFirebase (line 207): success"
                    )
                }
            }
    }

}