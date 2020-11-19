package kg.kyrgyzcoder.primedoc01.ui.clinic

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.ui.clinic.detail.ClinicDetailActivity
import kg.kyrgyzcoder.primedoc01.ui.clinic.model.CategoryViewModel
import kg.kyrgyzcoder.primedoc01.ui.clinic.model.CategoryViewModelFactory
import kg.kyrgyzcoder.primedoc01.util.EXTRA_CATEGORY_ID
import kg.kyrgyzcoder.primedoc01.util.ScopedFragment
import kg.kyrgyzcoder.primedoc01.util.USER_ACCESS_TOKEN
import kg.kyrgyzcoder.primedoc01.util.USER_CRED
import kotlinx.android.synthetic.main.fragment_clinic.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class ClinicFragment : ScopedFragment(), KodeinAware,
    CategoryRecyclerViewAdapter.ClinicClickListener {

    override val kodein: Kodein by closestKodein()
    private val categoryViewModelFactory: CategoryViewModelFactory by instance()

    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var adapterC: CategoryRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clinic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryViewModel =
            ViewModelProviders.of(this, categoryViewModelFactory).get(CategoryViewModel::class.java)

        val sp = requireActivity().getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        val token = sp.getString(USER_ACCESS_TOKEN, "")
        val userToken = "Bearer $token"

        recyclerViewClinics.setHasFixedSize(true)
        adapterC = CategoryRecyclerViewAdapter(this)
        recyclerViewClinics.adapter = adapterC
        initList(userToken)

    }

    private fun initList(userToken: String) = launch {
        val listCategories = categoryViewModel.getCategories(userToken)

        pBarClinicList.visibility = View.GONE
        adapterC.submitList(listCategories)
    }

    override fun onClick(position: Int) {
        val current = adapterC.getItemAtPos(position)

        val intent = Intent(requireContext(), ClinicDetailActivity::class.java)
        intent.putExtra(EXTRA_CATEGORY_ID, current.id)
        startActivity(intent)
    }

}