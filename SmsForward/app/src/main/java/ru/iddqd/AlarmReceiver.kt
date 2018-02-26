package ru.iddqd

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null)
            return;

        val forwardingNumber = MainActivity.getForwardingNumber(context)

        if (forwardingNumber != null && !forwardingNumber.equals("", ignoreCase = true)) {

            val serviceIntent = Intent(context, BatteryMonitorService::class.java)
            context.startService(serviceIntent)
        }
    }

    companion object {

        fun schedule(context: Context, repeatMillis: Int) {

            val intent = Intent(context, AlarmReceiver::class.java)
            val pendIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + repeatMillis, repeatMillis.toLong(), pendIntent)
        }

    }
}