package kg.kyrgyzcoder.primedoc01.ui.med_card.medcard

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ReturnMode
import kg.kyrgyzcoder.primedoc01.MainActivity
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.data.medcard.ModelCard
import kg.kyrgyzcoder.primedoc01.ui.about.model.AboutUsViewModel
import kg.kyrgyzcoder.primedoc01.ui.about.model.AboutUsViewModelFactory
import kg.kyrgyzcoder.primedoc01.ui.med_card.model.MedCardViewModel
import kg.kyrgyzcoder.primedoc01.ui.med_card.model.MedCardViewModelFactory
import kg.kyrgyzcoder.primedoc01.util.*
import kotlinx.android.synthetic.main.activity_add_med_card.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import retrofit2.HttpException
import java.io.File
import java.net.ConnectException
import java.util.*

class AddMedCardActivity : AppCompatActivity(), KodeinAware, UploadRequestBody.UploadCallback {

    override val kodein: Kodein by closestKodein()
    private val medCardViewModelFactory: MedCardViewModelFactory by instance()
    private val aboutUsViewModelFactory: AboutUsViewModelFactory by instance()

    private lateinit var medCardViewModel: MedCardViewModel
    private lateinit var aboutUsViewModel: AboutUsViewModel

    private var photoUploaded: Boolean = false
    private var selectedImage: File? = null
    private var bYear: Int? = null
    private var bMonth: Int? = null
    private var bDay: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_med_card)

        medCardViewModel =
            ViewModelProviders.of(this, medCardViewModelFactory).get(MedCardViewModel::class.java)

        aboutUsViewModel =
            ViewModelProviders.of(this, aboutUsViewModelFactory).get(AboutUsViewModel::class.java)

        initHtml()
        arrBackMedCardAdd.setOnClickListener {
            onBackPressed()
        }

        val isEdit = intent.getStringExtra(EXTRA_EDIT_MED_CARD)
        if (isEdit != null && isEdit == "edit") {
            photoUploaded = true
            val medCard = intent.getSerializableExtra(EXTRA_MEDCARD_INFO) as ModelCard
            initUi(medCard)
        }

        setClickListeners()

        buttonEditCardAdd.setOnClickListener {
            Log.d("AddMedCardFragment", "onViewCreated (line 76): ")
            if (checkInputs()) {
                saveMedCard()
            }
        }

        imgViewMedCardAdd.setOnClickListener {
            openImageChooser()
        }

    }

    private fun initHtml() {
        val sp1 = getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        val token = sp1.getString(USER_ACCESS_TOKEN, "")
        val userToken = "Bearer $token"


        GlobalScope.launch(Dispatchers.Main) {
            try {
                val html = aboutUsViewModel.getPersonalData(userToken)

                textViewPersonalInfo.setOnClickListener {
                    val intent = Intent(this@AddMedCardActivity, DocActivity::class.java).apply {
                        putExtra(EXTRA_HTML, html)
                        putExtra(EXTRA_TITLE_DOC, "Соглашение на обработку персональных данных")
                    }
                    startActivity(intent)
                }
            } catch (e: HttpException) {
                Log.d("AddMedCardActivity", "initHtml (line 108): $e")
            }
        }
    }

    private fun initUi(medCard: ModelCard) {
        if (!medCard.userPhoto.isNullOrEmpty())
            Glide.with(this).load(medCard.userPhoto).into(imgViewMedCardAdd)

        userSurnameMedCardAdd.setText(medCard.lastName)
        userNameMedCardAdd.setText(medCard.firstName)
        userFatherNameMedCardAdd.setText(medCard.patronymic)
        userBirthdayMedCardAdd.text = medCard.birthDate
        userPhoneNumberMedCardAdd.setText(medCard.medCardPhoneNumber)

        checkBoxPrivacyAddMedCard.visibility = View.GONE
        textViewTextText.visibility = View.GONE
        textViewPersonalInfo.visibility = View.GONE
    }

    private fun openImageChooser() {
        com.esafirm.imagepicker.features.ImagePicker.create(this)
            .returnMode(ReturnMode.ALL)
            .folderMode(true)
            .toolbarFolderTitle(getString(R.string.choosePicture))
            .toolbarArrowColor(Color.parseColor("#FF8E04"))
            .toolbarImageTitle(getString(R.string.choosePicture))
            .includeVideo(false)
            .limit(1)
            .showCamera(true)
            .single()
            .toolbarDoneButtonText(getString(R.string.ok))
            .start(REQUEST_CODE_IMG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMG) {

            val image = com.esafirm.imagepicker.features.ImagePicker.getFirstImageOrNull(data)
            imgViewMedCardAdd.setImageURI(Uri.parse(image.path))
            selectedImage = File(image.path)
            updateProfilePhoto()
        }
    }

    private fun updateProfilePhoto() {
        Log.d("AddMedCardFragment", "updateProfilePhoto (line 69): todo")

        prBarMedCard.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        val sp1 = getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        val token = sp1.getString(USER_ACCESS_TOKEN, "")
        val id = sp1.getInt(USER_ID_KEY, 1)
        val userToken = "Bearer $token"
        val body = UploadRequestBody(selectedImage!!, "image", this@AddMedCardActivity)

        GlobalScope.launch(Dispatchers.Main) {

            val response = medCardViewModel.putUserPhoto(
                userToken, id,
                MultipartBody.Part.createFormData("imageFile", selectedImage!!.name, body)
            )
            if (response.isSuccessful) {
                Toast.makeText(
                    this@AddMedCardActivity,
                    getString(R.string.photoUploadedSuccess),
                    Toast.LENGTH_SHORT
                ).show()
                photoUploaded = true
                prBarMedCard.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                buttonEditCardAdd.setOnClickListener {
                    if (checkInputs()) {
                        saveMedCard()
                    }
                }

            } else {
                Log.d(
                    "AddMedCardFragment",
                    "updateProfilePhoto (line 127): ${response.errorBody()}"
                )
                prBarMedCard.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }

        }
    }


    private fun saveMedCard() {
        val userSurname = userSurnameMedCardAdd.text.toString()
        val userName = userNameMedCardAdd.text.toString()
        val userFatherName = userFatherNameMedCardAdd.text.toString()
        val phone = userPhoneNumberMedCardAdd.text.toString()
        val birthDay = userBirthdayMedCardAdd.text.toString()

        val model = ModelCard(birthDay, userName, userSurname, phone, userFatherName)
        prBarMedCard.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        updateUserMedCardData(model)

    }

    private fun updateUserMedCardData(model: ModelCard) {
        val sp1 = getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        val token = sp1.getString(USER_ACCESS_TOKEN, "")
        val id = sp1.getInt(USER_ID_KEY, 1)
        val userToken = "Bearer $token"

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = medCardViewModel.putUserMedCardData(userToken, id, model)
                if (response.isSuccessful) {
                    prBarMedCard.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    Toast.makeText(
                        this@AddMedCardActivity,
                        getString(R.string.medCardUpdateSuccess),
                        Toast.LENGTH_SHORT
                    ).show()
                    goToMain()
                } else {
                    Toast.makeText(
                        this@AddMedCardActivity,
                        "ERROR: ${response.errorBody()} code: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    prBarMedCard.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }
            } catch (e: ConnectException) {
                Log.d("AddMedCardFragment", "updateUserMedCardData  ${e.message}")
            }
        }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("FROM_EDIT", "true")
        startActivity(intent)
    }

    private fun setClickListeners() {
        userSurnameMedCardAdd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                userSurnameMedCardAdd.error = null
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        userNameMedCardAdd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                userNameMedCardAdd.error = null
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        userBirthdayMedCardAdd.setOnClickListener {
            chooseDate()
        }
    }

    private fun chooseDate() {
        val calendar = Calendar.getInstance()
        val mYear = bYear ?: calendar.get(Calendar.YEAR)
        val mMonth = bMonth ?: calendar.get(Calendar.MONTH)
        val mDay = bDay ?: calendar.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(
            this,
            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            { _, year, month, dayOfMonth ->
                Log.d("AddMedCardFragment", "chooseDate: $year/$month/$dayOfMonth")
                Log.d("AddMedCardFragment", "chooseDate: today: $mYear/$mMonth/$mDay")
                if (mYear < year) {
                    Toast.makeText(
                        this,
                        getString(R.string.chooseProperDate),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (mYear == year && mMonth < (month)) {
                    Toast.makeText(
                        this,
                        getString(R.string.chooseProperDate),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (mYear == year && mMonth == (month) && dayOfMonth > mDay) {
                    Toast.makeText(
                        this,
                        getString(R.string.chooseProperDate),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {

                    bYear = year
                    bMonth = month
                    bDay = dayOfMonth
                    val dom = if (dayOfMonth < 10)
                        "0$dayOfMonth"
                    else
                        "$dayOfMonth"

                    val date = if (month >= 9)
                        "$year-${month + 1}-$dom"
                    else
                        "$year-0${month + 1}-$dom"
                    userBirthdayMedCardAdd.text = date
                }
            },
            mYear, mMonth, mDay
        )
        dialog.setButton(
            DatePickerDialog.BUTTON_POSITIVE,
            getString(R.string.ok),
            dialog
        )
        dialog.setButton(
            DatePickerDialog.BUTTON_NEGATIVE,
            getString(R.string.cancel),
            dialog
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }


    private fun checkInputs(): Boolean {
        var ret = true

        if (userSurnameMedCardAdd.text.toString().isEmpty()) {
            userSurnameMedCardAdd.error = getString(R.string.requiredField)
            ret = false
        }

        if (userNameMedCardAdd.text.toString().isEmpty()) {
            userNameMedCardAdd.error = getString(R.string.requiredField)
            ret = false
        }

        if (userBirthdayMedCardAdd.text.toString() == "" && userBirthdayMedCardAdd.text.toString() != getString(
                R.string.birth_hint
            )
        ) {
            ret = false
            Toast.makeText(
                this,
                getString(R.string.selectBirthDayError),
                Toast.LENGTH_SHORT
            ).show()
        }

        if (!photoUploaded) {
            ret = false
            Toast.makeText(
                this,
                getString(R.string.uploadPhotoError),
                Toast.LENGTH_SHORT
            ).show()
        }

        if (checkBoxPrivacyAddMedCard.visibility == View.VISIBLE && !checkBoxPrivacyAddMedCard.isChecked) {
            ret = false
            Toast.makeText(
                this,
                getString(R.string.pleaseReadPrivacy),
                Toast.LENGTH_SHORT
            ).show()
        }

        return ret

    }

    override fun onProgressUpdate(percentage: Int) {
        Log.d("AddMedCardFragment", "onProgressUpdate (line 162): $percentage")
    }

}