package ru.iddqd

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootCompleteBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null)
            AlarmReceiver.schedule(context, 60 * 30 * 1000) // every 30 minutes approx
    }
}