package com.example.tiktokdownloaded.view.update_download

import kotlinx.coroutines.CoroutineScope

interface UpdateInterface {
    fun updateInterface(updateDownload: UpdateDownload)
    fun cancel(coroutineScope: CoroutineScope)
}