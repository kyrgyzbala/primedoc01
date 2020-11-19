package kg.kyrgyzcoder.primedoc01.ui.med_card.medcard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kg.kyrgyzcoder.primedoc01.R
import kg.kyrgyzcoder.primedoc01.data.medcard.ModelCard
import kg.kyrgyzcoder.primedoc01.ui.med_card.confirmed.ConfirmedActivity
import kg.kyrgyzcoder.primedoc01.ui.med_card.done.HistoryActivity
import kg.kyrgyzcoder.primedoc01.ui.med_card.model.MedCardViewModel
import kg.kyrgyzcoder.primedoc01.ui.med_card.model.MedCardViewModelFactory
import kg.kyrgyzcoder.primedoc01.ui.med_card.upcoming.UpcomingActivity
import kg.kyrgyzcoder.primedoc01.util.*
import kotlinx.android.synthetic.main.fragment_med_card_pr.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException


class MedCardPrFragment : ScopedFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val medCardViewModelFactory: MedCardViewModelFactory by instance()

    private lateinit var medCardViewModel: MedCardViewModel

    private var modelMedCardUser: ModelCard? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_med_card_pr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        medCardViewModel =
            ViewModelProviders.of(this, medCardViewModelFactory).get(MedCardViewModel::class.java)

        addMedCard.setOnClickListener {
            val intent = Intent(requireContext(), AddMedCardActivity::class.java)
            intent.putExtra(EXTRA_EDIT_MED_CARD, "add")
            startActivity(intent)
        }
        val sp = requireActivity().getSharedPreferences(USER_CRED, Context.MODE_PRIVATE)
        val token = sp.getString(USER_ACCESS_TOKEN, "")
        val userId = sp.getInt(USER_ID_KEY, 1)
        val userToken = "Bearer $token"
        loadMedCard(userToken, userId)

        historyTextView.setOnClickListener {
            startActivity(Intent(requireContext(), HistoryActivity::class.java))
        }

        upcomingTextView.setOnClickListener {
            startActivity(Intent(requireContext(), UpcomingActivity::class.java))
        }

        confirmedTextView.setOnClickListener {
            startActivity(Intent(requireContext(), ConfirmedActivity::class.java))
        }

    }

    private fun loadMedCard(userToken: String, userId: Int) = launch {
        try {

            val photo = medCardViewModel.getUserPhoto(userToken, userId)

            if (!photo.image.isNullOrEmpty()) {
                relAddMedCardAdd.visibility = View.GONE
                relativeMedCardExits.visibility = View.VISIBLE
                Glide.with(requireActivity()).load(photo.image).into(imgViewMedCardPr)
                loadMedCardInfo(userToken, userId, photo.image)
            } else {
                relAddMedCardAdd.visibility = View.VISIBLE
                relativeMedCardExits.visibility = View.GONE
            }
        } catch (e: ConnectException) {

        } catch (e: SocketTimeoutException) {

        } catch (e: HttpException) {
            relAddMedCardAdd.visibility = View.VISIBLE
            relativeMedCardExits.visibility = View.GONE
        }
    }

    private fun loadMedCardInfo(userToken: String, userId: Int, image: String) = launch {
        try {

            val info = medCardViewModel.getUserMedCard(userToken, userId)
            modelMedCardUser = info
            modelMedCardUser?.userPhoto = image
            val name = "${info.lastName} ${info.firstName} ${info.patronymic}"
            userNameMedCardPr.text = name
            birthDayMedCardPr.text = info.birthDate

            relativeMedCardExits.setOnClickListener {
                val intent = Intent(requireContext(), MedCardDetailActivity::class.java)
                intent.putExtra(EXTRA_MEDCARD_INFO, modelMedCardUser)
                startActivity(intent)
            }
        } catch (e: ConnectException) {

        } catch (e: SocketTimeoutException) {

        } catch (e: HttpException) {
            relAddMedCardAdd.visibility = View.VISIBLE
            relativeMedCardExits.visibility = View.GONE
        }
    }

}