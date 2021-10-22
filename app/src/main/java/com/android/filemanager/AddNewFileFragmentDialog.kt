package com.android.filemanager

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.android.filemanager.databinding.AddNewFileFragmentBinding

class AddNewFileFragmentDialog : DialogFragment() {

    private var binding: AddNewFileFragmentBinding? = null
    private lateinit var onClickCreateAddFile: OnClickCreateAddFile

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onClickCreateAddFile  = context as OnClickCreateAddFile
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialog = AlertDialog.Builder(context)
        binding = AddNewFileFragmentBinding.inflate(layoutInflater)
        setupListener()

        return alertDialog.setView(binding!!.root).create()

    }
    private fun setupListener() {
        binding!!.btnAddNewFolderDialogCreateFolder.setOnClickListener {
            val nameFolder = binding!!.edtAddNewFileFragmentDialog.text?.toString()
            if (binding!!.edtAddNewFileFragmentDialog.length() > 0) {
                onClickCreateAddFile.onClickButtonCreateFile(nameFolder!!)
                dismiss()
            }else{
               binding!!.telAddNewFileDialogNameFolder.error= "Field not empty"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}