package com.sushchak.bohdan.lockscreen

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!Settings.canDrawOverlays(this)) {
            // ask for setting
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )

            startActivityForResult(intent, 22)
        }
        else{

            Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT ).show()

            val intent = Intent(this, LockScreenService::class.java)
            startService(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 22) {
            if (Settings.canDrawOverlays(this)) {

                Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT ).show()

                val intent = Intent(this, LockScreenService::class.java)
                startService(intent)
            } else {

                Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT ).show()
            }
        }
    }
}
