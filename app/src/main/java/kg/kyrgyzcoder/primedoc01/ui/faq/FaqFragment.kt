package kg.kyrgyzcoder.primedoc01.ui.faq

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.ui.faq.model.FaqViewModel
import kg.kyrgyzcoder.primedoc01.ui.faq.model.FaqViewModelFactory
import kg.kyrgyzcoder.primedoc01.util.ScopedFragment
import kg.kyrgyzcoder.primedoc01.util.USER_ACCESS_TOKEN
import kg.kyrgyzcoder.primedoc01.util.USER_CRED
import kotlinx.android.synthetic.main.fragment_faq.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.net.ConnectException


class FaqFragment : ScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val faqViewModelFactory: FaqViewModelFactory by instance()

    private lateinit var faqViewModel: FaqViewModel
    private lateinit var adapterF: FaqRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faq, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        faqViewModel =
            ViewModelProviders.of(this, faqViewModelFactory).get(FaqViewModel::class.java)

        val sp = requireActivity().getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        val token = sp.getString(USER_ACCESS_TOKEN, "")
        val userToken = "Bearer $token"

        initUI(userToken)

    }

    private fun initUI(userToken: String) = launch {

        try {
            val current = faqViewModel.getFaqList(userToken)

            Log.d("FaqFragment", "initUI (line 62): $current")

            adapterF = FaqRecyclerViewAdapter()
            recyclerVewFaq.adapter = adapterF

            adapterF.submitList(current)
            pBarFaq.visibility = View.GONE

        } catch (e: ConnectException) {

        }

    }

}