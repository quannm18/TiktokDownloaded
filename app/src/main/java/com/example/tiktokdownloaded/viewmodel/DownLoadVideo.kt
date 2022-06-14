package com.example.tiktokdownloaded.viewmodel

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.tiktokdownloaded.model.TikTokModel

class DownLoadVideo {
    var mydownloadid : Long = 0
    public fun download(tikTokModel: TikTokModel, context: Context){
        Log.d("TAG", "download: ${tikTokModel.awemeDetail!!.video.play_addr.url_list.get(2)}")
        var request = DownloadManager.Request(
            Uri.parse(tikTokModel.awemeDetail.video.play_addr.url_list.get(2))
        ).setTitle(tikTokModel?.awemeDetail?.shareInfo?.share_desc)
            .setDescription(tikTokModel.awemeDetail.author.nickname)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setAllowedOverMetered(true)

        var dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        mydownloadid = dm.enqueue(request)

        var br = object : BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                var  id = p1?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1)
                if (id==mydownloadid){
                    Toast.makeText(p0,"Completed", Toast.LENGTH_LONG).show()
                }
            }

        }
        context.registerReceiver(br, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

}