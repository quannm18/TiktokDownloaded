package com.example.tiktokdownloaded.view.fragment.myfile_element

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiktokdownloaded.R
import com.example.tiktokdownloaded.model.TikTokEntity
import com.example.tiktokdownloaded.model.TikTokRow
import com.example.tiktokdownloaded.view.adapter.TikTokChildAdapter
import com.example.tiktokdownloaded.viewmodel.TikTokViewModel
import kotlinx.android.synthetic.main.fragment_list_my_file.view.*

class ListMyFileFragment : Fragment() {
    private lateinit var mTikTokViewModel: TikTokViewModel
    private var listTitle: Set<String> = mutableSetOf()
    private var listEntity: List<TikTokEntity> = mutableListOf()
    private var sortListEntity: List<TikTokEntity> = mutableListOf()
    private var listParent: MutableList<TikTokRow> = ArrayList<TikTokRow>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_my_file, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = TikTokChildAdapter()
        val rcvMain = view.rcvMain
        val layoutManager = LinearLayoutManager(requireContext())

        rcvMain.layoutManager = layoutManager
        rcvMain.adapter = adapter

        mTikTokViewModel = ViewModelProvider(this).get(TikTokViewModel::class.java)
        mTikTokViewModel.readAllData.observe(viewLifecycleOwner, Observer { tikTokEntity ->
            val listTitle = tikTokEntity.mapTo(HashSet(tikTokEntity.size)) { it.date }

            val groupList = tikTokEntity.groupBy { it.date }
            for (i in listTitle.size - 1 downTo 0) {
                val title = listTitle.elementAt(i)
                val element = groupList[title]!!.toList()
//                val element = tikTokEntity
                val tikTokRow = TikTokRow(title, element)
                listParent.add(tikTokRow)
            }
            adapter.setTikTokList(tikTokEntity)
            adapter.notifyDataSetChanged()

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