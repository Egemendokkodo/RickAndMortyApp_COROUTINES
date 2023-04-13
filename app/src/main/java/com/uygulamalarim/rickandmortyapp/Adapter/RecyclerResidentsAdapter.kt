package com.uygulamalarim.rickandmortyapp.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uygulamalarim.rickandmortyapp.CharacterDetails
import com.uygulamalarim.rickandmortyapp.R
import com.uygulamalarim.rickandmortyapp.Retrofit.RickAndMortyApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class RecyclerResidentsAdapter(
    private val context: Context,
    private val list: List<CharacterDetails>
) : RecyclerView.Adapter<RecyclerResidentsAdapter.ViewHolder>() {

    private val characterDetailsList = mutableListOf<CharacterDetails>()
    private var onResidentIdsClickListener: ItemClickListener? = null

    init {
        characterDetailsList.addAll(list)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_earth_residents, parent, false)
        return ViewHolder(itemView)
    }

    inner class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view), View.OnClickListener {
        var characterPic: ImageView = view.findViewById(R.id.characterPic)
        var genderPic: ImageView = view.findViewById(R.id.genderPic)
        var characterName: TextView = view.findViewById(R.id.characterName)
        var idTextHidden: TextView = view.findViewById(R.id.idTextHidden)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val id = idTextHidden.text.toString()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://rickandmortyapi.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val apiService = retrofit.create(RickAndMortyApi::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                val idMoreCharacterDetail = apiService.getMoreDetailCharacter(id)
                Log.d("idMoreCharacterDetail::", idMoreCharacterDetail.toString())
                if (!id.isNullOrEmpty()) {
                    onResidentIdsClickListener?.onItemClicked(id)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = characterDetailsList[position]
        Glide.with(context).load(currentItem.image).into(holder.characterPic)
        holder.characterName.text = currentItem.name.toString()
        holder.idTextHidden.text = currentItem.id.toString()
        when (currentItem.gender) {
            "Female" -> Glide.with(context).load(R.drawable.femaleicon).into(holder.genderPic)
            "Male" -> Glide.with(context).load(R.drawable.maleicon).into(holder.genderPic)
            else -> Glide.with(context).load(R.drawable.unknownicon).into(holder.genderPic)
        }
    }

    override fun getItemCount(): Int {
        return characterDetailsList.size
    }

    fun updateList(newList: List<CharacterDetails>) {
        characterDetailsList.clear()
        characterDetailsList.addAll(newList)
        notifyDataSetChanged()
    }

    interface ItemClickListener {
        fun onItemClicked(id: String)
    }

    fun setOnCharacterClickListener(listener: ItemClickListener) {
        onResidentIdsClickListener = listener
    }
}

