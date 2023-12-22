package com.capstone.basaliproject.ui.learn.karakter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.basaliproject.R
import com.capstone.basaliproject.ui.learn.model.KarakterModel

class KarakterViewModel : ViewModel() {
    private val _wyanjanaData = MutableLiveData<List<KarakterModel>>()
    val wyanjanaData: LiveData<List<KarakterModel>> get() = _wyanjanaData

    private val _tengenanData = MutableLiveData<List<KarakterModel>>()
    val tengenanData: LiveData<List<KarakterModel>> get() = _tengenanData

    private val _swaraData = MutableLiveData<List<KarakterModel>>()
    val swaraData: LiveData<List<KarakterModel>> get() = _swaraData

    private val _gantunganData = MutableLiveData<List<KarakterModel>>()
    val gantunganData: LiveData<List<KarakterModel>> get() = _gantunganData

    private val _ardhaswaraData = MutableLiveData<List<KarakterModel>>()
    val ardhaswaraData: LiveData<List<KarakterModel>> get() = _ardhaswaraData

    private val _angkaData = MutableLiveData<List<KarakterModel>>()
    val angkaData: LiveData<List<KarakterModel>> get() = _angkaData

    init {
        // Load data here
        tengenanData()
        wyanjanaData()
        swaraData()
        gantunganData()
        ardhaswaraData()
        angkaData()
    }

    private fun tengenanData() {
        // Replace this with your actual data loading logic
        _tengenanData.value = listOf(
            KarakterModel(1, R.drawable.tengenan_adeg, "adĕg"),
            KarakterModel(2, R.drawable.tengenan_bisah, "bisah"),
            KarakterModel(3, R.drawable.tengenan_cecek, "cĕcĕk"),
            KarakterModel(4, R.drawable.tengenan_surang, "surang"),
            KarakterModel(5, R.drawable.tengenan_ulu_candra, "ulu candra"),
            KarakterModel(6, R.drawable.tengenan_ulu_ricem, "ulu ricĕm"),
        )
    }

    private fun wyanjanaData() {
        // Replace this with your actual data loading logic
        _wyanjanaData.value = listOf(
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

    private fun swaraData() {
        _swaraData.value = listOf(
            KarakterModel(1, R.drawable.swara_a, "a"),
            KarakterModel(2, R.drawable.swara_a1, "ā"),
            KarakterModel(3, R.drawable.swara_ai, "ai"),
            KarakterModel(4, R.drawable.swara_au, "au"),
            KarakterModel(5, R.drawable.swara_e, "e"),
            KarakterModel(6, R.drawable.swara_i, "i"),
            KarakterModel(7, R.drawable.swara_i1, "ī"),
            KarakterModel(8, R.drawable.swara_io, "ḹ/lö"),
            KarakterModel(9, R.drawable.swara_le, "ḷ/lĕ"),
            KarakterModel(10, R.drawable.swara_o, "o"),
            KarakterModel(11, R.drawable.swara_re, "ṛ/rĕ"),
            KarakterModel(12, R.drawable.swara_ro, "ṝ/rö"),
            KarakterModel(13, R.drawable.swara_u, "u"),
            KarakterModel(14, R.drawable.swara_u1, "ū"),
        )
    }

    private fun gantunganData() {
        _gantunganData.value = listOf(
            KarakterModel(1, R.drawable.gantungan_ha, "ha/a"),
            KarakterModel(2, R.drawable.gantungan_na, "na"),
            KarakterModel(3, R.drawable.gantungan_ca, "ca"),
            KarakterModel(4, R.drawable.gantungan_ra, "ra"),
            KarakterModel(5, R.drawable.gantungan_ka, "ka"),
            KarakterModel(6, R.drawable.gantungan_da, "da"),
            KarakterModel(7, R.drawable.gantungan_ta, "ta"),
            KarakterModel(8, R.drawable.gantungan_sa, "sa"),
            KarakterModel(9, R.drawable.gantungan_wa, "wa"),
            KarakterModel(10, R.drawable.gantungan_la, "la"),
            KarakterModel(11, R.drawable.gantungan_ma, "ma"),
            KarakterModel(12, R.drawable.gantungan_nga, "nga"),
            KarakterModel(13, R.drawable.gantungan_nya, "nya"),
            KarakterModel(14, R.drawable.gantungan_ya, "ya"),
            KarakterModel(15, R.drawable.gantungan_pa, "pa"),
            KarakterModel(16, R.drawable.gantungan_ja, "ja"),
            KarakterModel(17, R.drawable.gantungan_ba, "ba"),
            KarakterModel(18, R.drawable.gantungan_ga, "ga"),
        )
    }

    private fun ardhaswaraData() {
        _ardhaswaraData.value = listOf(
            KarakterModel(1, R.drawable.ardhaswara_gantungan_la, "l"),
            KarakterModel(2, R.drawable.ardhaswara_guwung, "r"),
            KarakterModel(3, R.drawable.ardhaswara_guwung_macelek, "rĕ"),
            KarakterModel(4, R.drawable.ardhaswara_nania, "y"),
            KarakterModel(5, R.drawable.ardhaswara_sukukembung, "w"),
        )
    }

    private fun angkaData() {
        _angkaData.value = listOf(
            KarakterModel(1, R.drawable.angka0, "0"),
            KarakterModel(2, R.drawable.angka1, "1"),
            KarakterModel(3, R.drawable.angka2, "2"),
            KarakterModel(4, R.drawable.angka3, "3"),
            KarakterModel(5, R.drawable.angka4, "4"),
            KarakterModel(6, R.drawable.angka5, "5"),
            KarakterModel(7, R.drawable.angka6, "6"),
            KarakterModel(8, R.drawable.angka7, "7"),
            KarakterModel(9, R.drawable.angka8, "8"),
            KarakterModel(10, R.drawable.angka9, "9"),
        )
    }
}