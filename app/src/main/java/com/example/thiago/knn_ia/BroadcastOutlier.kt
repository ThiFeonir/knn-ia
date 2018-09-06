package com.example.thiago.knn_ia

import android.app.NotificationManager
import android.app.PendingIntent
import android.widget.Toast
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService


class BroadcastOutlier : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "entrouBroadcast")
        val message = intent.getStringExtra("toastMessage")
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        getSystemService(context, NotificationManager::class.java)?.cancel(1)
        fireNotification(context)
    }

    private fun fireNotification(context: Context) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(context, 0, activityIntent, 0)

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val CHANNEL_ID = "channel_id"
        val mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon_credit_card)
                .setContentTitle("Cartão clonado!")
                .setContentText("Entre em contato com seu banco!")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.RED)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000, 1000))
                .setOnlyAlertOnce(true)

        ContextCompat.getSystemService(context, NotificationManager::class.java)
                ?.notify(15, mBuilder.build())

    }

    companion object {
        private val TAG = "MyBroadcastReceiver"
    }
}