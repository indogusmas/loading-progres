package com.indo.loadingprogress

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import android.widget.LinearLayout

class LoadingDialog(context: Context) {
    private var dialog: Dialog = Dialog(context)

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(view)

        // Set dialog width and height
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        dialog.setCancelable(false)
    }

    fun show() {
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    fun dismiss() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

    fun  isShowing(): Boolean{
        return dialog.isShowing;
    }





}