package com.example.statussaver.views.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.statussaver.utils.Constants
import com.example.statussaver.views.fragments.FragmentMedia

class MediaViewPagerAdapter (
    private val fragmentActivity: FragmentActivity,
    private val imageType: String = Constants.MEDIA_TYPE_WHATSAPP_IMAGES,
    private val videoType: String = Constants.MEDIA_TYPE_WHATSAPP_VIDEOS
) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val mediaFragment = FragmentMedia()
                val bundle = Bundle()
                bundle.putString(Constants.MEDIA_TYPE_KEY, imageType)
                mediaFragment.arguments = bundle
                mediaFragment
            }
            else -> {
                val mediaFragment = FragmentMedia()
                val bundle = Bundle()
                bundle.putString(Constants.MEDIA_TYPE_KEY, videoType)
                mediaFragment.arguments = bundle
                mediaFragment
            }
        }
    }

    override fun getItemCount(): Int = 2

}