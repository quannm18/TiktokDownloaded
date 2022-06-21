package com.example.tiktokdownloaded.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tiktokdownloaded.R
import com.example.tiktokdownloaded.model.TikTokEntity
import com.example.tiktokdownloaded.model.TikTokRow
import kotlinx.android.synthetic.main.row_child_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class TikTokParentAdapter : RecyclerView.Adapter<TikTokParentAdapter.TikTokParentViewHolder>() {
    private var listTikTokParent : List<TikTokRow> = emptyList()
    private  var adapter: TikTokChildAdapter = TikTokChildAdapter()
     class TikTokParentViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TikTokParentViewHolder {
        return TikTokParentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_child_item, parent,false))
    }

    override fun onBindViewHolder(holder: TikTokParentViewHolder, position: Int) {
        val tikTokParent = listTikTokParent[position]
        val date = Calendar.getInstance().time
        val dateInString = date.toString("dd/MM/yyyy")
        if (dateInString!=tikTokParent.title){
            holder.itemView.tvSectionHeaderRow.text = tikTokParent.title
        }else{
            holder.itemView.tvSectionHeaderRow.text = "Today"
        }
        adapter = TikTokChildAdapter()
        adapter.setTikTokList(tikTokParent.listTikTokEntity)
        val layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.itemView.rcvChildRow.layoutManager = layoutManager
        holder.itemView.rcvChildRow.adapter = adapter
        Log.e("count","${position}")
    }

    override fun getItemCount(): Int {
        return listTikTokParent.size
    }

    fun setDataParent(listParent:  List<TikTokRow>){
        this.listTikTokParent = emptyList()
        this.listTikTokParent = listParent
        notifyDataSetChanged()
    }
}
fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}
