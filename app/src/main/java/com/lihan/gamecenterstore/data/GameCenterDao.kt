package com.lihan.gamecenterstore.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lihan.gamecenterstore.domain.model.GameCenterStore
import kotlinx.coroutines.flow.Flow

@Dao
interface GameCenterDao {

    @Insert
    suspend fun insertStore(gameCenterStoreEntity: GameCenterStoreEntity)

    @Query("select * from GameCenterStoreEntity")
    fun getAllGameCenter() : Flow<List<GameCenterStoreEntity>>
}