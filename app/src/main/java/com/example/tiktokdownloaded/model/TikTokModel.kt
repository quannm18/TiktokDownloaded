package com.example.tiktokdownloaded.model

import com.google.gson.annotations.SerializedName

data class TikTokModel (
    @SerializedName("aweme_detail")
    val awemeDetail: AwemeDetail?
)
data class AwemeDetail (
    @SerializedName("author")
    val author: Author,
    @SerializedName("video")
    val video : Video,
    @SerializedName("music")
    val music: Music,
    @SerializedName("share_info")
    val shareInfo: ShareInfo,
    @SerializedName("desc")
    val desc: String
)
data class Author(
    @SerializedName("nickname")
    val nickname:String
)
data class Video(
    @SerializedName("origin_cover")
    val origin_cover: OriginCover,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("play_addr")
    val play_addr: PlayAddr,
)
data class Music(
    @SerializedName("play_url")
    val playUrlMusic: PlayUrlMusic
)

data class PlayUrlMusic(
    @SerializedName("uri")
    val uri: String
)
data class OriginCover(
    @SerializedName("url_list")
    val url_list : List<String>
)
data class PlayAddr(
    @SerializedName("url_list")
    val url_list : List<String>
)
data class ShareInfo(
    @SerializedName("share_desc")
    val share_desc:String
)

