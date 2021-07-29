package com.example.mobil.sorgular.query

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseAPI {

    // JAVA FirebaseAPI firebase;
    // KOTLİN firebase: FirebaseAPI;

     // motionlayout
    // recylerview adapter
    lateinit var db: FirebaseFirestore //Firestore dan veri çekmek için https://firebase.google.com/docs/firestore/quickstart?authuser=0

    fun initFirestore() {
        db = Firebase.firestore // firestoru başlattık. db objesine atadık. sorgularda kullanacağız.
    }

    // = in return den farkı yok. yani return olarak db.collection("Test").get()  dönmüş olduk. diğer tarafta buna
    // bir listener ekleyeceğız. listener ın içinde verinin databaseden gelip gelmediğini kontrol
    // edeceğiz.
    fun getTip1(): Task<QuerySnapshot> = //1. sorgu
        db.collection("tripdata") // veriyi alacağımız collection
            .orderBy("trip_distance", Query.Direction.DESCENDING) // yolcu sayısına göre büyükten küçüğe sıralıyoruz
            .limit(5) // ilk 5 kayıtı getiriyoruz
            .get()



    fun getTip2(startTime: Long, finishTime: Long): Task<QuerySnapshot> =
        db.collection("tripdata")
            .whereGreaterThanOrEqualTo("tpep_pickup_datetime", startTime)
            .whereLessThanOrEqualTo("tpep_pickup_datetime", finishTime)
            .get()

    fun getTip3(startTime: Long, finishTime: Long): Task<QuerySnapshot> =
        db.collection("tripdata")
            .whereGreaterThanOrEqualTo("tpep_pickup_datetime", startTime)
            .whereLessThanOrEqualTo("tpep_pickup_datetime", finishTime)
            .get()

    fun getLocation(locationId: Int): Task<QuerySnapshot> =
        db.collection("zonedata")
            .whereEqualTo("LocationID", locationId)
            .get()

}