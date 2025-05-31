package com.example.sportakip


import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


data class AntrenmanVerisi(
    val tur: String,
    val sureDakika: Int,
    val tarih: String,
    val not: String = "" // Varsayılan boş
)

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("antrenman_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _antrenmanListesi = MutableLiveData<MutableList<AntrenmanVerisi>>(mutableListOf())
    val antrenmanListesi: LiveData<MutableList<AntrenmanVerisi>> = _antrenmanListesi
    val antrenmanSayisi: LiveData<Int> = MutableLiveData<Int>().apply {
        value = _antrenmanListesi.value?.size ?: 0
    }

    init {
        verileriYukle()
    }
    fun antrenmanSil(index: Int) {
        val guncelListe = _antrenmanListesi.value ?: return
        if (index in 0 until guncelListe.size) {
            guncelListe.removeAt(guncelListe.size - 1 - index) // reverse ettiğimiz için ters
            _antrenmanListesi.value = guncelListe
            (antrenmanSayisi as MutableLiveData).value = guncelListe.size
            verileriKaydet()
        }
    }

    fun antrenmanEkle(veri: AntrenmanVerisi) {
        val guncelListe = _antrenmanListesi.value ?: mutableListOf()
        guncelListe.add(veri)
        _antrenmanListesi.value = guncelListe
        (antrenmanSayisi as MutableLiveData).value = guncelListe.size
        verileriKaydet()
    }

    private fun verileriKaydet() {
        val json = gson.toJson(_antrenmanListesi.value)
        prefs.edit().putString("antrenmanlar", json).apply()
    }

    private fun verileriYukle() {
        val json = prefs.getString("antrenmanlar", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<AntrenmanVerisi>>() {}.type
            val liste: MutableList<AntrenmanVerisi> = gson.fromJson(json, type)
            _antrenmanListesi.value = liste
            (antrenmanSayisi as MutableLiveData).value = liste.size
        }
    }
}