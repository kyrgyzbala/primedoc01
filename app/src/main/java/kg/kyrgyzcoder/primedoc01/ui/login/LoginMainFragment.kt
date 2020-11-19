package kg.kyrgyzcoder.primedoc01.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.data.register.ModelRegister
import kg.kyrgyzcoder.primedoc01.ui.login.model.RegisterViewModel
import kg.kyrgyzcoder.primedoc01.ui.login.model.RegisterViewModelFactory
import kg.kyrgyzcoder.primedoc01.util.*
import kotlinx.android.synthetic.main.fragment_login_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.io.EOFException
import java.net.ConnectException
import java.net.SocketTimeoutException


class LoginMainFragment : ScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val registerViewModelFactory: RegisterViewModelFactory by instance()

    private lateinit var registerViewModel: RegisterViewModel

    private var snack: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ccp.registerCarrierNumberEditText(phoneNumberEditText)

        registerViewModel =
            ViewModelProviders.of(this, registerViewModelFactory).get(RegisterViewModel::class.java)

        if (requireActivity().intent.hasExtra(EXTRA_FROM_PIN)) {
            handleFromPin()
        }


        phoneNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                snack?.dismiss()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        registerButton.setOnClickListener {
            if (checkPhoneNumber())
                registerWithPhoneNumber()
        }

    }

    private fun handleFromPin() {
        textView2.text = getString(R.string.recoverPin)
        registerButton.text = getString(R.string.getCode)
    }

    private fun registerWithPhoneNumber() = launch {
        val phone = ccp.fullNumberWithPlus
        val pwd = getDeviceId(requireContext())
        val model = ModelRegister(listOf("USER"), pwd, phone)

        try {
            pBarLoginMain.visibility = View.VISIBLE
            noInternet.visibility = View.GONE
            val response = registerViewModel.registerNewUser(model)
            if (response.isSuccessful) {
                Log.d("LoginMainFragment", "registerWithPhoneNumber (line 74): reg success")
                val bundle = Bundle()
                bundle.putString(EXTRA_USER_PHONE, phone)
                bundle.putString(EXTRA_USER_PWD, pwd)
                bundle.putString(EXTRA_CODE_TYPE, "reg")
                findNavController().navigate(
                    R.id.action_loginMainFragment_to_codeConfirmationFragment,
                    bundle
                )
            } else {
                pBarLoginMain.visibility = View.VISIBLE
                GlobalScope.launch(Dispatchers.Main) {
                    val response2 = registerViewModel.recoverPwd(phone)
                    if (response2.isSuccessful) {
                        val bundle = Bundle()
                        bundle.putString(EXTRA_USER_PHONE, phone)
                        bundle.putString(EXTRA_USER_PWD, pwd)
                        bundle.putString(EXTRA_CODE_TYPE, "rec")
                        findNavController().navigate(
                            R.id.action_loginMainFragment_to_codeConfirmationFragment,
                            bundle
                        )
                    }
                    pBarLoginMain?.visibility = View.GONE
                }
            }
        } catch (e: ConnectException) {

        } catch (e: SocketTimeoutException) {
            prBarLogin.visibility = View.GONE
            noInternet.visibility = View.VISIBLE
            Snackbar.make(requireView(), "У вас слабый интернет", Snackbar.LENGTH_LONG)
                .setAction("Повторить попытку") {
                    requireActivity().recreate()
                }
                .setDuration(3000)
                .setActionTextColor(Color.parseColor("#DEF404"))
                .setTextColor(Color.parseColor("#00BDD0"))
                .show()
        } catch (e: EOFException) {
            val bundle = Bundle()
            bundle.putString(EXTRA_USER_PHONE, phone)
            bundle.putString(EXTRA_USER_PWD, pwd)
            findNavController().navigate(
                R.id.action_loginMainFragment_to_codeConfirmationFragment,
                bundle
            )
        }

    }


    private fun checkPhoneNumber(): Boolean {
        if (phoneNumberEditText.text.toString().length < 9) {
            phoneNumberEditText.error = getString(R.string.phoneNumberFormatError)
            return false
        }
        return true
    }

    // Device hard-coded id
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }


}