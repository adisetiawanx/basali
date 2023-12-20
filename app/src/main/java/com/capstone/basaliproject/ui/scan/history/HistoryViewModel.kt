package com.capstone.basaliproject.ui.scan.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.capstone.basaliproject.data.api.response.DataItem
import com.capstone.basaliproject.data.repo.UserRepository

class HistoryViewModel(repository: UserRepository) : ViewModel() {

    val history: LiveData<PagingData<DataItem>> =
        repository.getAksaraSortedByScannedAt().cachedIn(viewModelScope)

}