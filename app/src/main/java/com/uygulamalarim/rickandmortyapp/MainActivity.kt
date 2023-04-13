package com.uygulamalarim.rickandmortyapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uygulamalarim.rickandmortyapp.Adapter.RecyclerLocationAdapter
import com.uygulamalarim.rickandmortyapp.Adapter.RecyclerResidentsAdapter
import com.uygulamalarim.rickandmortyapp.Retrofit.RetrofitClient
import com.uygulamalarim.rickandmortyapp.View.DetailedPage
import kotlinx.coroutines.*
class MainActivity : AppCompatActivity(), RecyclerLocationAdapter.OnResidentIdsClickListener,RecyclerResidentsAdapter.ItemClickListener {

    private val list = ArrayList<LocationModelMain>()

    private lateinit var adapterLocation: RecyclerLocationAdapter
    private lateinit var recyclerviewLocation: RecyclerView
    private lateinit var locationList:ArrayList<LocationModelMain>
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var recyclerviewCharacters:RecyclerView
    private lateinit var adapterCharacter: RecyclerResidentsAdapter
    private lateinit var characterList: ArrayList<CharacterDetails>
    private lateinit var layoutManagerCharacter: LinearLayoutManager

    private var currentPage = 1
    private var totalPages = 1

    private lateinit var progressBar:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar=findViewById(R.id.progressBar)
        locationList= arrayListOf()
        recyclerviewLocation=findViewById(R.id.recyclerviewLocation)

        recyclerviewCharacters=findViewById(R.id.recyclerviewCharacters)
        layoutManagerCharacter=LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        recyclerviewCharacters.layoutManager =layoutManagerCharacter
        adapterCharacter = RecyclerResidentsAdapter(this@MainActivity,listOf())
        adapterCharacter.setOnCharacterClickListener(this)
        recyclerviewCharacters.adapter = adapterCharacter





        layoutManager=LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
        recyclerviewLocation.layoutManager =layoutManager
        adapterLocation = RecyclerLocationAdapter(list)
        recyclerviewLocation.adapter = adapterLocation
        adapterLocation.setOnResidentIdsClickListener(this)


        lazyLoad()

        // the app starts here
        loadPage(1)



    }
    override fun onItemClicked(id: String) {
        Log.d("MAİNACTİVİTY İD ",id.toString())
        val i:Intent= Intent(this,DetailedPage::class.java)
        i.putExtra("id",id)
        startActivity(i)
    }

    private fun lazyLoad() {
        progressBar.visibility = View.GONE
        recyclerviewLocation.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            var isLoading = false

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && firstVisibleItemPosition + visibleItemCount >= totalItemCount) {
                    isLoading = true

                    if (currentPage < totalPages) {
                        currentPage++
                        loadPage(currentPage)
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isLoading = false
                }
            }
        })
    }

    private fun loadPage(page: Int) {

        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.getLocations(page)
                totalPages = response.info.pages

                withContext(Dispatchers.Main) {
                    val locationList = ArrayList<LocationModelMain>()
                    for (i in 0 until response.results.size) {
                        locationList.add(response)
                    }
                    Log.d("LOCATİONLİST::",locationList.toString())
                    list.addAll(locationList)


                    delay(3000)
                    progressBar.visibility = View.GONE
                    adapterLocation.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", e.message ?: "")
                progressBar.visibility = View.GONE
            }
        }
    }



    override suspend fun onResidentIdsClick(residentsIds: List<Int>) {
        Log.d("onResidentIdsClick::", residentsIds.toString())

        val characterDetailsList = mutableListOf<CharacterDetails>()
        val api = RetrofitClient.api
        if (!residentsIds.isNullOrEmpty()){
            val jobs = residentsIds.map { residentsIds ->
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        val characterDetails = api.getCharacterDetails(residentsIds)
                        characterDetailsList.add(characterDetails)
                        Log.d("CHARACTERS:", characterDetails.toString())
                        adapterCharacter.updateList(characterDetailsList)
                        adapterCharacter.notifyDataSetChanged()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            jobs.forEach { it.join() }
    }else {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    withContext(Dispatchers.Main) {
                        characterDetailsList.clear()
                        adapterCharacter.updateList(characterDetailsList)
                        adapterCharacter.notifyDataSetChanged()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }


    }

}

