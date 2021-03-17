package com.enigma.application.presentation.mytask

interface MyTaskOnClickListener {
    fun onClickUnAssign(id: String)
    fun onClickDone(id: String)
    fun onClickDetail(id: String)

}