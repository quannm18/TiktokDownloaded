package com.example.tiktokdownloaded.view.fragment

import android.app.DownloadManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tiktokdownloaded.MainViewModelFactory
import com.example.tiktokdownloaded.R
import com.example.tiktokdownloaded.model.TikTokModel
import com.example.tiktokdownloaded.repository.Repository
import com.example.tiktokdownloaded.viewmodel.DownLoadVideo
import com.example.tiktokdownloaded.viewmodel.MainViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
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

            val id = getIDFromUrl(txtFind)
            viewModel.getPostTikTok(id as String)
            viewModel.myResponse.observe(viewLifecycleOwner, Observer {
                    response->
                if (response.isSuccessful){
                    Log.e("awemeDetail",response.body().toString())
                    val  tikTokModel:TikTokModel = response.body()!!
                    downLoadVideo.download(tikTokModel, context!!)

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

}