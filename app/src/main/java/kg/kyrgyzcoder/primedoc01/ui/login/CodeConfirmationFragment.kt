package kg.kyrgyzcoder.primedoc01.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.data.register.ModelRegister
import kg.kyrgyzcoder.primedoc01.data.register.ModelResetPwd
import kg.kyrgyzcoder.primedoc01.ui.login.model.RegisterViewModel
import kg.kyrgyzcoder.primedoc01.ui.login.model.RegisterViewModelFactory
import kg.kyrgyzcoder.primedoc01.util.*
import kotlinx.android.synthetic.main.fragment_code_confirmation.*
import kotlinx.android.synthetic.main.fragment_login_main.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.io.EOFException


class CodeConfirmationFragment : ScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val registerViewModelFactory: RegisterViewModelFactory by instance()

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_code_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerViewModel =
            ViewModelProviders.of(this, registerViewModelFactory).get(RegisterViewModel::class.java)

        val bundle = arguments

        val phoneNumber = bundle?.getString(EXTRA_USER_PHONE)
        val pwd = bundle?.getString(EXTRA_USER_PWD)
        val type = bundle?.getString(EXTRA_CODE_TYPE)
        Log.d("CodeConfirmationFragmen", "onViewCreated (line 56): $phoneNumber $pwd $type")
        textViewCodeSentNumber.text = phoneNumber

        nextButtonCodeConfirm.setOnClickListener {
            Log.d("CodeConfirmationFragmen", "onViewCreated (line 62): $type")
            if (codeConfirmationEditText.text.toString().isEmpty()) {
                codeConfirmationEditText.error = getString(R.string.requiredField)
            } else {
                if (type == "rec")
                    resetPwd(phoneNumber)
                else
                    verifyCode(pwd, phoneNumber)
            }
        }

        codeNotReceivedTextView.setOnClickListener {
            if (type == "rec")
                sendRecCode(phoneNumber!!)
            else
                sendRegCode(phoneNumber!!)
        }

    }

    private fun sendRecCode(phoneNumber: String) = launch {

        val response = registerViewModel.recoverPwd(phoneNumber)

        if (response.isSuccessful) {
            Toast.makeText(requireContext(), "Код отправлен заново", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                requireContext(),
                "Ошибка ${response.message()}, code: ${response.code()}",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun sendRegCode(phoneNumber: String) = launch {
        val pwd = getDeviceId(requireContext())
        val model = ModelRegister(listOf("USER"), pwd, phoneNumber)

        val response = registerViewModel.registerNewUser(model)
        if (response.isSuccessful) {
            Toast.makeText(requireContext(), "Код отправлен заново", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                requireContext(),
                "Ошибка ${response.message()}, code: ${response.code()}",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    // Device hard-coded id
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    private fun resetPwd(phoneNumber: String?) = launch {
        pBarCodeConfirm.visibility = View.VISIBLE
        val pwd = getDeviceId(requireContext())
        val modelResetPwd = ModelResetPwd(
            password = pwd,
            confirmPassword = pwd,
            token = codeConfirmationEditText.text.toString()
        )
        val response = registerViewModel.resetPwd(modelResetPwd)
        pBarCodeConfirm.visibility = View.GONE

        if (response.isSuccessful) {
            val editorSp =
                requireActivity().getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE)
                    .edit()
            editorSp.putString(USER_PHONE, phoneNumber)
            editorSp.putString(USER_PWD, pwd)
            editorSp.apply()
            findNavController().navigate(R.id.action_codeConfirmationFragment_to_pinOneFragment)
        } else {
            Snackbar.make(requireView(), "Код неверный", Snackbar.LENGTH_LONG)
                .setAction("Отправить код заново?") {
                    codeConfirmationEditText.setText("")
                    sendRecCode(phoneNumber!!)
                }
                .setDuration(5000)
                .setActionTextColor(Color.parseColor("#DEF404"))
                .setTextColor(Color.parseColor("#00BDD0"))
                .show()
        }

    }

    private fun verifyCode(pwd: String?, phoneNumber: String?) = launch {
        pBarCodeConfirm.visibility = View.VISIBLE

        try {
            val response =
                registerViewModel.verifyPhoneNumber(codeConfirmationEditText.text.toString())

            pBarCodeConfirm.visibility = View.GONE

            if (response.isSuccessful) {
                val editorSp =
                    requireActivity().getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE)
                        .edit()
                editorSp.putString(USER_PHONE, phoneNumber)
                editorSp.putString(USER_PWD, pwd)
                editorSp.apply()
                findNavController().navigate(R.id.action_codeConfirmationFragment_to_pinOneFragment)
            } else {
                Snackbar.make(requireView(), "Код неверный", Snackbar.LENGTH_LONG)
                    .setAction("Отправить код заново?") {
                        codeConfirmationEditText.setText("")
                        sendRegCode(phoneNumber!!)
                    }
                    .setDuration(5000)
                    .setActionTextColor(Color.parseColor("#DEF404"))
                    .setTextColor(Color.parseColor("#00BDD0"))
                    .show()
            }
        } catch (e: EOFException) {
            pBarCodeConfirm?.visibility = View.GONE
            val editorSp =
                requireActivity().getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE)
                    .edit()
            editorSp.putString(USER_PHONE, phoneNumber)
            editorSp.putString(USER_PWD, pwd)
            editorSp.apply()
            findNavController().navigate(R.id.action_codeConfirmationFragment_to_pinOneFragment)
        }

    }
}