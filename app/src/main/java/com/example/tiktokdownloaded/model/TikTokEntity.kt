package com.example.tiktokdownloaded.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tiktok_table")
data class TikTokEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var title: String,
    val urlVideo: String,
    val urlMusic: String,
    val urlThumbnail: String,
    val author: String,
    val duration: String,
    val fileName: String,
    val date: String,
    val process: Int,
    )

enum class StateDownload{
    WAITING, DOWNLOADING, DOWNLOADED, ERROR
}

fun convertTikTok(tikTokModel: TikTokModel, date: String, fileName: String, mProcess: Int): TikTokEntity {
    return TikTokEntity(
        id = 0,
        title = tikTokModel.awemeDetail!!.desc,
        urlVideo = tikTokModel.awemeDetail.video.play_addr.url_list[2],
        urlMusic = tikTokModel.awemeDetail.music.playUrlMusic.uri,
        urlThumbnail = tikTokModel.awemeDetail.video.origin_cover.url_list[0],
        author = tikTokModel.awemeDetail.author.nickname,
        duration = tikTokModel.awemeDetail.video.duration,
        fileName = fileName,
        date = date,
        process = mProcess
    )
}