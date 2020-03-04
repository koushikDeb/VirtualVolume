package com.koushik.virtualvolume

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat
import android.media.AudioManager
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri


public class Volumeservice : Service()
{
    lateinit var  notificationManager:NotificationManager
    var CHANNEL_ID:String ="com.koushik.volumecontrol"

    lateinit var customNotification: Notification
    companion object {
        fun startService(context: Context, message: String) {
            val startIntent = Intent(context, Volumeservice::class.java)
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)
        }
        fun stopService(context: Context) {
            val stopIntent = Intent(context, Volumeservice::class.java)
            context.stopService(stopIntent)
        }
    }




    override fun onBind(p0: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }











    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


      handelactions(intent?.action)
        
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             createchannel()
        }

        val notificationLayout = RemoteViews(packageName, R.layout.notification_small)

        var leftIntent =  Intent(this, Volumeservice::class.java)
        leftIntent.setAction("left")
        notificationLayout.setOnClickPendingIntent(R.id.volumedecrese, PendingIntent.getService(this, 0, leftIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        var rightintent =  Intent(this, Volumeservice::class.java)
        rightintent.setAction("right");
        notificationLayout.setOnClickPendingIntent(R.id.volumeincrease, PendingIntent.getService(this, 0, rightintent, PendingIntent.FLAG_UPDATE_CURRENT));

         notificationLayout.setProgressBar(R.id.mediaseekbar,15,getcurrentvol(),false)


        //val notificationLayoutExpanded = RemoteViews(packageName, R.layout.notification_large)

// Apply the layouts to the notification
        customNotification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_vo_up)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)

            .build()

           //  notificationManager.notify(1001,customNotification) .setCustomBigContentView(notificationLayoutExpanded)


           startForeground(1001,customNotification)

        return START_STICKY
    }
   lateinit var am:AudioManager;
    private fun getcurrentvol(): Int {
         am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val volume_level = am.getStreamVolume(AudioManager.STREAM_MUSIC)
return volume_level
    }

    private fun handelactions(action: String?) {
        if (action=="left")
        {
            am.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                getcurrentvol()-1,
                0);
            //notificationManager.notify(1001,customNotification)
        }
        else if(action=="right")
        {
            am.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                getcurrentvol()+1,
                0);
            //notificationManager.notify(1001,customNotification)
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun createchannel()
    {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_LOW
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText

        mChannel.setSound(null,null)

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this

            notificationManager.createNotificationChannel(mChannel)
        }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf();
    }


}