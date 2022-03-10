package com.lihan.gamecenterstore.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameCenterStoreEntity(
    val name : String,
    val address : String,
    val lat : Double,
    val lng : Double
){
    @PrimaryKey(autoGenerate = true)
    var id : Int?=null
}
