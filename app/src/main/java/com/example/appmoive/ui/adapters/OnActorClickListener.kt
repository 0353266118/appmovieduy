// file: ui/adapters/OnActorClickListener.kt
package com.example.appmoive.ui.adapters

import com.example.appmoive.data.model.Cast

// interface xử lí sự kiện khi click vào item diễn viên
interface OnActorClickListener {
    fun onActorClick(actor: Cast)
}