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

    private var apiKeyText: EditText? = null
    private var userIdText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiKeyText = findViewById(R.id.apiKey) as EditText
        userIdText = findViewById(R.id.userId) as EditText
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
        val apiKey = apiKeyText!!.text.toString()
        val userId = userIdText!!.text.toString()
        setApiKey(this, apiKey)
        setUserId(this, userId)
    }

    fun sendTest(v: View) {
        var apiKey = apiKeyText!!.text.toString()
        var userId = userIdText!!.text.toString()
        if (apiKey.isNullOrEmpty())
            apiKey = getApiKey(this) ?: ""
        if (userId.isNullOrEmpty())
            userId = getUserId(this) ?: ""
        TelegramUtils.sendMessage(apiKey, userId, "Test message!")
    }

    companion object {
        val SHARED_PREF = "ru.iddqd.PREF"
        val TELEGRAM_API_KEY = "api_key"
        val TELEGRAM_USER_ID = "user_id"

        @JvmStatic
        fun getApiKey(ctx: Context): String? {
            val prefs = ctx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
            return prefs.getString(TELEGRAM_API_KEY, "")
        }

        @JvmStatic
        fun setApiKey(ctx: Context, num: String) {
            val prefs = ctx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString(TELEGRAM_API_KEY, num)
            editor.commit()
        }

        @JvmStatic
        fun getUserId(ctx: Context): String? {
            val prefs = ctx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
            return prefs.getString(TELEGRAM_USER_ID, "")
        }

        @JvmStatic
        fun setUserId(ctx: Context, num: String) {
            val prefs = ctx.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString(TELEGRAM_USER_ID, num)
            editor.commit()
        }


    }
}
