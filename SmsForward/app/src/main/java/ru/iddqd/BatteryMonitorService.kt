package ru.iddqd

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.preference.PreferenceManager

/**
 * Created by spars on 28/08/2016.
 */
class BatteryMonitorService: IntentService("BatteryMonitorService") {

    override fun onHandleIntent(intent: Intent?) {

        val forwardingNumber = MainActivity.getForwardingNumber(this)

        if (forwardingNumber == null || forwardingNumber.equals("", ignoreCase = true))
            return

        val level = getPhoneChargeLevel(this)

        if (level < 35) {

            val lastSms = getLastBatteryLevelSms(this)

            val now = System.currentTimeMillis()

            if (now - lastSms > 24L * 3600L * 1000L) { // no more than 1 sms per day
                setLastBatteryLevelSms(this, now)
                SmsUtil.send( forwardingNumber,  "BATTERY LEVEL LOW: ${level}%")
            }
        }
    }

    private fun getPhoneChargeLevel(context: Context): Int {
        synchronized(BatteryMonitorService::class.java) {
            val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            val batteryStatus = context.registerReceiver(null, ifilter)

            if (batteryStatus != null) {
                val level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

                return Math.round(level / scale.toFloat() * 100.0f)
            }

            return -1
        }
    }

    fun getLastBatteryLevelSms(context: Context): Long {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs?.getLong(LAST_BATTERY_MOAN, 0) ?: 0L
    }

    fun setLastBatteryLevelSms(context: Context, tm: Long) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context);
        val editor = prefs?.edit() ?: null
        if (editor != null) {
            editor.putLong(LAST_BATTERY_MOAN, tm)
            editor.commit()
        }
    }

    companion object {
        const val LAST_BATTERY_MOAN = "_x3"
    }
}