package kg.kyrgyzcoder.primedoc01.util

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kg.kyrgyzcoder.primedoc01.R
import kotlinx.android.synthetic.main.activity_doc.*

class DocActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc)

        arrBackDoc.setOnClickListener {
            onBackPressed()
        }

        val html = intent.getStringExtra(EXTRA_HTML)
        val text = intent.getStringExtra(EXTRA_TITLE_DOC)
        titleDocTextView.title = text

        webViewDoc.settings.javaScriptEnabled = true
        webViewDoc.loadData(html!!, "text/html; charset=utf-8", "UTF-8")

    }
}