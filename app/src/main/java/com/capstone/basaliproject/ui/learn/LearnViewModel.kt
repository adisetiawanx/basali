package com.capstone.basaliproject.ui.learn

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.basaliproject.R
import com.capstone.basaliproject.ui.learn.model.KarakterModel
import com.capstone.basaliproject.ui.learn.model.LearnModel

class LearnViewModel(application: Application) : AndroidViewModel(application) {

    // Use LiveData or StateFlow to hold your data
    private val _learnData = MutableLiveData<List<LearnModel>>()
    val learnData: LiveData<List<LearnModel>> get() = _learnData

    init {
        // Load data here
        loadData()
    }

    private fun loadData() {
        // Replace this with your actual data loading logic
        _learnData.value = listOf(
            LearnModel(1, R.drawable.aksara_suara, "Panganggĕ  Swara", "Aksara swara (ᬳᬓ᭄ᬱᬭᬲ᭄ᬯᬭ}}) adalah aksara yang digunakan untuk suku kata yang tidak memiliki konsonan di awal, atau dalam kata lain suku kata yang hanya terdiri vokal. Aksara Bali memiliki 14 aksara vokal yang diwarisi dari tradisi tulis Sanskerta."),
            LearnModel(2, R.drawable.aksara_wianjana, "Aksara Wyañjana", "Aksara wyañjana (ᬳᬓ᭄ᬱᬭᬯ᭄ᬬᬜ᭄ᬚᬦ) adalah aksara konsonan dengan vokal inheren /a/. Sebagai salah satu aksara turunan Brahmi, aksara Bali memiliki 33 aksara wyañjana untuk menuliskan 33 bunyi konsonan yang digunakan dalam bahasa Sanskerta dan Kawi."),
            LearnModel(3, R.drawable.aksara_swalalita, "Aksara śwalalita", "Aksara Swalalita adalah aksara yang digunakan untuk menulis Bahasa Kawi, Bahasa Bali Tengahan, Bahasa Sansekerta dan digunakan untuk menulis Kidung, Kakawin Parwa dan Sloka."),
            LearnModel(4, R.drawable.aksara_suara, "Aksara Modre", "Aksara modre (ᬳᬓ᭄ᬱᬭᬫᭀᬤ᭄ᬭᬾ}}) adalah aksara suci yang terutama dipakai dalam bidang keagamaan untuk upacara, mantra, rajah, dan fungsi-fungsi keramat lainnya. Aksara tipe ini memiliki berbagai macam rupa, tetapi umumnya ditandai dengan adanya diakritik ulu candra atau ulu ricĕm. Pembahasan mengenai rupa dan jenis modre dapat ditemukan pada lontar dengan judul krakah atau griguh."),
            LearnModel(5, R.drawable.gantungan, "Aksara Gantungan", "Vokal inheren dari tiap aksara dasar dapat dimatikan dengan penggunaan diaktrik adĕg-adĕg. Akan tetapi, adĕg-adĕg normalnya tidak digunakan di tengah kata atau kalimat, sehingga untuk menuliskan suku kata tertutup di tengah kata dan kalimat, digunakanlah bentuk gantungan (ᬕᬦ᭄ᬢᬸᬗᬦ᭄) atau gempelan (ᬕᬾᬫ᭄ᬧᬾᬮᬦ᭄) yang dimiliki oleh setiap aksara dasar; gantungan melekat di bawah aksara dasar sementara gempelan melekat di samping aksara dasar. Berbeda dengan adĕg-adĕg, gantungan/gempelan tidak hanya mematikan konsonan yang diiringinya tetapi juga menunjukkan konsonan selanjutnya. Sebagai contoh, aksara ma (ᬫ) yang diiringi bentuk pasangan dari pa (᭄ᬧ) menjadi mpa (ᬫ᭄ᬧ)."),
            LearnModel(6, R.drawable.angka_bali, "Aksara Angka", "Aksara Bali memiliki lambang bilangannya sendiri yang berlaku selayaknya angka Arab, namun sebagian bentuknya memiliki rupa yang persis sama dengan beberapa aksara Bali, semisal angka 2 ᭒ dengan aksara swara la lĕnga ᬍ}}. Karena persamaan bentuk ini, angka yang digunakan di tengah kalimat perlu diapit dengan tanda baca carik untuk memperjelas fungsinya sebagai lambang bilangan. Semisal, \"tanggal 23 Ruwah\" ditulis ᬢᬗ᭄ᬕᬮ᭄᭞᭒᭓᭞ᬭᬸᬯᬄ}}. Pengapit ini dapat diabaikan apabila fungsi lambang bilangan sudah jelas dari konteks, misal nomor halaman di pojok lontar."),
            LearnModel(7, R.drawable.pengangge_tengenan, "Panganggĕ Tĕngĕnan", "Panganggĕ tĕngĕnan (ᬧᬗᬗ᭄ᬕᭂᬢᭂᬗᭂᬦᬦ᭄) digunakan untuk menutup suatu suku kata dengan konsonan."),
            LearnModel(8, R.drawable.aksara_suara, "Panganggĕ Ardhaswara", "Panganggĕ ardhaswara (ᬧᬗᬗ᭄ᬕᭂᬳᬃᬥᬲ᭄ᬯᬭ) digunakan untuk menuliskan gugus konsonan semivokal dalam satu suku kata."),
            // Add more items here...
        )
    }
}
