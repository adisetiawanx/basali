package com.capstone.basaliproject.ui.learn.karakter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.basaliproject.R
import com.capstone.basaliproject.ui.learn.model.KarakterModel
import com.capstone.basaliproject.ui.learn.model.LearnModel

class WyanjanaViewModel : ViewModel() {
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
            KarakterModel(1, R.drawable.wyanjana_ca, "ca"),
            KarakterModel(2, R.drawable.wyanjana_da, "ḍa"),
            KarakterModel(3, R.drawable.wyanjana_ga, "ga"),
            KarakterModel(4, R.drawable.wyanjana_da1, "da"),
            KarakterModel(5, R.drawable.wyanjana_ka, "ka"),
            KarakterModel(6, R.drawable.wyanjana_kha, "kha"),
            KarakterModel(7, R.drawable.wyanjana_ga, "ga"),
            KarakterModel(8, R.drawable.wyanjana_gha, "gha"),
            KarakterModel(9, R.drawable.wyanjana_nga, "ṅa"),
            KarakterModel(10, R.drawable.wyanjana_ha, "ha/a"),
            KarakterModel(11, R.drawable.wyanjana_ca, "ca"),
            KarakterModel(12, R.drawable.wyanjana_cha, "cha"),
            KarakterModel(13, R.drawable.wyanjana_la, "ja"),
            KarakterModel(14, R.drawable.wyanjana_lha, "jha"),
            KarakterModel(15, R.drawable.wyanjana_na_rambat, "ña"),
            KarakterModel(16, R.drawable.wyanjana_ya, "ya"),
            KarakterModel(17, R.drawable.wyanjana_ra, "ra"),
        )
    }
}