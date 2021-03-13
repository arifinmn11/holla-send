package com.enigma.application.utils.component

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.enigma.application.R

class ConfirmationDialog {
    companion object {
        fun build(context: Context): AlertDialog {
            val inflate = LayoutInflater.from(context).inflate(R.layout.layout_dialog, null, true)
            val dialog = AlertDialog.Builder(context).setView(inflate).setCancelable(true)
                // Judul
                .setTitle("Alert Dialog Title")
                // Pesan yang di tamopilkan
                .setMessage("Pesan Alert Dialog")
                .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->
                    Toast.makeText(context, "Anda memilih tombol ya", Toast.LENGTH_LONG).show()
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->
                    Toast.makeText(context, "Anda memilih tombol tidak", Toast.LENGTH_LONG).show()
                }).create()
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            return dialog
        }
    }
}