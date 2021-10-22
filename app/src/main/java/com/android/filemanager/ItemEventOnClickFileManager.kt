package com.android.filemanager

import java.io.File

interface ItemEventOnClickFileManager {
    fun onClickItemFile(file:File)
    fun onDeleteItemFile(file: File)
    fun onCopyItemFile(file: File)
    fun onMoveItemFile(file: File)
}