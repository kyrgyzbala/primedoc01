package kg.kyrgyzcoder.primedoc01

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kg.kyrgyzcoder.primedoc01.ui.about.AboutUsFragment
import kg.kyrgyzcoder.primedoc01.ui.chat.ChatChatListFragment
import kg.kyrgyzcoder.primedoc01.ui.clinic.ClinicFragment
import kg.kyrgyzcoder.primedoc01.ui.faq.FaqFragment
import kg.kyrgyzcoder.primedoc01.ui.med_card.medcard.MedCardPrFragment


class MainPagerAdapter(private val fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> {
                ClinicFragment()
            }
            1 -> {
                MedCardPrFragment()
            }
            2 -> {
                ChatChatListFragment()
            }
            3 -> {
                FaqFragment()
            }
            else -> {
                AboutUsFragment()
            }
        }
    }

}