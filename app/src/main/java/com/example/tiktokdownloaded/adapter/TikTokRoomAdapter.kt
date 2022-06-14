package com.example.tiktokdownloaded.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tiktokdownloaded.R
import com.example.tiktokdownloaded.model.TikTokEntity

class TikTokRoomAdapter : RecyclerView.Adapter<TikTokRoomAdapter.TikTokViewHolder>() {
    private lateinit var tikTokList : List<TikTokEntity>;
    class TikTokViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TikTokViewHolder {
        return TikTokViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_myfile,parent,false))
    }

    override fun onBindViewHolder(holder: TikTokViewHolder, position: Int) {
        holder.itemView.setOnClickListener(View.OnClickListener {

        })
    }

    override fun getItemCount(): Int {
        return tikTokList.size
    }

    fun setTikTokList(list: List<TikTokEntity>){
        this.tikTokList = list
        notifyDataSetChanged()
    }
}