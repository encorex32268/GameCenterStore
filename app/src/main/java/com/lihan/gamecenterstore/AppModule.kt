package com.lihan.gamecenterstore

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lihan.gamecenterstore.data.GameCenterDatabase
import com.lihan.gamecenterstore.data.GameCenterRepository
import com.lihan.gamecenterstore.data.GameCenterRepositoryImp
import com.lihan.gamecenterstore.data.toGameCenterStoreEntity
import com.lihan.gamecenterstore.domain.model.GameCenterStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGameCenterStoreDatabase(app: Application): GameCenterDatabase {
        return Room.databaseBuilder(
            app,
            GameCenterDatabase::class.java,
            "gamecenter_store.db"
        ).build().also { db ->
            CoroutineScope(Dispatchers.IO).launch {
                val gson = Gson()
                val tokenType = object : TypeToken<ArrayList<GameCenterStore>>(){}.type
                val gameCenterStores : ArrayList<GameCenterStore> = gson.fromJson(TaitoStore.json,tokenType)
                gameCenterStores.forEach {
                    db.dao.insertStore(it.toGameCenterStoreEntity())
                    }
                }
            }
        }


    @Singleton
    @Provides
    fun provideGameCenterRepository(db: GameCenterDatabase): GameCenterRepository {
        return GameCenterRepositoryImp(db.dao)
    }


}