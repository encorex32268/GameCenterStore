package com.lihan.gamecenterstore.domain.model

data class GameCenterStore (
    val id : Int?=null,
    val name : String,
    val address : String,
    val lat : Double,
    val lng : Double
    ){
    override fun toString(): String {
        return "GameCenterStore(id=$id, name='$name', address='$address', lat=$lat, lng=$lng)"
    }
}