package com.example.appmoive.data.model

import com.google.gson.annotations.SerializedName

// đối tượng diễn viên lấy từ API
data class Cast(val name: String, @SerializedName("profile_path") val profilePath: String?)