package com.josephuszhou.wudaozi.demo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.josephuszhou.wudaozi.WuDaozi
import com.josephuszhou.wudaozi.filter.Filter
import com.josephuszhou.wudaozi.imageloader.impl.GlideLoader

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()

        val launcher = WuDaozi.getLauncher(this) { result ->
            result?.let {
                for (uri in it) {
                    Log.e("WuDaozi", "->$uri")
                }
                Toast.makeText(this, "Return images count: ${it.size}", Toast.LENGTH_SHORT)
                    .show()
            } ?: Toast.makeText(this, "Return images count: -99999", Toast.LENGTH_SHORT)
                .show()
        }

        findViewById<AppCompatButton>(R.id.btn).setOnClickListener {
            WuDaozi.with(this) // set context
                .theme(R.style.CustomWuDaoziTheme) // set custom theme, change toolbar control normal color
                .imageLoader(GlideLoader()) // set custom image loader
                .columnsCount(4) // set custom columns count
                .maxSelectableCount(9) // set custom count of selectable images
                .filter(minByteSize = 1024 * 10, selectedTypes = arrayOf(Filter.Type.JPG)) // set size filter, min is 10KB, only support jpg
                .start(launcher)  // start to select images
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "fail and bye bye ~", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        }
    }
}
