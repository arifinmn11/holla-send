package com.enigma.application.presentation.newtask

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.enigma.application.R
import com.enigma.application.data.model.newtask.DataItem
import com.enigma.application.databinding.ContainerNewTaskBinding

class TaskViewHolder(
    view: View,
    val onClickListener: TaskOnClickListener
) : RecyclerView.ViewHolder(view) {

    private val binding = ContainerNewTaskBinding.bind(view)
    fun bind(data: DataItem) {

        binding.apply {

            data?.apply {
                tvAddress.text = data.id
                tvReceiver.text = this.destination?.name ?: ""
                idCard.text = this.id
            }

            sendButton.setOnClickListener {

            }

//            cardView.setOnClickListener {
//                if (selectedIcon.visibility == View.GONE) {
//                    cardView.setBackgroundResource(R.drawable.selected_card)
//                    selectedIcon.visibility = View.VISIBLE
//                    onClickListener.onSelected(data.id.toString())
//                } else {
//                    cardView.setBackgroundResource(R.drawable.unselected_card)
//                    selectedIcon.visibility = View.GONE
//                    onClickListener.onUnSelected(data.id.toString())
//                }
//            }
        }
    }
}