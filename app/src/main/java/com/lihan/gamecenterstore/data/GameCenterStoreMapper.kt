package com.lihan.gamecenterstore.data

import com.lihan.gamecenterstore.domain.model.GameCenterStore

fun GameCenterStoreEntity.toGameCenterStore() : GameCenterStore {
    return GameCenterStore(
        id, name, address, lat, lng
    )
}

fun GameCenterStore.toGameCenterStoreEntity() : GameCenterStoreEntity{
    return GameCenterStoreEntity(
        name, address, lat, lng
    )
}