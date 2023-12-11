package com.capstone.basaliproject.ui.learn.karakter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.basaliproject.R
import com.capstone.basaliproject.ui.learn.model.KarakterModel

class TengenanViewModel : ViewModel() {
    // Use LiveData or StateFlow to hold your data
    private val _karakterData = MutableLiveData<List<KarakterModel>>()
    val karakterData: LiveData<List<KarakterModel>> get() = _karakterData

    init {
        // Load data here
        loadData()
    }

    private fun loadData() {
        // Replace this with your actual data loading logic
        _karakterData.value = listOf(
            KarakterModel(1, R.drawable.tengenan_adeg, "adĕg"),
            KarakterModel(1, R.drawable.tengenan_bisah, "bisah"),
            KarakterModel(1, R.drawable.tengenan_cecek, "cĕcĕk"),
            KarakterModel(1, R.drawable.tengenan_surang, "surang"),
            KarakterModel(1, R.drawable.tengenan_ulu_candra, "ulu candra"),
            KarakterModel(1, R.drawable.tengenan_ulu_ricem, "ulu ricĕm"),
        )
    }
}