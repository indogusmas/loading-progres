package com.indo.exampleloadingprogressbar

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.indo.loadingprogress.LoadingDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadingDialog = LoadingDialog.Builder(this)
            .setTitle("Loading Example")
            .build()
        findViewById<Button>(R.id.btn_loading).setOnClickListener {
            showLoadingWithDelay()

        }
    }

    private fun showLoadingWithDelay() {
        // Show loading dialog
        loadingDialog.show()

        // Create a coroutine in the lifecycle scope
        lifecycleScope.launch {
            try {
                delay(4000) // Wait for 4 seconds
            } finally {
                // Dismiss the dialog in finally block to ensure it's always dismissed
                loadingDialog.dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss()
        }
    }
}