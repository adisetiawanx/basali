package com.capstone.basaliproject.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstone.basaliproject.data.api.response.DataItem

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history:List<DataItem>)

    @Query("SELECT * FROM history")
    fun getAllHistory(): PagingSource<Int, DataItem>

    @Query("Delete FROM history")
    suspend fun deleteAll()
}