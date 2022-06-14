package com.example.tiktokdownloaded.view.fragment

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tiktokdownloaded.BuildConfig
import com.example.tiktokdownloaded.MainViewModelFactory
import com.example.tiktokdownloaded.R
import com.example.tiktokdownloaded.model.TikTokModel
import com.example.tiktokdownloaded.repository.Repository
import com.example.tiktokdownloaded.viewmodel.DownLoadVideo
import com.example.tiktokdownloaded.viewmodel.MainViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.File
import java.util.logging.Logger

class HomeFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var downloadManager : DownloadManager
    private var permissionApp = 0
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        permissionApp = if (it){
            1
        }else{
            0
        }
    }
    var mydownloadid : Long = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarHome.setBackgroundColor(R.color.white)
        downloadManager = context!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(MainViewModel::class.java)
        val downLoadVideo = DownLoadVideo()
        tilFind.editText!!.setOnFocusChangeListener { _, focus ->
            run {
                if (focus) {
                    btnDownload.alpha = 1F;

                }else{
                    btnDownload.alpha = 0.5F;
                }
            }
        }
        btnDownload.setOnClickListener(View.OnClickListener {

            val txtFind = tilFind.editText!!.text.toString();
            tilFind.error = setErrorForTI(txtFind)

            requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (permissionApp ==1){
                download(txtFind, "Noob Developer")
            }else{
                Toast.makeText(context, "Denied", Toast.LENGTH_SHORT).show()
            }
            val id = getIDFromUrl(txtFind)
            viewModel.getPostTikTok(id as String)
            viewModel.myResponse.observe(viewLifecycleOwner, Observer {
                    response->
                if (response.isSuccessful){
                    Log.e("awemeDetail",response.body().toString())
                    val  tikTokModel:TikTokModel = response.body()!!


                }else{
                    tilFind.editText!!.setText(response.code().toString())
                }
            })
            Log.e("id","$id")
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

    private fun download(url: String, fileName: String){
        try {
            var downloadManager = context!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val  linkVideo = Uri.parse(url)

            val  request = DownloadManager.Request(linkVideo)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                .setMimeType("mp4")
                .setAllowedOverRoaming(false)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(fileName)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator+fileName+".mp4")
                downloadManager.enqueue(request)
            Toast.makeText(context, "Downloading", Toast.LENGTH_SHORT).show()
        }catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
    }


}