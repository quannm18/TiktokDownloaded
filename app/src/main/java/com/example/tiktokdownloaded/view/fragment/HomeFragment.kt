package com.example.tiktokdownloaded.view.fragment

import android.Manifest
import android.app.DownloadManager
import android.content.*
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.Context.DOWNLOAD_SERVICE
import android.content.pm.PackageManager
import android.database.Cursor
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
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.tiktokdownloaded.R
import com.example.tiktokdownloaded.model.TikTokEntity
import com.example.tiktokdownloaded.model.TikTokModel
import com.example.tiktokdownloaded.network.repository.Repository
import com.example.tiktokdownloaded.util.DateString.Companion.dateInString
import com.example.tiktokdownloaded.view.update_download.UpdateDownload
import com.example.tiktokdownloaded.viewmodel.MainViewModel
import com.example.tiktokdownloaded.viewmodel.MainViewModelFactory
import com.example.tiktokdownloaded.viewmodel.TikTokViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration


class HomeFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private var downloadID: Long = 0
    lateinit var tikTokModel: TikTokModel
    private var isShow: Int = 0
    private lateinit var tikTokViewModel: TikTokViewModel
    private var isSuccess: Boolean = false
    private var localUriVideo = ""

    private var process: Long = 0;
    private lateinit var fileNameDB: String

    private lateinit var updateDownload: UpdateDownload
    private var percent: Long = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tikTokModel = TikTokModel(null)
        view.layoutPreView.visibility = View.GONE
        view.btnDownloadVideo.visibility = View.GONE
        view.btnDownloadAudio.visibility = View.GONE
        view.btnDownloadThumb.visibility = View.GONE

        tikTokViewModel = ViewModelProvider(this).get(TikTokViewModel::class.java)

        updateDownload = ViewModelProvider(this).get(UpdateDownload::class.java)
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        tilFind.editText!!.setOnFocusChangeListener { _, focus ->
            run {
                if (focus) {
                    btnDownload.alpha = 1F

                } else {
                    view.layoutPreView.visibility = View.GONE
                    view.btnDownloadVideo.visibility = View.GONE
                    view.btnDownloadAudio.visibility = View.GONE
                    view.btnDownloadThumb.visibility = View.GONE
                    btnDownload.alpha = 0.5F
                }
            }
        }

        btnDownload.setOnClickListener(View.OnClickListener {

            val txtFind = tilFind.editText!!.text.toString()
            tilFind.error = setErrorForTI(txtFind)

            if (setErrorForTI(txtFind) != "") {
                return@OnClickListener
            }
            view.layoutPreView.visibility = View.VISIBLE
            view.btnDownloadVideo.visibility = View.VISIBLE
            view.btnDownloadAudio.visibility = View.VISIBLE
            view.btnDownloadThumb.visibility = View.VISIBLE
            val id = getIDFromUrl(txtFind)
            viewModel.getPostTikTok(id as String)
            viewModel.myResponse.observe(viewLifecycleOwner) { response ->
                if (response.isSuccessful) {
                    tikTokModel = TikTokModel(null)
                    Log.e("awemeDetail", response.body().toString())
                    tikTokModel = response.body()!!

                    val linkImage = tikTokModel.awemeDetail!!.video.origin_cover.url_list.get(1)
                    val millis: String = tikTokModel.awemeDetail!!.video.duration
                    val titleVideo: String = tikTokModel.awemeDetail!!.desc
                    val tagName: String = tikTokModel.awemeDetail!!.author.nickname
                    val duration = millis.toInt().toDuration(DurationUnit.MILLISECONDS)
                    val timeString = duration.toComponents { m, s, _ ->
                        String.format("%02d:%02d", m, s)
                    }
                    Glide.with(imgPreviewRow)
                        .load(Uri.parse(linkImage))
                        .centerCrop()
                        .into(imgPreviewRow)

                    view.tvTimeVideoRow.text = timeString
                    view.tvTitleRow.text = titleVideo
                    view.tvSubtitle.text = "@${tagName.trim()}"

                    isShow = 1
                } else {
                    tilFind.editText!!.setText(response.code().toString())
                }
            }
            Log.e("id", "$id")
        })
        btnPaste.setOnClickListener {
            tilFind.editText!!.setText(pasteData())
            btnDownload.alpha = 1F
        }

        btnDownloadVideo.setOnClickListener(View.OnClickListener {
            haveStoragePermission(requireContext(), tikTokModel, 0)
        })
        btnDownloadAudio.setOnClickListener(View.OnClickListener {

            haveStoragePermission(requireContext(), tikTokModel, 1)
        })
        btnDownloadThumb.setOnClickListener(View.OnClickListener {
            val thumbnail = tikTokModel.awemeDetail!!.video.origin_cover
            haveStoragePermission(requireContext(), tikTokModel, 2)
        })
    }

    private fun getIDFromUrl(txtFind: String): Any {
        var id = ""
        var indexOfID: Int = 0
        if (txtFind != null) {
            indexOfID = txtFind.indexOf("/video/", 0, true).toInt()
            val indexOfCH = txtFind.indexOf("?", 0, true).toInt()
            id = txtFind.substring(indexOfID + 7 until indexOfCH)
        }
        return id
    }

    private fun setErrorForTI(txtFind: String): CharSequence? {
        if (txtFind.length < 5) {
            return "Link fail. Please renew link"
        } else if (!txtFind.contains("/video/")) {
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

    fun downloadVideo(url: String?, fileName: String) {
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

        val temp = 0
        viewModel.viewModelScope.launch {
            withContext(Dispatchers.Default) {
                Log.e("Log 1", "111111111111")
                try {
                    while (isActive) {
                        view?.let { queryStatus(it, downloadManager, downloadID, this) }
                        updateDownload.setTotal(percent)
                        Log.e("okk", "$percent")

                        delay(1200)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {

                }
            }
        }
        updateDownload.getTotal().observe(viewLifecycleOwner){
            process = it
            tikTokViewModel.updateTikTok(convertTikTok(tikTokModel, dateInString, fileName, process.toInt()))

        }

        var br: BroadcastReceiver? = null
        br = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                var id = p1?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

                if (id != downloadID) {
                    return
                }
                val cursor: Cursor = downloadManager.query(DownloadManager.Query().setFilterById(id))

                if (cursor.moveToFirst()) {
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        // download is successful
                        isSuccess = true
                        Toast.makeText(p0, "Completed", Toast.LENGTH_SHORT).show()
                        fileNameDB = p1?.getStringExtra(DownloadManager.COLUMN_TITLE).toString()
                        val date = getCurrentDateTime()
                        val dateInString = date.toString("dd/MM/yyyy")
                        localUriVideo = cursor.getString(cursor.getColumnIndex("local_uri"))
                        val tikTokEntity = convertTikTok(tikTokModel, dateInString, fileName, process.toInt())
                        tikTokViewModel.addTikTok(tikTokEntity)
                        Toast.makeText(requireContext(), "Insert", Toast.LENGTH_SHORT).show()
                        requireContext().unregisterReceiver(br)
//                        holderScope.cancel()
                    } else {
                        // download is cancelled
                        Toast.makeText(p0, "Error", Toast.LENGTH_SHORT).show()
                        requireContext().unregisterReceiver(br)
//                        holderScope.cancel()
                    }
                } else {
                    // download is cancelled
                    Toast.makeText(p0, "Error", Toast.LENGTH_SHORT).show()
                    requireContext().unregisterReceiver(br)
//                    holderScope.cancel()
                }

            }

        }
        requireContext().registerReceiver(br, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    fun downloadAudio(url: String?, fileName: String) {
        val download_Uri: Uri = Uri.parse(url)
        val request = DownloadManager.Request(download_Uri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setAllowedOverRoaming(false)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setTitle("Downloading")
        request.setDescription("Downloading File")
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_MUSIC,
            "$fileName.mp3"
        )
        val downloadManager = requireContext().getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)
        var br :BroadcastReceiver? = null
        br= object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                var id = p1?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadID) {
                    Toast.makeText(p0, "Completed Download Audio", Toast.LENGTH_SHORT).show()
                    requireContext().unregisterReceiver(br)
                }else{
                    Toast.makeText(p0, "Error", Toast.LENGTH_SHORT).show()
                    requireContext().unregisterReceiver(br)
                }
            }
        }
        requireContext().registerReceiver(br, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

    }

    fun downloadThumb(url: String?, fileName: String) {
        val download_Uri: Uri = Uri.parse(url)
        val request = DownloadManager.Request(download_Uri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setAllowedOverRoaming(false)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setTitle("Downloading")
        request.setDescription("Downloading File")
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_PICTURES,
            "$fileName.jpg"
        )
        val downloadManager = requireContext().getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)
        var br :BroadcastReceiver? = null
        br= object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {

                var id = p1?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadID) {
                    Toast.makeText(p0, "Completed Download Image", Toast.LENGTH_SHORT).show()
                    requireContext().unregisterReceiver(br)
                }else{
                    Toast.makeText(p0, "Error", Toast.LENGTH_SHORT).show()
                    requireContext().unregisterReceiver(br)
                }
            }
        }
        requireContext().registerReceiver(br, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    fun haveStoragePermission(context: Context, tikTokModel: TikTokModel, type: Int): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("Permission error", "You have permission")
                Log.e("link", tikTokModel.awemeDetail!!.video.play_addr.url_list[1])
                val fileName = "tiktok_${System.currentTimeMillis()}"
                if (type == 0) {
                    downloadVideo(tikTokModel.awemeDetail.video.play_addr.url_list[1], fileName)
                } else if (type == 1) {
                    downloadAudio(tikTokModel.awemeDetail.music.playUrlMusic.uri, fileName)
                } else if (type == 2) {
                    downloadThumb(tikTokModel.awemeDetail.video.origin_cover.url_list.get(0), fileName)
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

    fun pasteData(): String {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        var pasteData = ""
        if (!clipboard!!.hasPrimaryClip()) {
        } else if (!clipboard.primaryClipDescription!!.hasMimeType(MIMETYPE_TEXT_PLAIN)) {

            // since the clipboard has data but it is not plain text
        } else {

            //since the clipboard contains plain text.
            val item = clipboard.primaryClip!!.getItemAt(0)

            // Gets the clipboard as text.
            pasteData = item.text.toString()
        }
        return pasteData
    }

    fun convertTikTok(tikTokModel: TikTokModel, date: String, fileName: String, mProcess: Int): TikTokEntity {
        return TikTokEntity(
            id = 0,
            title = tikTokModel.awemeDetail!!.desc,
            urlVideo = localUriVideo,
            urlMusic = tikTokModel.awemeDetail.music.playUrlMusic.uri,
            urlThumbnail = tikTokModel.awemeDetail.video.origin_cover.url_list[0],
            author = tikTokModel.awemeDetail.author.nickname,
            duration = tikTokModel.awemeDetail.video.duration,
            fileName = fileName,
            date = date,
            process = mProcess
        )
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    private suspend fun queryStatus(v: View, mgr: DownloadManager, idD: Long, it: CoroutineScope) {
        val c: Cursor = mgr.query(DownloadManager.Query().setFilterById(idD))
        if (c == null) {
            Toast.makeText(
                activity, "No found",
                Toast.LENGTH_LONG
            ).show()
        } else {
            c.moveToFirst()
            percent = (setData(c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)), c.getLong(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))))

            Log.d(
                "abc", "COLUMN_BYTES_DOWNLOADED_SO_FAR: "
                        + c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
            )

            Log.d(
                "abc", "TOTAL SIZE: "
                        + c.getString(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
            )
            Log.d(
                "abc", "COLUMN_STATUS: "
                        + c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))
            )
            Log.d(
                "cancel", "COLUMN_REASON: "
                        + statusMessage(c, it, c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)), c.getLong(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)))
            )
            Log.d(
                "cancel", "---------------------------"
            )
            c.close()
        }
    }

    private fun statusMessage(c: Cursor, it: CoroutineScope, current: Long, total: Long): String? {
        var msg = "???"
        msg = when (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            DownloadManager.STATUS_FAILED -> {
                it.cancel()
                "Failed"
            }
            DownloadManager.STATUS_PAUSED -> {

                "Pause"
            }
            DownloadManager.STATUS_PENDING -> {

                "Pending"
            }
            DownloadManager.STATUS_RUNNING -> {
                "Running"
            }
            DownloadManager.STATUS_SUCCESSFUL -> {
                it.cancel()
                "Success"
            }
            else -> {
                it.cancel()
                "FAILED"
            }
        }
        return msg
    }

    private suspend fun setData(current: Long, total: Long): Long {
        return ((current * 100 / total));
    }
}
