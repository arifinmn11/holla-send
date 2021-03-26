package com.enigma.application.presentation.mytask

import com.enigma.application.data.model.mytask.DataItem

interface MyTaskOnClickListener {
    fun onClickUnAssign(id: DataItem)
    fun onClickDone(id: DataItem)
    fun onClickDetail(id: DataItem)
}