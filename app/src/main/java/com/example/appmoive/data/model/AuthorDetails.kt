// file: data/model/AuthorDetails.kt
package com.example.appmoive.data.model


// class chứa thông tin về người viết review
data class AuthorDetails(
    val rating: Double? // Dùng Double? vì có thể một số review không có rating
)