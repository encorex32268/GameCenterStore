package com.lihan.gamecenterstore.data

import com.lihan.gamecenterstore.domain.model.GameCenterStore
import kotlinx.coroutines.flow.Flow

interface GameCenterRepository {

    suspend fun insertStore(gameCenterStoreEntity: GameCenterStore)
    fun getAllGameCenter() : Flow<List<GameCenterStore>>
}