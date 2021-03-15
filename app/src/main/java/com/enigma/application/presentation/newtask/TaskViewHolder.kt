package com.enigma.application.presentation.newtask

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.enigma.application.R
import com.enigma.application.databinding.ContainerNewTaskBinding

class TaskViewHolder(val view: View, val onClickListener: TaskOnClickListener) :
    RecyclerView.ViewHolder(view) {
    private val binding = ContainerNewTaskBinding.bind(view)
    fun bind(data: String) {
        binding.apply {
            tvAddress.text = data
            var status = false

            cardView.setOnClickListener {
                if (!status) {
                    cardView.setBackgroundResource(R.drawable.selected_card)
                    selectedIcon.visibility = View.VISIBLE
                    Log.d("selected : ", tvAddress.text.toString())
                    status = !status
                    onClickListener.onSelected(data)
                } else {
                    cardView.setBackgroundResource(R.drawable.unselected_card)
                    Log.d("selected : ", tvAddress.text.toString())
                    selectedIcon.visibility = View.GONE
                    status = !status
                    onClickListener.onUnSelected(data)
                }
            }
        }
    }
}