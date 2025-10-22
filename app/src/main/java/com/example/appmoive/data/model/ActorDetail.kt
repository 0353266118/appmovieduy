// file: data/model/ActorDetail.kt
package com.example.appmoive.data.model

import com.google.gson.annotations.SerializedName

data class ActorDetail(
    val id: Int,
    val name: String,
    @SerializedName("profile_path") val profilePath: String?,
    val birthday: String?,
    @SerializedName("place_of_birth") val placeOfBirth: String?
    // Em có thể thêm biography, v.v. nếu muốn
)