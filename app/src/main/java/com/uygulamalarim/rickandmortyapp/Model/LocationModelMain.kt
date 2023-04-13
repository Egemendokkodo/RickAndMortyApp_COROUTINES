package com.uygulamalarim.rickandmortyapp

import com.google.gson.annotations.SerializedName


data class LocationModelMain(
    @SerializedName("info")
    val info: İnfo,
    @SerializedName("results")
    val results: List<Result>
)

data class İnfo(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("prev")
    val prev: Any
)

data class Result(
    @SerializedName("created")
    val created: String,
    @SerializedName("dimension")
    val dimension: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("residents")
    val residents: List<String>,
    @SerializedName("type")
    val type: String,
    @SerializedName("url")
    val url: String
)