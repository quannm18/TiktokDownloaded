package com.example.tiktokdownloaded.view.fragment

import android.Manifest
import android.app.DownloadManager
import android.content.*
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.Context.DOWNLOAD_SERVICE
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.tiktokdownloaded.MainViewModelFactory
import com.example.tiktokdownloaded.R
import com.example.tiktokdownloaded.model.TikTokEntity
import com.example.tiktokdownloaded.model.TikTokModel
import com.example.tiktokdownloaded.repository.Repository
import com.example.tiktokdownloaded.viewmodel.MainViewModel
import com.example.tiktokdownloaded.viewmodel.TikTokViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.imgPreviewRow
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.row_preview_after_download.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration


class HomeFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private var downloadID: Long = 0
    lateinit var  tikTokModel: TikTokModel ;
    private var isShow : Int = 0;
    private lateinit var tikTokViewModel: TikTokViewModel
    private var isSuccess: Boolean = false

    private lateinit var fileNameDB : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.layoutPreView.visibility = View.GONE
        view.btnDownloadVideo.visibility = View.GONE
        view.btnDownloadAudio.visibility = View.GONE
        view.btnDownloadThumb.visibility = View.GONE

        tikTokViewModel = ViewModelProvider(this).get(TikTokViewModel::class.java)
        
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(MainViewModel::class.java)
        tilFind.editText!!.setOnFocusChangeListener { _, focus ->
            run {
                if (focus) {
                    btnDownload.alpha = 1F;

                }else{
                    btnDownload.alpha = 0.5F;
                }
            }
        }

        imgPreviewRow.setOnClickListener(View.OnClickListener {
            tikTokViewModel.addTikTok(TikTokEntity(0,"abc","https://v16m.byteicdn.com/1a805e6e130da6ebbde8c45f31968362/62b19137/video/tos/useast2a/tos-useast2a-pve-0037-aiso/93aa4c8bcdbc43e7b4d9b823ec7d68cf/?a=0&ch=0&cr=0&dr=0&lr=all&cd=0%7C0%7C0%7C0&cv=1&br=1954&bt=977&btag=80000&cs=0&ds=6&ft=L4cJSoTzDJhNvyBiZqBaRff3nfpBO5njkhp2z7&mime_type=video_mp4&qs=0&rc=ZzVkMzs2ZjxpMzc1OGU1NEBpanJ1NTc6Zm9nZDMzZjgzM0A1Ml4yM182XzUxXjRiL15iYSM2XnMxcjRfLmVgLS1kL2Nzcw%3D%3D&l=202206210336340101901920202060416B&cc=10","https://v16m.byteicdn.com/1a805e6e130da6ebbde8c45f31968362/62b19137/video/tos/useast2a/tos-useast2a-pve-0037-aiso/93aa4c8bcdbc43e7b4d9b823ec7d68cf/?a=0&ch=0&cr=0&dr=0&lr=all&cd=0%7C0%7C0%7C0&cv=1&br=1954&bt=977&btag=80000&cs=0&ds=6&ft=L4cJSoTzDJhNvyBiZqBaRff3nfpBO5njkhp2z7&mime_type=video_mp4&qs=0&rc=ZzVkMzs2ZjxpMzc1OGU1NEBpanJ1NTc6Zm9nZDMzZjgzM0A1Ml4yM182XzUxXjRiL15iYSM2XnMxcjRfLmVgLS1kL2Nzcw%3D%3D&l=202206210336340101901920202060416B&cc=10","https://v16m.byteicdn.com/1a805e6e130da6ebbde8c45f31968362/62b19137/video/tos/useast2a/tos-useast2a-pve-0037-aiso/93aa4c8bcdbc43e7b4d9b823ec7d68cf/?a=0&ch=0&cr=0&dr=0&lr=all&cd=0%7C0%7C0%7C0&cv=1&br=1954&bt=977&btag=80000&cs=0&ds=6&ft=L4cJSoTzDJhNvyBiZqBaRff3nfpBO5njkhp2z7&mime_type=video_mp4&qs=0&rc=ZzVkMzs2ZjxpMzc1OGU1NEBpanJ1NTc6Zm9nZDMzZjgzM0A1Ml4yM182XzUxXjRiL15iYSM2XnMxcjRfLmVgLS1kL2Nzcw%3D%3D&l=202206210336340101901920202060416B&cc=10",
            "Quan Ngo","09:54", "abc.mp4","18/09/2002"))
            Toast.makeText(requireContext(),"Insert",Toast.LENGTH_SHORT).show()
        })
        btnDownload.setOnClickListener(View.OnClickListener {

            val txtFind = tilFind.editText!!.text.toString();
            tilFind.error = setErrorForTI(txtFind)

            if (setErrorForTI(txtFind)!=""){
                return@OnClickListener
            }
            view.layoutPreView.visibility = View.VISIBLE
            view.btnDownloadVideo.visibility = View.VISIBLE
            view.btnDownloadAudio.visibility = View.VISIBLE
            view.btnDownloadThumb.visibility = View.VISIBLE
            val id = getIDFromUrl(txtFind)
            viewModel.getPostTikTok(id as String)
            viewModel.myResponse.observe(viewLifecycleOwner, Observer {
                    response->
                if (response.isSuccessful){
                    tikTokModel = TikTokModel(null);
                    Log.e("awemeDetail",response.body().toString())
                    tikTokModel = response.body()!!

                    val linkImage = tikTokModel.awemeDetail!!.video.origin_cover.url_list.get(1)
                    val millis:String = tikTokModel.awemeDetail!!.video.duration
                    val titleVideo : String = tikTokModel.awemeDetail!!.desc
                    val tagName : String = tikTokModel.awemeDetail!!.author.nickname
                    val duration = millis.toInt().toDuration(DurationUnit.MILLISECONDS)
                    val timeString = duration.toComponents { m, s, _ ->
                        String.format("%02d:%02d", m, s)
                    }
                    Glide.with(imgPreviewRow)
                        .load(Uri.parse(linkImage))
                        .centerCrop()
                        .into(imgPreviewRow)

                    view.tvTimeVideoRow.setText(timeString)
                    view.tvTitleRow.setText(titleVideo)
                    view.tvSubtitle.setText("@${tagName}")

                    isShow = 1
                }else{
                    tilFind.editText!!.setText(response.code().toString())
                }
            })
            Log.e("id","$id")
        })
        btnPaste.setOnClickListener(View.OnClickListener {
            tilFind.editText!!.setText(pasteData())
            btnDownload.alpha = 1F;
        })

        btnDownloadVideo.setOnClickListener(View.OnClickListener {

            haveStoragePermission(requireContext(),tikTokModel,0)

        })
        btnDownloadAudio.setOnClickListener(View.OnClickListener {

            haveStoragePermission(requireContext(),tikTokModel,1)
        })
        btnDownloadThumb.setOnClickListener(View.OnClickListener {
            val thumbnail = tikTokModel.awemeDetail!!.video.origin_cover
            haveStoragePermission(requireContext(),tikTokModel,2)
        })
    }

    private fun getIDFromUrl(txtFind: String): Any {
        var id = ""
        var indexOfID:Int = 0;
        var demoID = "7103276878366051611"
        if (txtFind!=null){
            indexOfID = txtFind.indexOf("/video/",0,true).toInt();
            Log.e("Log", "index: $indexOfID - ${indexOfID+demoID.length} - ${txtFind.length}" )

            id = txtFind.substring(indexOfID+7 until indexOfID+demoID.length+7)
        }
        return id;
    }

    private fun setErrorForTI(txtFind: String): CharSequence? {
        if (txtFind.length<5){
            return "Link fail. Please renew link"
        }else if (!txtFind.contains("/video/")){
            return "This is mobile link"
        }
        return ""
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    fun downloadVideo(url: String?, fileName:String) {
        val download_Uri: Uri = Uri.parse(url)
        val request = DownloadManager.Request(download_Uri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setAllowedOverRoaming(false)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setTitle("Downloading")
        request.setDescription("Downloading File")
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_MOVIES,
            "$fileName.mp4"
        )
        val downloadManager = requireContext().getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)
        var br = object : BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                var  id = p1?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1)
                if (id==downloadID){
                    isSuccess =true
                    Toast.makeText(p0,"Completed", Toast.LENGTH_LONG).show()

                    fileNameDB = p1?.getStringExtra(DownloadManager.COLUMN_TITLE).toString()
                    val date = getCurrentDateTime()
                    val dateInString = date.toString("dd/MM/yyyy")
                    tikTokViewModel.addTikTok(convertTikTok(tikTokModel, dateInString, fileName))
                    Toast.makeText(requireContext(),"Insert",Toast.LENGTH_SHORT).show()
                }
            }

        }
        requireContext().registerReceiver(br, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }
    fun downloadAudio(url: String?, fileName:String) {
        val download_Uri: Uri = Uri.parse(url)
        val request = DownloadManager.Request(download_Uri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setAllowedOverRoaming(false)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setTitle("Downloading")
        request.setDescription("Downloading File")
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_MOVIES,
            "$fileName.mp3"
        )
        val downloadManager = requireContext().getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)
        var br = object : BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                var  id = p1?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1)
                if (id==downloadID){
                    Toast.makeText(p0,"Completed", Toast.LENGTH_LONG).show()
                }
            }

        }
        requireContext().registerReceiver(br, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

    }
    fun downloadThumb(url: String?, fileName:String) {
        val download_Uri: Uri = Uri.parse(url)
        val request = DownloadManager.Request(download_Uri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setAllowedOverRoaming(false)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setTitle("Downloading")
        request.setDescription("Downloading File")
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_MOVIES,
            "$fileName.jpg"
        )
        val downloadManager = requireContext().getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)
        var br = object : BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                var  id = p1?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1)
                if (id==downloadID){
                    Toast.makeText(p0,"Completed", Toast.LENGTH_LONG).show()
                }
            }

        }
        requireContext().registerReceiver(br, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

    }
    fun haveStoragePermission(context: Context, tikTokModel: TikTokModel, type: Int): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("Permission error", "You have permission")
                Log.e("link", tikTokModel.awemeDetail!!.video.play_addr.url_list[1])
                val fileName = "tiktok_${System.currentTimeMillis()}"
                if (type==0){
                    downloadVideo(tikTokModel.awemeDetail!!.video.play_addr.url_list[2], fileName)
                }else if (type==1){
                    downloadAudio(tikTokModel.awemeDetail!!.music.playUrlMusic.uri, fileName)
                }else if (type ==2){
                    downloadThumb(tikTokModel.awemeDetail!!.video.origin_cover.url_list.get(0), fileName)
                }

                true
            } else {
                Log.e("Permission error", "You have asked for permission")
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else { //you dont need to worry about these stuff below api level 23
            Log.e("Permission error", "You already have the permission")
            true
        }
    }
    fun pasteData() : String{
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        var pasteData = ""
        if (!clipboard!!.hasPrimaryClip()) {
        } else if (!clipboard!!.primaryClipDescription!!.hasMimeType(MIMETYPE_TEXT_PLAIN)) {

            // since the clipboard has data but it is not plain text
        } else {

            //since the clipboard contains plain text.
            val item = clipboard!!.primaryClip!!.getItemAt(0)

            // Gets the clipboard as text.
            pasteData = item.text.toString()
        }
        return pasteData
    }

    fun convertTikTok(tikTokModel: TikTokModel, date: String, fileName: String): TikTokEntity {
        return TikTokEntity(
            id = 0,
            title = tikTokModel.awemeDetail!!.desc,
            urlVideo = tikTokModel.awemeDetail.video.play_addr.url_list[2],
            urlMusic = tikTokModel.awemeDetail.music.playUrlMusic.uri,
            urlThumbnail = tikTokModel.awemeDetail.video.origin_cover.url_list[0],
            author = tikTokModel.awemeDetail.author.nickname,
            duration = tikTokModel.awemeDetail.video.duration,
            fileName = fileName,
            date = date
        )
    }
    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}
