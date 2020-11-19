package kg.kyrgyzcoder.primedoc01.ui.chat

import android.Manifest
import android.content.Intent
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.opentok.android.*
import kg.kyrgyzcoder.primedoc01.MainActivity
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.util.EXTRA_CALL_REF
import kg.kyrgyzcoder.primedoc01.util.EXTRA_DOC_NAME
import kotlinx.android.synthetic.main.activity_call_actual.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class CallActualActivity : AppCompatActivity(), Session.SessionListener,
    PublisherKit.PublisherListener {

    companion object {
        private const val API_KEY = "46923824"
        private var SESSION_ID =
            "2_MX40NjkyMzgyNH5-MTYwMDMyODU0NTY0OH5INDhFcHlIcjEwbGF6MEk0ZTV6SHRKRzB-fg"
        private var TOKEN =
            "T1==cGFydG5lcl9pZD00NjkyMzgyNCZzaWc9N2YwMjljNjc5YjE5YjIyZWEwN2FmMTBhYmYwNWEwZjliOGMyZDVkNTpzZXNzaW9uX2lkPTJfTVg0ME5qa3lNemd5Tkg1LU1UWXdNRE15T0RVME5UWTBPSDVJTkRoRmNIbEljakV3YkdGNk1FazBaVFY2U0hSS1J6Qi1mZyZjcmVhdGVfdGltZT0xNjAwMzI4NjI2Jm5vbmNlPTAuMjIzNzU2MDQ0NjYwODE0Njcmcm9sZT1wdWJsaXNoZXImZXhwaXJlX3RpbWU9MTYwMjkyMDYyNSZpbml0aWFsX2xheW91dF9jbGFzc19saXN0PQ=="
        private const val TAG = "CallingActivity"
        private const val RC_VIDEO_APP_PERM = 124
    }

    private lateinit var ref: DocumentReference

    private lateinit var mSession: Session
    private var mPublisher: Publisher? = null
    private var mSubscriber: Subscriber? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_actual)
        initToken()
    }

    private fun initToken() {
        val db = FirebaseFirestore.getInstance()
        db.collection("videoChatToken").document("token").get().addOnSuccessListener {
            TOKEN = it.getString("token")!!
            SESSION_ID = it.getString("session")!!
            val refStr = intent.getStringExtra(EXTRA_CALL_REF)
            val userName = intent.getStringExtra(EXTRA_DOC_NAME)
            userNameCallActual.text = userName
            ref = FirebaseFirestore.getInstance().document(refStr!!)
            addListener()
            cancelCallButton.setOnClickListener {
                endCall()
            }
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private fun requestPermissions() {

        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.INTERNET,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
        ) {

            mSession = Session.Builder(this, API_KEY, SESSION_ID).build()
            mSession.setSessionListener(this)
            mSession.connect(TOKEN)
        } else {
            EasyPermissions.requestPermissions(
                this, "CAMERA, AUDIO", RC_VIDEO_APP_PERM, Manifest.permission.INTERNET,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
        }

    }

    private fun endCall() {
        val map = mutableMapOf<String, Any>()
        map["accepted"] = false
        map["declined"] = false
        map["uid"] = ""
        map["receiverId"] = ""
        ref.set(map, SetOptions.merge()).addOnSuccessListener {
            goBack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val map = mutableMapOf<String, Any>()
        map["accepted"] = false
        map["declined"] = false
        map["uid"] = ""
        map["receiverId"] = ""
        mSubscriber?.destroy()
        mPublisher?.destroy()
    }

    private fun goBack() {
        mSubscriber?.destroy()
        mPublisher?.destroy()
        Toast.makeText(this, "Call ended", Toast.LENGTH_LONG).show()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
        startActivity(intent)
    }

    private fun addListener() {
        ref.addSnapshotListener { value, error ->

            if (error != null) {
                Log.d("CallActualActivity", "addListener (line 41): $error")
                goBack()
            }

            if (value != null && value.exists()) {
                val uid = value.getString("uid")
                if (uid.isNullOrEmpty()) {
                    goBack()
                }
            }
        }
    }

    override fun onConnected(p0: Session?) {
        Log.d("CallActualActivity", "onConnected (line 119): ")
        mPublisher = Publisher.Builder(this).build()
        mPublisher?.setPublisherListener(this)
        smallContainer.addView(mPublisher?.view)

        if (mPublisher?.view is GLSurfaceView) {
            (mPublisher?.view as GLSurfaceView).setZOrderOnTop(true)
        }

        mSession.publish(mPublisher)
    }

    override fun onDisconnected(p0: Session?) {
        Log.d("CallActualActivity", "onDisconnected (line 136): ")
    }

    override fun onStreamReceived(p0: Session?, p1: Stream?) {

        Log.d("CallActualActivity", "onStreamReceived (line 139): ")

        if (mSubscriber == null) {
            mSubscriber = Subscriber.Builder(this, p1!!).build()
            mSession.subscribe(mSubscriber)
            largeContainer.addView(mSubscriber!!.view)
        }
    }

    override fun onStreamDropped(p0: Session?, p1: Stream?) {
        Log.d("CallActualActivity", "onStreamDropped (line 150): ")
        if (mSubscriber != null) {
            mSubscriber = null
            largeContainer.removeAllViews()
        }
    }

    override fun onError(p0: Session?, p1: OpentokError?) {
        Toast.makeText(this, "Error $p1", Toast.LENGTH_SHORT).show()
        Log.d("CallActualActivity", "onError (line 160): $p1")
    }

    override fun onStreamCreated(p0: PublisherKit?, p1: Stream?) {
        Log.d("CallActualActivity", "onStreamCreated (line 164): ")
    }

    override fun onStreamDestroyed(p0: PublisherKit?, p1: Stream?) {
        Log.d("CallActualActivity", "onStreamDestroyed (line 168): ")
    }

    override fun onError(p0: PublisherKit?, p1: OpentokError?) {
        Log.d("CallActualActivity", "onError (line 172): ")
    }
}