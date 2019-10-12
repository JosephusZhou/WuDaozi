package com.josephuszhou.wudaozi.demo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.josephuszhou.wudaozi.R
import com.josephuszhou.wudaozi.WuDaozi
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()

        btn.setOnClickListener {
            WuDaozi.with(this) // set context
                //.theme(R.style.CustomWuDaoziTheme) // set custom theme, change toolbar control normal color
                //.imageLoader(GlideLoader()) // set custom image loader
                .columnsCount(4) // set custom columns count
                .maxSelectableCount(1) // set custom count of selectable images
                .start() // start to select images
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            WuDaozi.REQUEST_CODE -> {
                /*if (resultCode == Activity.RESULT_OK) {
                }*/
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
