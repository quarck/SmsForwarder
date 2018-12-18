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

class SmsBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val pudsBundle = intent.extras

            val pdus = pudsBundle.get("pdus") as Array<Any>

            for (pdu in pdus) {

                val messages = SmsMessage.createFromPdu(pdu as ByteArray)

                val forwardingNumber = MainActivity.getForwardingNumber(context)

                if (forwardingNumber != null && !forwardingNumber.equals("", ignoreCase = true)) {

                    Log.d("SMSFW", "about to forward message to $forwardingNumber, text: ${messages.messageBody}")

                    forwardMessage(context, forwardingNumber, messages)
                }
            }
        }
        catch (ex: Exception) {

        }
    }

    private fun forwardMessage(ctx: Context, to: String, message: SmsMessage) {
        val from = message.originatingAddress

        Log.d("SMSFW", "forwardMessage: from=$from, to=$to")

        if (from != null && from == to) {
            // skip
        } else {
            doForwardMessage(ctx, from, to, message)
        }

    }

    private fun doForwardMessage(ctx: Context, from: String, to: String, message: SmsMessage) {
        //		if (to.eq)
        Log.d("SMSFW", "doForwardMessage.")

        val time = System.currentTimeMillis() / 1000

        var credits = MainActivity.getLastSmsCredits(ctx)

        val lastSmsTime = MainActivity.getLastSmsTime(ctx)
        if (lastSmsTime != 0L) {
            val timeSinceLast = time - lastSmsTime

            credits += timeSinceLast
            if (credits > 3600 * 20)
                credits = (3600 * 20).toLong() // cap
        }

        Log.d("SMSFW", "doForwardMessage: credits = $credits")

        if (credits > 0) {

            val msgBody = from +  ": " + message.messageBody

            SmsUtil.send(to, msgBody)

            credits -= 3600

            MainActivity.setLastSmsTime(ctx, time)
        }

        MainActivity.setLastSmsCredits(ctx, credits)
    }
}
