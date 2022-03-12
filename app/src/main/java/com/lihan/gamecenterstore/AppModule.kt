package com.lihan.gamecenterstore

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lihan.gamecenterstore.data.*
import com.lihan.gamecenterstore.domain.model.GameCenterStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
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
            initData(db)
        }
    }

    private fun initData(db: GameCenterDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            db.dao.getAllGameCenter().collect {
                if (it.isEmpty()) {
                    val gson = Gson()
                    val tokenType = object : TypeToken<ArrayList<GameCenterStore>>() {}.type
                    val gameCenterStores: ArrayList<GameCenterStore> =
                        gson.fromJson(TaitoStore.json, tokenType)
                    gameCenterStores.forEach {
                        db.dao.insertStore(it.toGameCenterStoreEntity())
                    }
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