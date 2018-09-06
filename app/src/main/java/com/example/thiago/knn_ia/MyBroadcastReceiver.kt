package com.example.thiago.knn_ia

import android.app.NotificationManager
import android.app.PendingIntent
import android.widget.Toast
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.gson.Gson




class MyBroadcastReceiver : BroadcastReceiver() {
    lateinit var sharedPref : SharedPreferences
    override fun onReceive(context: Context, intent: Intent) {

        val kMeans = KMeans()

        Log.d(TAG, "entrouBroadcast")

        val message = intent.getStringExtra("toastMessage")

        sharedPref = context.getSharedPreferences(
                "preferenceOrder", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref.getString("newOrder", "")
        val obj = gson.fromJson<Order>(json, Order::class.java)

        val added = kMeans.add(obj, true)


        if (added) {
            Toast.makeText(context,
                    "Adicionado!", Toast.LENGTH_SHORT).show()
            ContextCompat.getSystemService(context, NotificationManager::class.java)?.cancel(1)
            fireNotification(context)
        }
    }

    private fun fireNotification(context: Context) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(context, 0, activityIntent, 0)

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val CHANNEL_ID = "channel_id"
        val mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon_cart)
                .setContentTitle("OK!")
                .setContentText("Compra realizada com sucesso!")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.GREEN)
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