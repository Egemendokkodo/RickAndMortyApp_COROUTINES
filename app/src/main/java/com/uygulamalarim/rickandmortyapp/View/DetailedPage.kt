package com.uygulamalarim.rickandmortyapp.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.uygulamalarim.rickandmortyapp.LocationModelMain
import com.uygulamalarim.rickandmortyapp.R
import com.uygulamalarim.rickandmortyapp.Retrofit.RetrofitClient
import kotlinx.coroutines.*

class DetailedPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_page)
        val i:Intent=intent
        val id=i.getStringExtra("id")
        Log.d("INTENTTEKİ VERİ::",id.toString())
        val statusTv:TextView=findViewById(R.id.statusTv)
        val specyTv:TextView=findViewById(R.id.specyTv)
        val genderTv:TextView=findViewById(R.id.genderTv)
        val originTv:TextView=findViewById(R.id.originTv)
        val locationTv:TextView=findViewById(R.id.locationTv)
        val episodesTv:TextView=findViewById(R.id.episodesTv)
        val createdatTv:TextView=findViewById(R.id.createdatTv)
        val characterNameTv:TextView=findViewById(R.id.characterNameTv)
        val characterPicDetailed:ImageView=findViewById(R.id.characterPicDetailed)

        val backbtn:ImageButton=findViewById(R.id.backbtn)

        backbtn.setOnClickListener {
            this.finish()
        }


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.getMoreDetailCharacter(id.toString())

                withContext(Dispatchers.Main) {
                    statusTv.text = response.status.toString()
                    specyTv.text = response.species.toString()
                    genderTv.text = response.gender.toString()
                    originTv.text = response.origin.name.toString()
                    locationTv.text = response.location.name.toString()
                    createdatTv.text = response.created.toString()
                    characterNameTv.text = response.name.toString()
                    Glide.with(applicationContext).load(response.image).into(characterPicDetailed)

                    val episodeNumbers = response.episode.map { it.split("/").last() }
                    episodesTv.text = episodeNumbers.joinToString(", ")





                }
            } catch (e: Exception) {
                Log.e("API_ERROR", e.message ?: "")

            }
        }






    }

}