package ru.iddqd

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.AudioManager
import android.os.PowerManager
import android.os.Vibrator


@Suppress("UNCHECKED_CAST")
fun<T> Context.service(svc: String) =  getSystemService(svc) as T

val Context.alarmManager: AlarmManager
    get() = service(Context.ALARM_SERVICE)

val Context.audioManager: AudioManager
    get() = service(Context.AUDIO_SERVICE)

val Context.powerManager: PowerManager
    get() = service(Context.POWER_SERVICE)

val Context.vibratorService: Vibrator
    get() = service(Context.VIBRATOR_SERVICE)

val Context.notificationManager: NotificationManager
    get() = service(Context.NOTIFICATION_SERVICE)

