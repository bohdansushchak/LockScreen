package com.sushchak.bohdan.lockscreen

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.WindowManager
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.view.SurfaceView
import android.view.animation.AnimationUtils
import android.widget.*

class LockScreenService : Service() {

    private lateinit var mReceiver: BroadcastReceiver
    private var isShowing = false

    private lateinit var windowManager: WindowManager
    private lateinit var textView: TextView
    private lateinit var surface: SurfaceView

    private lateinit var button: Button

    var params: WindowManager.LayoutParams? = null

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        textView = TextView(this)
        textView.text = "Hello there"
        textView.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        textView.textSize=52f

        surface = SurfaceView(this)

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,

            if( Build.VERSION.SDK_INT < Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
                    else WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT
        )

        params?.gravity = Gravity.CENTER

        mReceiver = LockScreenStateReceiver()
        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_USER_PRESENT)

        registerReceiver(mReceiver, filter)


        button = Button(this)
        button.text = "Click"
        button.setOnClickListener {

            val anim = AnimationUtils.loadAnimation(this@LockScreenService, R.anim.shake)
            textView.startAnimation(anim)

        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    inner class LockScreenStateReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            if (intent.action == Intent.ACTION_SCREEN_OFF) {
                //if screen is turn off show the textview
                if (!isShowing) {

                    var linearLayout = LinearLayout(this@LockScreenService)

                    linearLayout.orientation = LinearLayout.VERTICAL

                    linearLayout.addView(textView)
                    linearLayout.addView(button)

                    windowManager.addView(linearLayout, params)

                    //windowManager.addView(surface,params)

                    isShowing = true
                }

            } /*else if (intent.action == Intent.ACTION_USER_PRESENT) {
                //Handle resuming events if user is present/screen is unlocked remove the textview immediately
                if (isShowing) {
                    windowManager.removeViewImmediate(textView)
                    isShowing = false
                }
            }*/
        }

    }

    override fun onDestroy() {
        //unregister receiver when the service is destroy
        if (mReceiver != null) {
            unregisterReceiver(mReceiver)
        }

        //remove view if it is showing and the service is destroy
        if (isShowing) {
            windowManager.removeViewImmediate(textView)
            isShowing = false
        }
        super.onDestroy()
    }


}
