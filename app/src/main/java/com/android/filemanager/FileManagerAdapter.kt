package com.android.filemanager

import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.filemanager.databinding.ItemFileBinding
import com.android.filemanager.databinding.ItemFileGridBinding
import java.io.File

class FileManagerAdapter(
    private val files: MutableList<File>,
    private var filteredFiles: MutableList<File> = files,
     var viewType: ViewType = ViewType.ROW,
    val onClickFileManager: ItemEventOnClickFileManager
) :
    RecyclerView.Adapter<FileManagerAdapter.FileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        return FileViewHolder(
            LayoutInflater.from(parent.context).inflate(
                if (viewType == ViewType.ROW.ordinal) R.layout.item_file else R.layout.item_file_grid,
                parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bindData(filteredFiles[position])
    }

    override fun getItemCount(): Int {
        return filteredFiles.size
    }

    fun addNewFile(file: File) {
        files.add(0, file)
        notifyItemInserted(0)
    }

    fun deleteFile(file: File) {
        val index = files.indexOf(file)
        if (index > -1) {
            files.removeAt(index)
            notifyItemRemoved(index)
        }
    }


    fun searchFileAdapter(query: String) {
        if (query.isNotEmpty()) {
            val result = ArrayList<File>()
            files.forEach {
                if (it.name.toLowerCase().contains(query.toLowerCase())) {
                    result.add(it)
                }
            }
            filteredFiles = result
            notifyDataSetChanged()
        } else {
            filteredFiles = files
            notifyDataSetChanged()
        }
    }

     fun setViewTypeItem(viewType: ViewType){
        this.viewType = viewType
         notifyDataSetChanged()
    }
    override fun getItemViewType(position: Int): Int {
        return viewType.ordinal
    }

    inner class FileViewHolder(private val itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private lateinit var tvItemFileTitleFile: TextView
        private lateinit var imgItemFileFolder: ImageView
        private lateinit var btnItemFileMenu: ImageView
        fun bindData(file: File) {
            tvItemFileTitleFile = itemView.findViewById(R.id.tv_item_file_title_file)
            imgItemFileFolder = itemView.findViewById(R.id.img_item_file_folder)
            btnItemFileMenu = itemView.findViewById(R.id.btn_item_file_menu)
            tvItemFileTitleFile.text = file.name
            when {
                file.isDirectory -> {
                    imgItemFileFolder.setImageResource(R.drawable.ic_round_folder_24)
                }
                file.isFile -> {
                    imgItemFileFolder.setImageResource(R.drawable.ic_round_insert_drive_file_24)
                }
            }

            itemView.setOnClickListener {
                onClickFileManager.onClickItemFile(file)
            }
            btnItemFileMenu.setOnClickListener {
                setupPopupMenu(file)
            }
        }

        private fun setupPopupMenu(file: File) {
            val popupMenu = PopupMenu(itemView.context, itemView, Gravity.END)
            popupMenu.menuInflater.inflate(R.menu.item_file_menu, popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_file_delete -> {
                        onClickFileManager.onDeleteItemFile(file)
                    }
                    R.id.item_file_copy -> {
                        onClickFileManager.onCopyItemFile(file)
                    }
                    R.id.item_file_move -> {
                        onClickFileManager.onMoveItemFile(file)
                    }
                }
                true
            }
        }

    }
}

