package com.sushchak.bohdan.lockscreen.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.os.SystemClock
import com.sushchak.bohdan.lockscreen.control.LayoutAlertView

class LockScreenService : Service() {

    private lateinit var mReceiver: BroadcastReceiver
    private var isShowing = false

    private lateinit var windowManager: WindowManager
    private lateinit var alertView: LayoutAlertView

    var params: WindowManager.LayoutParams? = null

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
            else WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT
        )
        params?.gravity = Gravity.CENTER

        alertView = LayoutAlertView(context = this@LockScreenService)
        alertView.title = "Title"
        alertView.content = "Alert message"
        alertView.buttonText = "Ok"
        alertView.onClickAlert = {
            closeAlertView()
        }

        mReceiver = LockScreenStateReceiver()

        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        filter.addAction(Intent.ACTION_USER_PRESENT)

        registerReceiver(mReceiver, filter)

        //val anim = AnimationUtils.loadAnimation(this@LockScreenService, R.anim.shake)
        //textView.startAnimation(anim)
    }

    override fun onTaskRemoved(rootIntent: Intent) {

        val restartService = Intent(
            applicationContext,
            this.javaClass
        )
        restartService.setPackage(packageName)
        val restartServicePI = PendingIntent.getService(
            applicationContext, 1, restartService,
            PendingIntent.FLAG_ONE_SHOT
        )

        //Restart the service once it has been killed android

        val alarmService = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 100, restartServicePI)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver)
        }

        closeAlertView()
        super.onDestroy()
    }

    private fun closeAlertView(){
        if (isShowing) {
            windowManager.removeViewImmediate(alertView)
            isShowing = false
        }
    }

    inner class LockScreenStateReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            if (intent.action == Intent.ACTION_SCREEN_OFF) {
                //if screen is turn off show the textview
                if (!isShowing) {

                    windowManager.addView(alertView, params)

                    isShowing = true
                }
            }

            /*else if (intent.action == Intent.ACTION_USER_PRESENT) {
                //Handle resuming events if user is present/screen is unlocked remove the textview immediately
                if (isShowing) {
                    windowManager.removeViewImmediate(alertView)
                    isShowing = false
                }
            }*/
        }

    }
}
