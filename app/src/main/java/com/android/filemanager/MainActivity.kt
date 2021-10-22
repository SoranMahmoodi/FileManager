package com.android.filemanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.android.filemanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnClickCreateAddFile {

    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupFragmentFileManager()
        setupSearchFileManager()
        setupListener()
    }

    private fun setupListener() {
        binding!!.btnMainActivityCreateFolder.setOnClickListener {
            setupFragmentCreateFile()
        }
        binding!!.mbtMainActivityLayoutManager.check(R.id.btn_main_activity_set_row)
        binding!!.mbtMainActivityLayoutManager.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (checkedId == R.id.btn_main_activity_set_row && isChecked) {
                val fragment =
                    supportFragmentManager.findFragmentById(R.id.fragment_main_activity_container) as FileManagerFragment
                fragment.setViewTypeItem(ViewType.ROW)
            } else if (checkedId == R.id.btn_main_activity_set_grid && isChecked) {
                val fragment =
                    supportFragmentManager.findFragmentById(R.id.fragment_main_activity_container) as FileManagerFragment
                fragment.setViewTypeItem(ViewType.GRID)
            }
        }
    }

    private fun setupFragmentCreateFile() {
        AddNewFileFragmentDialog().show(supportFragmentManager, "createFile")
    }

    private fun setupSearchFileManager() {
        binding!!.edtMainActivitySearchFile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val fragment =
                    supportFragmentManager.findFragmentById(R.id.fragment_main_activity_container) as FileManagerFragment
                s?.let {
                    fragment.searchFile(it.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun setupFragmentFileManager() {
        if (StorageHelper.isExternalStorageReadable()) {
            val externalFile = getExternalFilesDir(null)
            listFiles(externalFile?.path, false)
        }
    }


    private fun listFiles(path: String?, isBackStack: Boolean) {
        val fileManagerFragment = FileManagerFragment()
        val bundle = Bundle()
        bundle.putString("path", path)
        fileManagerFragment.arguments = bundle
        val fragmentManager = supportFragmentManager.beginTransaction()
        fragmentManager.replace(R.id.fragment_main_activity_container, fileManagerFragment)
        if (isBackStack)
            fragmentManager.addToBackStack(null)

        fragmentManager.commit()
    }

    fun listFiles(path: String?) {
        listFiles(path, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onClickButtonCreateFile(nameFolder: String) {
        val fragment: Fragment =
            supportFragmentManager.findFragmentById(R.id.fragment_main_activity_container) as FileManagerFragment
        fragment?.let {
            (it as FileManagerFragment).createNewFolder(nameFolder)
        }

    }
}