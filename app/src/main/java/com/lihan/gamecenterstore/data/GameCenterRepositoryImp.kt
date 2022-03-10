package com.lihan.gamecenterstore.data

import com.lihan.gamecenterstore.domain.model.GameCenterStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GameCenterRepositoryImp (
    private val dao : GameCenterDao
        ) : GameCenterRepository{
    override suspend fun insertStore(gameCenterStore : GameCenterStore) {
        dao.insertStore(gameCenterStore.toGameCenterStoreEntity())
    }

    override fun getAllGameCenter(): Flow<List<GameCenterStore>> {
       return dao.getAllGameCenter().map {
           it.map {
               it.toGameCenterStore()
           }
       }
    }


}