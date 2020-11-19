package kg.kyrgyzcoder.primedoc01.ui.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.ui.about.model.AboutUsViewModel
import kg.kyrgyzcoder.primedoc01.ui.about.model.AboutUsViewModelFactory
import kg.kyrgyzcoder.primedoc01.util.*
import kotlinx.android.synthetic.main.fragment_about_us.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import retrofit2.HttpException
import java.net.SocketTimeoutException


class AboutUsFragment : ScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val aboutUsViewModelFactory: AboutUsViewModelFactory by instance()

    private lateinit var aboutUsViewModel: AboutUsViewModel

    private var html = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        aboutUsViewModel =
            ViewModelProviders.of(this, aboutUsViewModelFactory).get(AboutUsViewModel::class.java)

        val sp = requireActivity().getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        val token = sp.getString(USER_ACCESS_TOKEN, "")
        val userToken = "Bearer $token"

        bindUI(userToken)
        initHtml(userToken)

    }

    private fun initHtml(userToken: String) = launch {
        Log.d("AboutUsFragment", "initHtml (line 60): ")
        try {
            html = aboutUsViewModel.getPaidService(userToken)
            textViewAgreement?.setOnClickListener {
                val intent = Intent(requireContext(), DocActivity::class.java).apply {
                    putExtra(EXTRA_HTML, html)
                    putExtra(EXTRA_TITLE_DOC, textViewAgreement.text.toString())
                }
                startActivity(intent)
            }
        } catch (e: HttpException) {
            Log.d("AboutUsFragment", "initHtml (line 70): ")
        } catch (e: SocketTimeoutException) {

        }
    }

    private fun bindUI(userToken: String) = launch {
        try {
            val aboutUs = aboutUsViewModel.getAboutUs(token = userToken)

            pBarAboutUs?.visibility = View.GONE
            for (a in aboutUs) {
                when (a.order) {
                    1 -> {
                        textViewWhatsPrimeDoc.text = a.header
                        textViewAboutPrimeDoc.text = a.paragraph
                    }
                    2 -> {
                        textViewOurContacts.text = a.header
                        textViewPhoneNumbers.text = a.paragraph
                    }
                    3 -> {
                        textViewAddressHeader.text = a.header
                        textViewAddress.text = a.paragraph
                    }
                }
            }
        } catch (e: HttpException) {

        } catch (e: SocketTimeoutException) {

        }


    }

}