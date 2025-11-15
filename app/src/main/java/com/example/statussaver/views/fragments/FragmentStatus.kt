package com.example.statussaver.views.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.statussaver.data.StatusRepo
import com.example.statussaver.databinding.FragmentStatusBinding
import com.example.statussaver.utils.Constants
import com.example.statussaver.utils.SharedPrefKeys
import com.example.statussaver.utils.SharedPrefUtils
import com.example.statussaver.utils.getFolderPermissions
import com.example.statussaver.viewmodels.StatusViewModel
import com.example.statussaver.viewmodels.factories.StatusViewModelFactory
import com.example.statussaver.views.adapters.MediaViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class FragmentStatus : Fragment() {
    private val binding by lazy {
        FragmentStatusBinding.inflate(layoutInflater)
    }

    private lateinit var type: String
    private val WHATSAPP_REQUEST_CODE = 101
    private val WHATSAPP_BUSINESS_REQUEST_CODE = 102

    private val viewPagerTitles = arrayOf("Images", "Videos")
    lateinit var viewModel: StatusViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            arguments?.let {
                val repo = StatusRepo(requireActivity())
                viewModel = ViewModelProvider(requireActivity(), StatusViewModelFactory(repo)) [StatusViewModel::class.java]

                type = it.getString(Constants.FRAGMENT_TYPE_KEY, "")
                tempText.text = type
                when (type) {
                    Constants.TYPE_WHATSAPP_MAIN -> {

                        val isPermissionGranted = SharedPrefUtils.getPrefBoolean(
                            SharedPrefKeys.PREF_KEY_WP_PERMISSION_GRANTED,
                            false)
                        if (isPermissionGranted) {
                            getWhatsAppStatuses()
                        }

                        permissionLayout.allowBtnPermissions.setOnClickListener {
                            getFolderPermissions(
                                context = requireActivity(),
                                REQUEST_CODE = WHATSAPP_REQUEST_CODE,
                                initalUri = Constants.getWhatsappUri()
                            )
                        }

                        val viewPagerAdapter = MediaViewPagerAdapter(requireActivity())
                        statusViewPager.adapter = viewPagerAdapter
                        TabLayoutMediator(tabLayout, statusViewPager) { tab, pos ->
                            tab.text = viewPagerTitles[pos]
                        }.attach()

                    }
                    Constants.TYPE_WHATSAPP_BUSINESS -> {

                        val isPermissionGranted = SharedPrefUtils.getPrefBoolean(
                            SharedPrefKeys.PREF_KEY_WP_BUSINESS_PERMISSION_GRANTED,
                            false)
                        if (isPermissionGranted) {
                            getWhatsAppBusinessStatuses()
                        }

                        permissionLayout.allowBtnPermissions.setOnClickListener {
                            getFolderPermissions(
                                context = requireActivity(),
                                REQUEST_CODE = WHATSAPP_BUSINESS_REQUEST_CODE,
                                initalUri = Constants.getWhatsappBusinessUri()
                            )
                        }

                        val viewPagerAdapter = MediaViewPagerAdapter(requireActivity(),
                            imageType = Constants.MEDIA_TYPE_WHATSAPP_BUSINESS_IMAGES,
                            videoType = Constants.MEDIA_TYPE_WHATSAPP_BUSINESS_VIDEOS
                        )
                        statusViewPager.adapter = viewPagerAdapter
                        TabLayoutMediator(tabLayout, statusViewPager) { tab, pos ->
                            tab.text = viewPagerTitles[pos]
                        }.attach()

                    }
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    fun getWhatsAppStatuses() {
        // function to get whatApp statuses
        binding.permissionLayoutHolder.visibility = View.GONE
        viewModel.getWhatsAppStatuses()
    }

    fun getWhatsAppBusinessStatuses() {
        // function to get whatApp statuses
        binding.permissionLayoutHolder.visibility = View.GONE
        viewModel.getWhatsAppBusinessStatuses()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK) {
            val treeUri = data?.data!!
            requireActivity().contentResolver.takePersistableUriPermission(
                treeUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            if (requestCode == WHATSAPP_REQUEST_CODE) {
                // WhatsApp Logic
                SharedPrefUtils.putPrefString(
                    SharedPrefKeys.PREF_KEY_WP_TREE_URI,
                    treeUri.toString()
                )
                SharedPrefUtils.putPrefBoolean(SharedPrefKeys.PREF_KEY_WP_PERMISSION_GRANTED, true)

                getWhatsAppStatuses()
            }
            else if (requestCode == WHATSAPP_BUSINESS_REQUEST_CODE) {
                // WhatsApp Business Logic
                SharedPrefUtils.putPrefString(
                    SharedPrefKeys.PREF_KEY_WP_BUSINESS_TREE_URI,
                    treeUri.toString()
                )
                SharedPrefUtils.putPrefBoolean(SharedPrefKeys.PREF_KEY_WP_BUSINESS_PERMISSION_GRANTED, true)

                getWhatsAppBusinessStatuses()
            }
        }
    }
}