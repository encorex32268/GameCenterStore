package com.lihan.gamecenterstore.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GameCenterStoreEntity::class], version = 1)
abstract class GameCenterDatabase : RoomDatabase(){
    abstract val dao : GameCenterDao
}