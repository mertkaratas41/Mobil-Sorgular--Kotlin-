package com.example.mobil.sorgular.model

data class TripData(
    // yolculuk verisi
    val VendorID: Int = 0,
    val tpep_pickup_datetime: Long = 0,
    val tpep_dropoff_datetime: Long = 0,
    val passenger_count: Int = 0,
    val trip_distance: Float = 0.0f,
    val RatecodeID: Int = 0,
    val store_and_fwd_flag: String = "",
    val DOLocationID: Int = 0,
    val PULocationID: Int = 0,
    val payment_type: Int = 0,
    val fare_amount: Float = 0.0f,
    val extra: Float = 0.0f,
    val mta_tax: Float = 0.0f,
    val tip_amount: Float = 0.0f,
    val tolls_amount: Float = 0.0f,
    val improvement_surcharge: Float = 0.0f,
    val total_amount: Float = 0.0f,
    val congestion_surcharge: Float = 0.0f,
    )

