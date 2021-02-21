package ru.HappyWorldGames.SmartDiary

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.startForegroundService
import java.util.*


class NotificationService : Service() {
    private val CHANNEL_ID = "NotificationService"

    companion object {
        var running = false

        fun startService(context: Context) {
            val startIntent = Intent(context, NotificationService::class.java)
            startForegroundService(context, startIntent)
        }
        fun stopService(context: Context) {
            val stopIntent = Intent(context, NotificationService::class.java)
            context.stopService(stopIntent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        running = true

        createNotificationChannel()

        startForeground(1, getNotification())

        /*val manager = getSystemService(ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.MINUTE, 5)
        val time = calendar.timeInMillis

        val intent = Intent(this, NotificationService.javaClass)
        manager.set(AlarmManager.RTC, time, PendingIntent.getService(this, 0, intent, 0))*/

        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                mNotificationManager.notify(1, getNotification())
            }
        }, 60000, 60000)

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        running = false
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun getNotification() : Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val lessonNow = DataInfo.getLessonNow()

        return NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("${lessonNow?.nameLesson}")
                .setContentText("${lessonNow?.roomLesson} (${lessonNow?.timeLesson})")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "Lesson",
                    NotificationManager.IMPORTANCE_NONE)
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }
}