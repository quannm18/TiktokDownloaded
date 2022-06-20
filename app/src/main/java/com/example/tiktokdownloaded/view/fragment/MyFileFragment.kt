package com.example.tiktokdownloaded.view.fragment

import android.os.Bundle
import android.text.TextUtils.replace
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.tiktokdownloaded.R
import com.example.tiktokdownloaded.adapter.TikTokRoomAdapter
import com.example.tiktokdownloaded.model.TikTokEntity
import com.example.tiktokdownloaded.view.fragment.myfile_element.ListMyFileFragment
import com.example.tiktokdownloaded.view.fragment.myfile_element.NoFoundFragment
import com.example.tiktokdownloaded.viewmodel.TikTokViewModel

class MyFileFragment : Fragment() {

    public lateinit var tikTokViewModel: TikTokViewModel
    private lateinit var tikTokList : List<TikTokEntity>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_file, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tikTokViewModel = ViewModelProvider(this).get(TikTokViewModel::class.java)

        tikTokList =  emptyList()
        tikTokViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            tikTokEntity -> try {
            if (tikTokEntity.size>0){
                val fragmentTransaction = childFragmentManager.beginTransaction()
                    .replace(R.id.frameMyFile, ListMyFileFragment())
                    .commit();

                return@Observer
            }else{
                val fragmentTransaction = childFragmentManager.beginTransaction()
                    .replace(R.id.frameMyFile, NoFoundFragment())
                    .commit();
                return@Observer
            }
            }catch (e:Exception){
                e.printStackTrace()
            }

        })

    }

    companion object {

    }
}