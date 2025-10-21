package com.example.appmoive.data.model

import com.google.gson.annotations.SerializedName

data class Review(val author: String, val content: String, @SerializedName("author_details") val authorDetails: AuthorDetails)