package com.example.tiktokdownloaded.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tiktokdownloaded.R
import com.example.tiktokdownloaded.model.TikTokEntity
import com.example.tiktokdownloaded.util.DateString
import kotlinx.android.synthetic.main.row_myfile.view.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class TikTokChildAdapter : RecyclerView.Adapter<TikTokChildAdapter.TikTokViewHolder>() {
    private var tikTokList : List<TikTokEntity> = emptyList()

    class TikTokViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TikTokViewHolder {
        return TikTokViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_myfile,parent,false))
    }

    override fun onBindViewHolder(holder: TikTokViewHolder, position: Int) {
        val tikTokEntity = tikTokList[position]

            holder.itemView.boxDownloadingRow.visibility = View.GONE
            holder.itemView.progressBarRow.visibility = View.GONE

        if (position==0){
            if (DateString.dateInString != tikTokEntity.date){
                holder.itemView.tvDateRow.text = tikTokEntity.date
            }else{
                holder.itemView.tvDateRow.text = "Today"
            }
        }else{
            if (tikTokEntity.date != tikTokList[position-1].date){
                holder.itemView.tvDateRow.text = tikTokEntity.date
            }else{
                holder.itemView.tvDateRow.visibility = View.GONE
            }
        }

        Glide.with(holder.itemView.imgListRow)
            .load(tikTokEntity.urlVideo)
            .centerCrop()
            .into(holder.itemView.imgListRow)
        holder.itemView.tvTikTokTitleRow.text = tikTokEntity.title
        holder.itemView.tvAuthorRow.text = "@${tikTokEntity.author}"
        holder.itemView.tvFilenameRow.text = "File: ${tikTokEntity.fileName}"
        val duration = tikTokEntity.duration.toInt().toDuration(DurationUnit.MILLISECONDS)
        val timeString = duration.toComponents { m, s, _ ->
            String.format("%02d:%02d", m, s)
        }
        holder.itemView.tvDurationListRow.text = timeString
        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context, "${tikTokEntity.id} ${tikTokEntity.urlVideo}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (tikTokList[position].date == DateString.dateInString){
            return 0
        }else if (tikTokList[position].date ==tikTokList[position].date){
            return 1
        }
        return 2
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