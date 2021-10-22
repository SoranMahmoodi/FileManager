package com.android.filemanager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.filemanager.databinding.FileManagerFragmentBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.collections.ArrayList

class FileManagerFragment : Fragment(), ItemEventOnClickFileManager {

    companion object {
        private const val TAG = "FileManagerFragment"
    }

    private lateinit var fileAdapter: FileManagerAdapter
    private var binding: FileManagerFragmentBinding? = null
    private lateinit var gridLayoutManager: GridLayoutManager
    private var pathFile: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pathFile = arguments?.getString("path")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FileManagerFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (StorageHelper.isExternalStorageReadable()) {
            val file = File(pathFile)
            val externalFile = file.listFiles().toMutableList()
            fileAdapter = FileManagerAdapter(externalFile, onClickFileManager = this)
            if (file.name.equals("files"))
                binding?.tvFileManagerFragmentPath?.text = "External Storeg"
            else
                binding?.tvFileManagerFragmentPath?.text = file.name

        }


        setupViews()
    }

    private fun setupViews() {
        gridLayoutManager = GridLayoutManager(requireContext(),1)
        binding!!.rvFileManagerFragment.apply {
            layoutManager = gridLayoutManager
            adapter = fileAdapter
        }
        binding?.btnFileManagerFragmentBack?.setOnClickListener {
            activity?.onBackPressed()
        }

    }

    fun createNewFolder(nameFolder: String) {
        if (StorageHelper.isExternalStorageWritable()) {
            val newFolder = File(pathFile + File.separator + nameFolder)
            if (!newFolder.exists()) {
                if (newFolder.mkdir()) {
                    fileAdapter.addNewFile(newFolder)
                    binding!!.rvFileManagerFragment.smoothScrollToPosition(0)
                }
            }
        }

    }

    override fun onClickItemFile(file: File) {
        (activity as MainActivity).listFiles(file?.path)
    }

    override fun onDeleteItemFile(file: File) {
        if (StorageHelper.isExternalStorageWritable()) {
            if (file.delete()) {
                fileAdapter.deleteFile(file)
            }
        }

    }

    override fun onCopyItemFile(file: File) {
        if (StorageHelper.isExternalStorageWritable()) {
            try {
                copy(file, getPathDestination(file))
                Toast.makeText(context, "copy is filed!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.i(TAG, "onCopyItemFile: $e")
            }
        }

    }

    override fun onMoveItemFile(file: File) {
        if (StorageHelper.isExternalStorageWritable()) {
            try {
                copy(file, getPathDestination(file))
                onDeleteItemFile(file)
                Toast.makeText(context, "move is filed!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.i(TAG, "onCopyItemFile: $e")
            }
        }

    }

    private fun copy(source: File, destination: File) {
        val fileInputFile = FileInputStream(source)
        val fileOutputFile = FileOutputStream(destination)
        val buffer = ByteArray(1024)
        var length: Int
        while ((fileInputFile.read(buffer).also { length = it }) > 0) {
            fileOutputFile.write(buffer, 0, length)
        }
        fileInputFile.close()
        fileOutputFile.close()
    }

    private fun getPathDestination(file: File): File = File(
        context?.getExternalFilesDir(null)?.path + File.separator + "Music" + File.separator,
        file.name
    )

     fun searchFile(query:String){
        fileAdapter?.let {
            fileAdapter.searchFileAdapter(query)
        }
    }
    fun setViewTypeItem(viewType: ViewType){
        fileAdapter.let {
            fileAdapter.setViewTypeItem(viewType)
            if (viewType == ViewType.ROW){
                gridLayoutManager.spanCount=1
            }else if (viewType == ViewType.GRID){
                gridLayoutManager.spanCount=2

            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
