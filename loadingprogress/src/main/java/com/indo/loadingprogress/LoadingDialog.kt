package com.indo.loadingprogress

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt

class LoadingDialog private constructor(
    context: Context,
    private val builder: Builder
) {
    private var dialog: Dialog = Dialog(context)
    private var progressView: CircularProgressView
    private var titleTextView: TextView

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
        progressView = view.findViewById(R.id.progressView)
        titleTextView = view.findViewById(R.id.tv_loading_title)

        setupDialog(view)
        applyCustomization()
    }

    private fun setupDialog(view: View) {
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(view)
            window?.setLayout(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setCancelable(builder.cancelable)
            setOnShowListener { progressView.restartAnimation() }
        }
    }

    private fun applyCustomization() {
        titleTextView.apply {
            text = builder.title
            visibility = if (builder.title.isNullOrEmpty()) View.GONE else View.VISIBLE
            setTextColor(builder.titleColor)
        }

        progressView.apply {
            builder.progressColor?.let { setProgressColor(it) }
            builder.strokeWidth?.let { setStrokeWidth(it) }
        }
    }


    fun show() {
        if (!dialog.isShowing) dialog.show()
    }

    fun dismiss() {
        if (dialog.isShowing) dialog.dismiss()
    }

    fun isShowing(): Boolean = dialog.isShowing

    fun updateTitle(title: String) {
        titleTextView.text = title
        titleTextView.visibility = if (title.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    class Builder(private val context: Context) {
        var title: String? = null
            private set
        var titleColor: Int = Color.BLACK
            private set
        var progressColor: Int? = null
            private set
        var strokeWidth: Float? = null
            private set
        var cancelable: Boolean = false
            private set

        fun setTitle(title: String) = apply { this.title = title }
        fun setTitleColor(@ColorInt color: Int) = apply { this.titleColor = color }
        fun setProgressColor(@ColorInt color: Int) = apply { this.progressColor = color }
        fun setStrokeWidth(width: Float) = apply { this.strokeWidth = width }
        fun setCancelable(cancelable: Boolean) = apply { this.cancelable = cancelable }

        fun build() = LoadingDialog(context, this)
    }
}