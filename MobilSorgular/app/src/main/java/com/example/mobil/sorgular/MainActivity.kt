package com.example.mobil.sorgular

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mobil.sorgular.adapter.Sorgu1Adapter
import com.example.mobil.sorgular.query.FirebaseAPI
import com.example.mobil.sorgular.model.TripData
import com.example.mobil.sorgular.model.ZoneData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.FirebaseApp
import com.google.maps.android.PolyUtil
import org.json.JSONObject
import java.util.*

class
MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var btnSorgu1: Button
    private lateinit var btnSorgu2: Button
    private lateinit var btnSorgu3: Button
    private lateinit var firebaseAPI: FirebaseAPI
    private lateinit var sorgu1Adapter: Sorgu1Adapter
    private lateinit var recylcerViewSorgu1: RecyclerView
    private lateinit var recylcerViewSorgu2: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var linearLayoutManager2: LinearLayoutManager
    private lateinit var datePicker2Baslangic: DatePicker
    private lateinit var datePicker2Bitis: DatePicker
    private lateinit var datePicker3: DatePicker
    private lateinit var sorgu3: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this) // firebase başlatıyoruz
        initMap() // harita başlatıyorz
        initFirebase() // firebase api (kendi database sorgu class ımız) başlatıyoruz
        initUI() // button textview gibi şeyleri xmldekileri classımızda başlattık
        datePickerMaxMinValue() // tarih seçicinin max ve min değerlerini aralık 2020 olarak ayarladık

        linearLayoutManager = LinearLayoutManager(this) // aşağıya kayan liste(ilk sorgudaki alt alta listenen sonuçlar) için linear layout manageri recyclerview e vermeliyiz.
        linearLayoutManager2 = LinearLayoutManager(this)
        recylcerViewSorgu1.layoutManager = linearLayoutManager
        recylcerViewSorgu2.layoutManager = linearLayoutManager2 // 2. sorgudaki recyclerview

        btnSorgu1.setOnClickListener { // 1. sorgunun butonuna tıklayınca nolacak.
            firebaseAPI.getTip1().addOnSuccessListener { documents -> //verileri databaseden getirdik.
                val dataList: List<TripData> = documents.toObjects(TripData::class.java)
                sorgu1Adapter = Sorgu1Adapter(dataList) // adapter imize listemizi verdik. adapter: verileri sıralayan alt alta şey. instagramda resimlerin alt alta sıralanması gibi. bizde 5 tane sonuç getiriyoruz.
                recylcerViewSorgu1.adapter = sorgu1Adapter // adapteri recyclerview e bağladık
            }
        }

        btnSorgu2.setOnClickListener {
            val calendarBaslangic: Calendar = Calendar.getInstance() // data picker dan günleri aldık.
            calendarBaslangic.set(Calendar.YEAR, datePicker2Baslangic.year)
            calendarBaslangic.set(Calendar.MONTH, datePicker2Baslangic.month)
            calendarBaslangic.set(Calendar.DAY_OF_MONTH, datePicker2Baslangic.dayOfMonth)
            calendarBaslangic.set(Calendar.MINUTE, 0)
            calendarBaslangic.set(Calendar.HOUR, 3)
            calendarBaslangic.set(Calendar.SECOND, 0)
            val timestampBaslangic: Long = calendarBaslangic.timeInMillis / 1000

            val calendarBitis: Calendar = Calendar.getInstance()
            calendarBitis.set(Calendar.YEAR, datePicker2Bitis.year)
            calendarBitis.set(Calendar.MONTH, datePicker2Bitis.month)
            calendarBitis.set(Calendar.DAY_OF_MONTH, datePicker2Bitis.dayOfMonth)
            calendarBitis.set(Calendar.MINUTE, 59)
            calendarBitis.set(Calendar.HOUR, 20)
            calendarBitis.set(Calendar.SECOND, 59)
            val timestampBitis: Long = calendarBitis.timeInMillis / 1000

            Toast.makeText(this, timestampBaslangic.toString(), Toast.LENGTH_LONG).show()
            Toast.makeText(this, timestampBitis.toString(), Toast.LENGTH_LONG).show()

            firebaseAPI.getTip2(timestampBaslangic, timestampBitis)
                .addOnSuccessListener { documents ->
                    val dataList: List<TripData> = documents.toObjects(TripData::class.java)
                    val list_filtered = dataList.filter { it.trip_distance > 0.0 }.sortedBy { it.trip_distance }.take(5) // burda filtreleme işlemini yapıyoruz. 0.0 a eşit olanları almadık. trip distance e göre sıraladık. ve 5 tane aldık.
                    sorgu1Adapter = Sorgu1Adapter(list_filtered)
                    recylcerViewSorgu2.adapter = sorgu1Adapter
                }
        }


        btnSorgu3.setOnClickListener {
            val calendarBaslangic: Calendar = Calendar.getInstance()
            calendarBaslangic.set(Calendar.YEAR, datePicker3.year)
            calendarBaslangic.set(Calendar.MONTH, datePicker3.month)
            calendarBaslangic.set(Calendar.DAY_OF_MONTH, datePicker3.dayOfMonth)
            calendarBaslangic.set(Calendar.MINUTE, 0)
            calendarBaslangic.set(Calendar.HOUR, -9)
            calendarBaslangic.set(Calendar.SECOND, 0)
            val timestampBaslangic: Long = calendarBaslangic.timeInMillis / 1000
            val calendarBitis: Calendar = Calendar.getInstance()
            calendarBitis.set(Calendar.YEAR, datePicker3.year)
            calendarBitis.set(Calendar.MONTH, datePicker3.month)
            calendarBitis.set(Calendar.DAY_OF_MONTH, datePicker3.dayOfMonth)
            calendarBitis.set(Calendar.MINUTE, 59)
            calendarBitis.set(Calendar.HOUR, 14)
            calendarBitis.set(Calendar.SECOND, 59)
            val timestampBitis: Long = calendarBitis.timeInMillis / 1000
            var baslangicKonumLat: Double = 0.0
            var baslangicKonumLon: Double = 0.0
            var bitisKonumLat: Double = 0.0
            var bitisKonumLon: Double = 0.0
            firebaseAPI.getTip3(timestampBaslangic, timestampBitis)
                .addOnSuccessListener { documents ->
                    val dataList: List<TripData> = documents.toObjects(TripData::class.java)
                    val list_filtered = dataList.sortedByDescending { it.trip_distance }.take(1) // trip distance göre sıradalık ama bu sefer tersten. 1 tane aldık.
                    sorgu3.text = list_filtered.get(0).trip_distance.toString() + "km" // km yi haritanın üzerindeki text e attık.
                    firebaseAPI.getLocation(list_filtered.get(0).PULocationID) // databaseden baslangıç lokasyonunu getirdik.
                        .addOnSuccessListener { document ->
                            googleMap.clear()
                            val dataZoneBaslangic = document.toObjects(ZoneData::class.java)
                            baslangicKonumLat = dataZoneBaslangic.get(0).longitude
                            baslangicKonumLon = dataZoneBaslangic.get(0).latitude
                            val position = LatLng(baslangicKonumLat, baslangicKonumLon)
                            googleMap.addMarker( // basşlangıç lokasyonuna haritada marker ekledik.
                                MarkerOptions()
                                    .position(position)
                                    .title(dataZoneBaslangic.get(0).zone)
                                    .snippet("Baslangic Noktasi")
                            )
                            firebaseAPI.getLocation(list_filtered.get(0).DOLocationID) // bitiş lokasyonu.
                                .addOnSuccessListener { document ->
                                    val dataZoneBitis = document.toObjects(ZoneData::class.java)
                                    bitisKonumLat = dataZoneBitis.get(0).longitude
                                    bitisKonumLon = dataZoneBitis.get(0).latitude

                                    val position = LatLng(bitisKonumLat, bitisKonumLon)
                                    googleMap.addMarker( // bitiş lokasyonu markeri.
                                        MarkerOptions()
                                            .position(position)
                                            .title(dataZoneBitis.get(0).zone)
                                            .snippet("Bitis Noktasi")
                                    )
                                    googleMap.addPolyline( // siyah çizgi.
                                        PolylineOptions()
                                            .clickable(true)
                                            .add(
                                                LatLng(baslangicKonumLat, baslangicKonumLon),
                                                LatLng(bitisKonumLat, bitisKonumLon)
                                            )
                                    )
                                    val australiaBounds = LatLngBounds( // haritayı konuma göre ortalıyoruz.
                                        LatLng(baslangicKonumLat - 5, baslangicKonumLon - 5),
                                        LatLng(baslangicKonumLat + 5, baslangicKonumLon + 5)
                                    )
                                    googleMap.moveCamera( // ortalama değerlerine göre ekrana yansıttık.
                                        CameraUpdateFactory.newLatLngZoom(
                                            australiaBounds.center,
                                            10f
                                        )
                                    )
                                    val url =  getUrl(LatLng(baslangicKonumLat, baslangicKonumLon), LatLng( bitisKonumLat, bitisKonumLon), "driving")
                                    request(url)
                                }
                        }
                }
        }
    }

    override fun onMapReady(gMap: GoogleMap?) {
        googleMap = gMap!!
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    fun datePickerMaxMinValue() {
        datePicker2Baslangic.minDate = 1606780799000L
        datePicker2Baslangic.maxDate = 1609444800000L
        datePicker2Bitis.minDate = 1606780799000L
        datePicker2Bitis.maxDate = 1609444800000L
        datePicker3.minDate = 1606780799000L
        datePicker3.maxDate = 1609444800000L
    }

    fun initUI() {
        btnSorgu1 = findViewById(R.id.btnSorgu1)
        btnSorgu2 = findViewById(R.id.btnSorgu2)
        btnSorgu3 = findViewById(R.id.btnSorgu3)
        datePicker2Baslangic = findViewById(R.id.datePicker2Baslangic)
        datePicker2Bitis = findViewById(R.id.datePicker2Bitis)
        recylcerViewSorgu1 = findViewById(R.id.recyclerSonuc1)
        recylcerViewSorgu2 = findViewById(R.id.recyclerView2)
        datePicker3 = findViewById(R.id.datePicker3)
        sorgu3 = findViewById(R.id.sorgu3Km)
    }

    fun initMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    fun initFirebase() {
        firebaseAPI = FirebaseAPI()
        firebaseAPI.initFirestore()
    }

    private fun getUrl(origin: LatLng, dest: LatLng, directionMode: String): String? { // direction mode yi driving yaptık.
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude
        val mode = "mode=$directionMode"
        val parameters = "$str_origin&$str_dest&$mode"
        val output = "json"
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(
            R.string.google_maps_key
        )
    }

    private fun request(url: String?) {
        val queue = Volley.newRequestQueue(this)
        val path: MutableList<List<LatLng>> = ArrayList()
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                val jsonResponse = JSONObject(response)
                val routes = jsonResponse.getJSONArray("routes")
                val legs = routes.getJSONObject(0).getJSONArray("legs")
                val steps = legs.getJSONObject(0).getJSONArray("steps")
                for (i in 0 until steps.length()) {
                    val points =
                        steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                    path.add(PolyUtil.decode(points))
                }
                for (i in 0 until path.size) {
                    this.googleMap!!.addPolyline(PolylineOptions().addAll(path[i]).color(Color.RED)) // google sunucularına istek attık gelen sonucu polyline olarak yani çzigi olarak ekrana yansıttık (kırmızı çizgi)
                }
            },
            {
                Log.d("deneme", it.message.toString())
            })
        queue.add(stringRequest)
    }

}