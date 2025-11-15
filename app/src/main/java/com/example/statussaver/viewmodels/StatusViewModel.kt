package com.example.statussaver.viewmodels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.statussaver.data.StatusRepo
import com.example.statussaver.models.MEDIA_TYPE_IMAGE
import com.example.statussaver.models.MEDIA_TYPE_VIDEO
import com.example.statussaver.models.MediaModel
import com.example.statussaver.utils.Constants
import com.example.statussaver.utils.SharedPrefKeys
import com.example.statussaver.utils.SharedPrefUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatusViewModel (val repo: StatusRepo) : ViewModel() {

    private val wpStatusLiveData get() = repo.whatsAppStatusesLiveData

    private val wpBusinessStatusLiveData get() = repo.whatsAppBusinessStatusesLiveData

    // wp main
    val whatsAppImagesLiveData = MutableLiveData<ArrayList<MediaModel>>()
    val whatsAppVideosLiveData = MutableLiveData<ArrayList<MediaModel>>()

    // wp business
    val whatsAppBusinessImagesLiveData = MutableLiveData<ArrayList<MediaModel>>()
    val whatsAppBusinessVideosLiveData = MutableLiveData<ArrayList<MediaModel>>()

    private var isPermissionsGranted = false

    init {
        SharedPrefUtils.init(repo.context)

        val wpPermissions = SharedPrefUtils.getPrefBoolean(SharedPrefKeys.PREF_KEY_WP_PERMISSION_GRANTED, false)
        val wpBusinessPermissions = SharedPrefUtils.getPrefBoolean(SharedPrefKeys.PREF_KEY_WP_BUSINESS_PERMISSION_GRANTED, false)

        isPermissionsGranted = wpPermissions && wpBusinessPermissions

        if (isPermissionsGranted) {
            CoroutineScope(Dispatchers.IO).launch {
                repo.getAllStatuses()
                repo.getAllStatuses(Constants.TYPE_WHATSAPP_BUSINESS)
            }
            CoroutineScope(Dispatchers.IO).launch {
                repo.getAllStatuses(Constants.TYPE_WHATSAPP_BUSINESS)
            }
        }

    }

    fun getWhatsAppStatuses() {
        CoroutineScope(Dispatchers.IO).launch {
            if (!isPermissionsGranted) {
                repo.getAllStatuses()
            }

            withContext(Dispatchers.Main) {
                getWhatsAppImages()
                getWhatsAppVideos()
            }
        }
    }

    fun getWhatsAppImages() {
        wpStatusLiveData.observe(repo.activity as LifecycleOwner) {
            val tempList = ArrayList<MediaModel>()
            it.forEach { mediaModel ->
                if (mediaModel.type == MEDIA_TYPE_IMAGE) {
                    tempList.add(mediaModel)
                }
            }
            whatsAppImagesLiveData.postValue(tempList)
        }
    }

    fun getWhatsAppVideos() {
        wpStatusLiveData.observe(repo.activity as LifecycleOwner) {
            val tempList = ArrayList<MediaModel>()
            it.forEach { mediaModel ->
                if (mediaModel.type == MEDIA_TYPE_VIDEO) {
                    tempList.add(mediaModel)
                }
            }
            whatsAppVideosLiveData.postValue(tempList)
        }
    }

    fun getWhatsAppBusinessStatuses() {
        CoroutineScope(Dispatchers.IO).launch {
            if (!isPermissionsGranted) {
                repo.getAllStatuses(Constants.TYPE_WHATSAPP_BUSINESS)
            }

            withContext(Dispatchers.Main) {
                getWhatsAppBusinessImages()
                getWhatsAppBusinessVideos()
            }
        }
    }

    fun getWhatsAppBusinessImages() {
        wpBusinessStatusLiveData.observe(repo.activity as LifecycleOwner) {
            val tempList = ArrayList<MediaModel>()
            it.forEach { mediaModel ->
                if (mediaModel.type == MEDIA_TYPE_IMAGE) {
                    tempList.add(mediaModel)
                }
            }
            whatsAppBusinessImagesLiveData.postValue(tempList)
        }
    }

    fun getWhatsAppBusinessVideos() {
        wpBusinessStatusLiveData.observe(repo.activity as LifecycleOwner) {
            val tempList = ArrayList<MediaModel>()
            it.forEach { mediaModel ->
                if (mediaModel.type == MEDIA_TYPE_VIDEO) {
                    tempList.add(mediaModel)
                }
            }
            whatsAppBusinessVideosLiveData.postValue(tempList)
        }
    }

}