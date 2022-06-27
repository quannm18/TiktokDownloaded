package com.example.tiktokdownloaded.view.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tiktokdownloaded.R
import com.example.tiktokdownloaded.model.TikTokEntity
import com.example.tiktokdownloaded.util.DateString
import com.example.tiktokdownloaded.util.listener.MyOnLongClickListener
import com.example.tiktokdownloaded.view.update_download.UpdateDownload
import com.example.tiktokdownloaded.viewmodel.SelectedChildViewModel
import com.example.tiktokdownloaded.viewmodel.TikTokViewModel
import kotlinx.android.synthetic.main.dialog_rename.view.*
import kotlinx.android.synthetic.main.row_myfile.view.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration


class TikTokChildAdapter : RecyclerView.Adapter<TikTokChildAdapter.TikTokViewHolder>() {
    private var tikTokList: List<TikTokEntity> = emptyList()
    private var selectedList: MutableList<TikTokEntity> = mutableListOf()
    private var isShowAll: Boolean = false
    private lateinit var mDialog: AlertDialog
    private lateinit var mViewModel: SelectedChildViewModel
    private lateinit var mEntityViewModel: TikTokViewModel
    private lateinit var longClickListener: MyOnLongClickListener

    var isEnable: Boolean = false
    var isSelected: Boolean = false
    var isSelectedAll: Boolean = false
    lateinit var mActivity: Activity
    private lateinit var alertDialog: AlertDialog
    private lateinit var updateDownload: UpdateDownload
    private lateinit var lifecycleOwner: LifecycleOwner

    class TikTokViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TikTokViewHolder {
        return TikTokViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_myfile, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TikTokViewHolder, position: Int) {
        val tikTokEntity = tikTokList[position]
        holder.itemView.cbRow.visibility = View.INVISIBLE
        if (tikTokEntity.process<100){
            holder.itemView.boxDownloadingRow.visibility = View.VISIBLE
            holder.itemView.progressBarRow.visibility = View.VISIBLE
            holder.itemView.progressBarRow.setProgress(tikTokEntity.process)
            holder.itemView.tvNumOfRow.setText("${tikTokEntity.process}")
        }
        if (tikTokEntity.process==100){
            holder.itemView.boxDownloadingRow.visibility = View.GONE
            holder.itemView.progressBarRow.visibility = View.GONE
        }

        if (isShowAll) {
            holder.itemView.cbRow.visibility = View.VISIBLE
            holder.itemView.btnMoreRowMyFile.visibility = View.INVISIBLE
        } else {
            holder.itemView.cbRow.visibility = View.INVISIBLE
            holder.itemView.btnMoreRowMyFile.visibility = View.VISIBLE
        }
        if (position == 0) {
            if (DateString.dateInString != tikTokEntity.date) {
                holder.itemView.tvDateRow.text = tikTokEntity.date
            } else {
                holder.itemView.tvDateRow.text = "Today"
            }
        }
        if (position > 0) {
            if (tikTokEntity.date != tikTokList[position - 1].date) {
                holder.itemView.tvDateRow.text = tikTokEntity.date
            } else {
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
        holder.itemView.setOnLongClickListener {
            isShowAll = !isShowAll
            if (isEnable) {
                isEnable = false
//                clickItem(holder)
            }
            if (!isEnable) {
                clickItem(holder)
                isEnable = true
            }
            true
        }

        holder.itemView.setOnClickListener {
            if (isEnable) {
                clickItem(holder)
            } else {
                Toast.makeText(
                    mActivity,
                    "You clicked ${tikTokList.get(holder.adapterPosition)}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        holder.itemView.btnMoreRowMyFile.setOnClickListener {
            val mPopUp = PopupMenu(holder.itemView.context, holder.itemView.btnMoreRowMyFile)
            mPopUp.inflate(R.menu.popup_menu)
            val menu = mPopUp.menu;
            mPopUp.setForceShowIcon(true);
            mPopUp.setOnMenuItemClickListener { it ->
                menuItemClick(it, holder.itemView.context, tikTokEntity)
            }
            mPopUp.show()
        }

        updateDownload.getTotal().observe(lifecycleOwner) {
            Log.e("c","$it")
        }
    }

    override fun getItemViewType(position: Int): Int {

        if (tikTokList[position].date == DateString.dateInString) {
            return 0
        } else if (tikTokList[position].date == tikTokList[position].date) {
            return 1
        }
        return 2
    }

    override fun getItemCount(): Int {
        return tikTokList.size
    }

    private fun menuItemClick(it: MenuItem, context: Context, item: TikTokEntity): Boolean {
        when (it.itemId) {
            R.id.btnRepostPopupmenu -> {
                Log.e("this", "Repost")
            }
            R.id.btnSharePopupmenu -> {
                Log.e("this", "Share")
                context.startActivity(
                    Intent.createChooser(
                        Intent().setAction(Intent.ACTION_SEND)
                            .setType("video/*")
                            .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            .putExtra(
                                Intent.EXTRA_STREAM,
                                Uri.parse(item.urlVideo)
                            ), "share_video"
                    )
                )

            }
            R.id.btnRenamePopupmenu -> {
                val dialogUpdate = AlertDialog.Builder(context)
                val viewUpdate = LayoutInflater.from(context).inflate(R.layout.dialog_rename,null)
                dialogUpdate.setView(viewUpdate)
                viewUpdate.btnRenameDialog.setOnClickListener {
                    val newTitle = viewUpdate.edRenameDialog.text

                    if (newTitle.trim().isEmpty()){
                        Toast.makeText(context,"Please enter data", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    item.title = newTitle.toString()
                    mEntityViewModel.updateTikTok(item)
                    Toast.makeText(context,"Successfully updated", Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                }
                viewUpdate.btnCloseDialog.setOnClickListener {
                    alertDialog.dismiss()
                }
                alertDialog = dialogUpdate.create()
                alertDialog.show()

                Log.e("this", "Rename")
            }
            R.id.btn_split_audio ->
                Log.e("this", "Split")
            R.id.btnDeletePopupmenu -> {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Delete")
                builder.setMessage("Are you wanting to delete  ${item.title}")
                builder.setPositiveButton("Yes") { _, _ ->
                    mEntityViewModel.deleteTikTok(item)
                    Toast.makeText(
                        context,
                        "Successfully removed ${item.title}",
                        Toast.LENGTH_SHORT
                    ).show()

                }.setNegativeButton("No"){
                        _,_->
                    alertDialog.dismiss()
                }
                alertDialog = builder.create()
                alertDialog.show()
                Log.e("this", "Delete")
            }
            else ->
                Log.e("this", "${it.title}")
        }
        return true
    }

    fun setTikTokList(
        list: List<TikTokEntity>,
        activity: Activity,
        myOnLongClickListener: MyOnLongClickListener,
        lifecycleOwner: LifecycleOwner
    ) {
        this.lifecycleOwner = lifecycleOwner
        this.mActivity = activity
        mEntityViewModel =
            ViewModelProvider(activity as FragmentActivity)[TikTokViewModel::class.java]

        this.mViewModel =
            ViewModelProvider(activity as FragmentActivity)[SelectedChildViewModel::class.java]
        this.updateDownload = ViewModelProvider(activity as FragmentActivity)[UpdateDownload::class.java]
        this.longClickListener = myOnLongClickListener
        this.tikTokList = emptyList()
        this.tikTokList = list
        notifyDataSetChanged()
    }

    private fun clickItem(holder: TikTokViewHolder) {
        val item = tikTokList[holder.adapterPosition]
        longClickListener.itemCheckBoxLongClickListener(isShowAll)
        longClickListener.deleteThisItem(item)
        longClickListener.cancelDialog()
        longClickListener.shareItem(item)

        if (holder.itemView.cbRow.visibility == View.INVISIBLE) {
            holder.itemView.cbRow.visibility = View.VISIBLE
            holder.itemView.cbRow.setChecked(true)
            holder.itemView.setBackgroundColor(Color.LTGRAY)
            selectedList.add(item)
        } else if (holder.itemView.cbRow.visibility == View.VISIBLE) {
            if (holder.itemView.cbRow.isSelected) {
//                holder.itemView.cbRow.visibility = View.GONE
                holder.itemView.cbRow.setChecked(false)
                holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            } else {
                holder.itemView.cbRow.setChecked(true)
                holder.itemView.setBackgroundColor(Color.LTGRAY)
            }
            Log.e("Select", "${holder.itemView.cbRow.isSelected}")
            selectedList.remove(item)
        }
        mViewModel.setTikTok(item)
        notifyDataSetChanged()
    }

}