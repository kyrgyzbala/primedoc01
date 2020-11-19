package kg.kyrgyzcoder.primedoc01.ui.med_card

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.util.EXTRA_CHECK
import kotlinx.android.synthetic.main.activity_receipt.*

class ReceiptActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)

        arrBackReceipt.setOnClickListener {
            onBackPressed()
        }

        val receipt = intent.getStringExtra(EXTRA_CHECK)

        if (receipt.isNullOrEmpty()) {
            emptyReceipt.visibility = View.VISIBLE
            relReceipt.visibility = View.GONE
        } else {
            emptyReceipt.visibility = View.GONE
            relReceipt.visibility = View.VISIBLE

            if (receipt.takeLast(4) == "html") {
                webViewReceipt.loadUrl(receipt)
            } else {
                webViewReceipt.visibility = View.GONE


                Glide.with(this).load(receipt)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            pBarReceipt.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            pBarReceipt.visibility = View.GONE
                            return false
                        }

                    })
                    .error(R.drawable.def_doctor)
                    .into(photoViewerReceipt)
            }
        }

    }
}