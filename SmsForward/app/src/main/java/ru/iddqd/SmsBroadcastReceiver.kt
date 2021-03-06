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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class SmsBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val pudsBundle = intent.extras

            val pdus = pudsBundle.get("pdus") as Array<Any>

            for (pdu in pdus) {

                val messages = SmsMessage.createFromPdu(pdu as ByteArray)

                val apiKey = MainActivity.getApiKey(context)
                val userId = MainActivity.getUserId(context)

                if (!apiKey.isNullOrBlank() && !userId.isNullOrBlank()) {

                    Log.d("SMSFW", "about to forward message to $apiKey/$userId, text: ${messages.messageBody}")

                    forwardMessage(context, apiKey, userId, messages)
                }
            }
        }
        catch (ex: Exception) {

        }
    }

    private fun forwardMessage(ctx: Context, apiKey: String, userId: String, message: SmsMessage) {

        val sb = StringBuilder()
        sb.append("SMS:\n")
        sb.append("DateTime: ")
        sb.append(SimpleDateFormat.getDateTimeInstance().format(Date()))
        sb.append("\nReceived from: ")
        sb.append(message.originatingAddress ?: "null")
        sb.append("\nMessage:\n")
        sb.append(message.messageBody)
        TelegramUtils.sendMessage(apiKey, userId, sb.toString())
    }
}
