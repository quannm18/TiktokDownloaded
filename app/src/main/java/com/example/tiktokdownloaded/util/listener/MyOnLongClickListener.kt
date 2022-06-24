package com.example.tiktokdownloaded.util.listener

import com.example.tiktokdownloaded.model.TikTokEntity

interface MyOnLongClickListener {
    fun itemCheckBoxLongClickListener(isShow: Boolean)

    fun cancelDialog()

    fun deleteThisItem(item : TikTokEntity)

    fun shareItem(item : TikTokEntity)
}