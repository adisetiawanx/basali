package com.capstone.basaliproject.ui.learn

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.basaliproject.R
import com.capstone.basaliproject.ui.learn.model.LearnModel

class LearnViewModel(application: Application) : AndroidViewModel(application) {

    // Use LiveData or StateFlow to hold your data
    private val _learnData = MutableLiveData<List<LearnModel>>()
    val learnData: LiveData<List<LearnModel>> get() = _learnData

    init {
        // Load data here
        loadData(application)
    }

    private fun loadData(application: Application) {
        // Replace this with your actual data loading logic
        _learnData.value = listOf(
            LearnModel(1, R.drawable.aksara_suara, application.getString(R.string.pangangge_swara), application.getString(R.string.desc_pangangge_swara)),
            LearnModel(2, R.drawable.aksara_wianjana, application.getString(R.string.aksara_wyanjana), application.getString(R.string.desc_aksara_wyanjana)),
            LearnModel(3, R.drawable.aksara_swalalita, application.getString(R.string.aksara_swalalita), application.getString(R.string.desc_aksara_swalalita)),
            LearnModel(4, R.drawable.aksara_suara, application.getString(R.string.aksara_modre), application.getString(R.string.desc_aksara_modre)),
            LearnModel(5, R.drawable.gantungan, application.getString(R.string.aksara_gantungan), application.getString(R.string.desc_aksara_gantungan)),
            LearnModel(6, R.drawable.angka_bali, application.getString(R.string.aksara_angka), application.getString(R.string.desc_aksara_angka)),
            LearnModel(7, R.drawable.pengangge_tengenan, application.getString(R.string.pangangge_tengenan), application.getString(R.string.desc_pangangge_tengenan)),
            LearnModel(8, R.drawable.aksara_suara, application.getString(R.string.pangangge_ardhaswara), application.getString(R.string.desc_pangangge_ardhaswara))
            // Add more items here...
        )
    }
}
