package com.example.thiago.knn_ia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.firestore.DocumentChange
import android.content.Context.NOTIFICATION_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.content.BroadcastReceiver
import android.content.SharedPreferences
import android.graphics.Color
import com.google.gson.Gson
import android.media.RingtoneManager






class MainActivity : AppCompatActivity() {
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var myDataset : ArrayList<String?>
    lateinit var sharedPref : SharedPreferences
    //internal var editor = sharedPref.edit()

    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = this?.getSharedPreferences(
                "preferenceOrder", Context.MODE_PRIVATE)

        val kMeans = KMeans()

        db.collection("orders")
                .whereEqualTo("card_number", "123456789")
                .addSnapshotListener(EventListener<QuerySnapshot> { value, e ->
                    if (e != null) {
                        Log.w("GetData", "Listen failed.", e)
                        return@EventListener
                    }

                    val orders = ArrayList<String?>()
                    for (doc in value!!) {
                        if (doc.get("price") != null) {
                            orders.add(doc.getString("price"))
                        }
                    }

                    for (dc in value?.documentChanges!!) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> {
                                //Log.d("ADDED", "New city: " + dc.document.data)
                                val latlng = dc.document.data["localização"].toString().removePrefix("lat/lng: (").split(",", ")")
                                val latitude = latlng[0]
                                val longitude = latlng[1]
                                val order = Order("123456789", Coordinators(latitude.toDouble(), longitude.toDouble()))
                                val added = kMeans.add(order, false)

                                if (!added) {
                                    Log.d("EntrouElse", "entrouif")
                                    with (sharedPref.edit()) {
                                        val gson = Gson()
                                        val json = gson.toJson(order)
                                        putString("newOrder", json)
                                        apply()
                                    }
                                    fireNotification()
                                    Toast.makeText(this, "Compra suspeita! Confirma ter feito a compra?", Toast.LENGTH_LONG).show()
                                } else {
                                    Log.d("EntrouElse", "entrouelse")
                                    //orders.add(dc.document.data["price"].toString())
                                }

                            }
                            else -> {
                                Log.d("NOTADDED", "Nada adicionado")
                            }
                        }
                    }

                    notifyRecycler(orders)
                    Log.d("GetData", "Current orders for Thiago: $orders")
                })

    }

    private fun notifyRecycler(orders: ArrayList<String?>) {
        myDataset = orders
        viewManager = LinearLayoutManager(this)
        viewAdapter = RecyclerAdapter(myDataset)

        recycler_orders.apply {
            setHasFixedSize(true)

            layoutManager = viewManager

            adapter = viewAdapter

        }

        viewAdapter.notifyDataSetChanged()
    }

    private fun fireNotification() {

        val activityIntent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0)

        val broadcastIntent = Intent(this, MyBroadcastReceiver::class.java)
        broadcastIntent.putExtra("toastMessage", "OK! Compra realizada com sucesso!")
        broadcastIntent.action = "notification"
        val actionPendingIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val broadcastIntent1 = Intent(this, BroadcastOutlier::class.java)
        broadcastIntent1.putExtra("toastMessage", "Cartão clonado! Entre em contato com o banco!")
        broadcastIntent1.action = "notification"
        val actionPendingIntent1 = PendingIntent.getBroadcast(this, 0, broadcastIntent1, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val CHANNEL_ID = "channel_id"
        val mBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_cart)
                .setContentTitle("Compra suspeita detectada!")
                .setContentText("Confirma ter realizado essa compra?")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setColor(Color.GREEN)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000, 1000))
                .addAction(R.mipmap.ic_launcher, "sim",
                        actionPendingIntent)
                .addAction(R.mipmap.ic_launcher, "não",
                        actionPendingIntent1)

        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
// mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build())

    }
}
