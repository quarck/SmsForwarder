/*
 * Copyright (c) 2014, Sergey Parshin, quarck@gmail.com
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of developer (Sergey Parshin) nor the
 *       names of other project contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ru.iddqd

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.EditText

class MainActivity : Activity() {

    private var mNumberView: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNumberView = findViewById(R.id.forwardingNumber) as EditText

        AlarmReceiver.schedule(this, 60 * 30 * 1000) // every 30 minutes approx
    }

    override fun onResume() {
        super.onResume()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    fun setNumber(v: View) {
        val forwardTo = mNumberView!!.text.toString()

        setForwardingNumber(this, forwardTo)
    }

    companion object {
        val SHARED_PREF = "ru.iddqd.PREF"
        val FORWARDING_NUMBER_KEY = "forwardTo"
        val LAST_SMS_TIME_KEY = "lastSms"
        val LAST_CREDITS_KEY = "lastCredits"
        val LAST_LOW_POWER_SMS_TIME_KEY = "lastLowPowerSms"

        @JvmStatic
        fun getForwardingNumber(ctx: Context): String? {
            val prefs = ctx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
            return prefs.getString(FORWARDING_NUMBER_KEY, "")
        }

        @JvmStatic
        fun setForwardingNumber(ctx: Context, num: String) {
            val prefs = ctx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString(FORWARDING_NUMBER_KEY, num)
            editor.commit()
        }


        @JvmStatic
        fun getLastSmsTime(ctx: Context): Long {
            val prefs = ctx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
            return prefs.getLong(LAST_SMS_TIME_KEY, 0)
        }

        @JvmStatic
        fun setLastSmsTime(ctx: Context, time: Long) {
            val prefs = ctx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putLong(LAST_SMS_TIME_KEY, time)
            editor.commit()
        }


        @JvmStatic
        fun getLastSmsCredits(ctx: Context): Long {
            val prefs = ctx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
            return prefs.getLong(LAST_CREDITS_KEY, (3600 * 20).toLong()) // 1 sms consumes 3600 credits. Each 1 seconds since last msg adds up one credit, but not exceeding total amount of 36000.
            // So not allowing more than 1 sms/hr on average, 
            // but allowing to "burst" these messages in a short period of time if necessary (but no more than 20 msgs in row)
        }

        @JvmStatic
        fun setLastSmsCredits(ctx: Context, time: Long) {
            val prefs = ctx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putLong(LAST_CREDITS_KEY, time)
            editor.commit()
        }


        @JvmStatic
        fun getLastLowBatterySms(ctx: Context): Long {
            val prefs = ctx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
            return prefs.getLong(LAST_LOW_POWER_SMS_TIME_KEY, 0)
        }

        @JvmStatic
        fun setLastLowBatterySms(ctx: Context, time: Long) {
            val prefs = ctx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putLong(LAST_LOW_POWER_SMS_TIME_KEY, time)
            editor.commit()
        }


    }
}
