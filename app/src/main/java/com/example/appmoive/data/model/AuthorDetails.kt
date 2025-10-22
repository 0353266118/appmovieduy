// file: data/model/AuthorDetails.kt
package com.example.appmoive.data.model

data class AuthorDetails(
    val rating: Double? // Dùng Double? vì có thể một số review không có rating
)