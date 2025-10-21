package com.example.appmoive.data.model

import com.google.gson.annotations.SerializedName

// class đại diện cho 1 comment

data class Review(val author: String, val content: String, @SerializedName("author_details") val authorDetails: AuthorDetails)