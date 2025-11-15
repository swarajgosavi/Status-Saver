package com.example.statussaver.views.activites

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.statussaver.utils.Constants
import com.example.statussaver.R
import com.example.statussaver.databinding.ActivityMainBinding
import com.example.statussaver.utils.SharedPrefUtils
import com.example.statussaver.utils.replaceFragment
import com.example.statussaver.views.fragments.FragmentSettings
import com.example.statussaver.views.fragments.FragmentStatus

class MainActivity : AppCompatActivity() {
    private val activity = this
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        SharedPrefUtils.init( activity )
        binding.apply {

            val fragmentWhatsAppStatus = FragmentStatus()
            val bundle = Bundle()
            bundle.putString(Constants.FRAGMENT_TYPE_KEY, Constants.TYPE_WHATSAPP_MAIN)
            replaceFragment(fragmentWhatsAppStatus, bundle)

            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_status -> {
                        // WhatsApp Status
                        val fragmentWhatsAppStatus = FragmentStatus()
                        val bundle = Bundle()
                        bundle.putString(Constants.FRAGMENT_TYPE_KEY, Constants.TYPE_WHATSAPP_MAIN)
                        replaceFragment(fragmentWhatsAppStatus, bundle)
                    }
                    R.id.menu_business_status -> {
                        // WhatsApp Status Business
                        val fragmentWhatsAppStatus = FragmentStatus()
                        val bundle = Bundle()
                        bundle.putString(Constants.FRAGMENT_TYPE_KEY, Constants.TYPE_WHATSAPP_BUSINESS)
                        replaceFragment(fragmentWhatsAppStatus, bundle)
                    }
                    R.id.menu_settings -> {
                        // Settings
                        replaceFragment(FragmentSettings())
                    }
                }
                return@setOnItemSelectedListener true

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }
}