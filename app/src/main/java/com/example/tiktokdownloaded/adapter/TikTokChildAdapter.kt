package com.example.tiktokdownloaded.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tiktokdownloaded.R
import com.example.tiktokdownloaded.model.TikTokEntity
import kotlinx.android.synthetic.main.row_myfile.view.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class TikTokChildAdapter : RecyclerView.Adapter<TikTokChildAdapter.TikTokViewHolder>() {
    private var tikTokList : List<TikTokEntity> = emptyList();
    class TikTokViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TikTokViewHolder {
        return TikTokViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_myfile,parent,false))
    }

    override fun onBindViewHolder(holder: TikTokViewHolder, position: Int) {
        val tikTokEntity = tikTokList[position]
        holder.itemView.setOnClickListener(View.OnClickListener {

        })

        if (holder.adapterPosition%2==0){
            holder.itemView.boxDownloadingRow.visibility = View.GONE
            holder.itemView.progressBarRow.visibility = View.GONE
        }
        Glide.with(holder.itemView.imgListRow)
            .load(tikTokEntity.urlVideo)
            .centerCrop()
            .into(holder.itemView.imgListRow)
        holder.itemView.tvTikTokTitleRow.setText(tikTokEntity.title)
        holder.itemView.tvAuthorRow.setText("@${tikTokEntity.author}")
        holder.itemView.tvFilenameRow.setText("File: ${tikTokEntity.fileName}")
        val duration = tikTokEntity.duration.toInt().toDuration(DurationUnit.MILLISECONDS)
        val timeString = duration.toComponents { m, s, _ ->
            String.format("%02d:%02d", m, s)
        }
        holder.itemView.tvDurationListRow.setText(timeString)

    }

    override fun getItemCount(): Int {
        return tikTokList.size
    }

    fun setTikTokList(list: List<TikTokEntity>){
        this.tikTokList = emptyList()
        this.tikTokList = list
        notifyDataSetChanged()
    }
}