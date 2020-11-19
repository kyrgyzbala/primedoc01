package kg.kyrgyzcoder.primedoc01.ui.med_card.medcard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.data.medcard.ModelCard
import kg.kyrgyzcoder.primedoc01.util.EXTRA_EDIT_MED_CARD
import kg.kyrgyzcoder.primedoc01.util.EXTRA_MEDCARD_INFO
import kotlinx.android.synthetic.main.activity_med_card_detail.*

class MedCardDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_med_card_detail)

        arrBackMedCardDetail.setOnClickListener {
            onBackPressed()
        }
        val medCard = intent.getSerializableExtra(EXTRA_MEDCARD_INFO) as ModelCard

        initUI(medCard)

    }

    private fun initUI(medCard: ModelCard) {
        if (!medCard.userPhoto.isNullOrEmpty())
            Glide.with(this).load(medCard.userPhoto).into(imgViewMedCardDetail)
        userNameMedCardDetail.text = medCard.firstName
        userSurnameMedCardDetail.text = medCard.lastName
        userFatherNameMedCardDetail.text = medCard.patronymic
        userBirthdayMedCardDetail.text = medCard.birthDate
        userPhoneNumberMedCardDetail.text = medCard.medCardPhoneNumber

        buttonEditCardDetail.setOnClickListener {
            val intent = Intent(this, AddMedCardActivity::class.java)
            intent.putExtra(EXTRA_EDIT_MED_CARD, "edit")
            intent.putExtra(EXTRA_MEDCARD_INFO, medCard)
            startActivity(intent)
        }
    }

}
