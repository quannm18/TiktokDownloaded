package com.example.tiktokdownloaded.view.fragment.myfile_element

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiktokdownloaded.R
import com.example.tiktokdownloaded.adapter.TikTokRoomAdapter
import com.example.tiktokdownloaded.viewmodel.TikTokViewModel
import kotlinx.android.synthetic.main.fragment_list_my_file.view.*

class ListMyFileFragment : Fragment() {
    private lateinit var mTikTokViewModel: TikTokViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_my_file, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = TikTokRoomAdapter()
        val rcvMain = view.rcvMain
        val layoutManager = LinearLayoutManager(requireContext())

        rcvMain.layoutManager = layoutManager
        rcvMain.adapter = adapter

        mTikTokViewModel = ViewModelProvider(this).get(TikTokViewModel::class.java)
        mTikTokViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            tikTokEntity -> adapter.setTikTokList(tikTokEntity)
        })
    }

    companion object {
         // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListMyFileFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}