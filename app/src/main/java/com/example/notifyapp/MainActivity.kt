package com.example.notifyapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.notifyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    var enableNotification = true;
    val requestPermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission(),) {isGranted: Boolean->
        if (isGranted) {
            if (enableNotification) {
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show()
        }

    }
    lateinit var binding: ActivityMainBinding

    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"
    val NOTIF_ID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        askForPermission()
        createNotifChannel()

        binding.btnShowNotif.setOnClickListener {

            val pendingIntent: PendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            var builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Sample Title")
                .setContentText("This is sample body notif")
                .setSmallIcon(R.drawable.ic_baseline_info_24)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            val notifManager = NotificationManagerCompat.from(this)
            // notificationId is a unique int for each notification that you must define
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@setOnClickListener
            }
            notifManager.notify(NOTIF_ID, builder.build())

        }
    }

    private fun createNotifChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
            }
            val manager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED) {
                //permission granted
            }else {
                requestPermissionsLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }

        }
    }
}