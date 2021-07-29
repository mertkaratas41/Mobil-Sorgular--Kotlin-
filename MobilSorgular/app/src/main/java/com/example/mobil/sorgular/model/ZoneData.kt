package com.example.mobil.sorgular.model

data class ZoneData( // konum verisi
    val LocationID: Int = 0,
    val borough: String = "",
    val zone: String = "",
    val latitude:  Double = 0.0,
    val longitude: Double = 0.0
)