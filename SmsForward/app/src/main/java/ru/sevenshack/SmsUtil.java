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


package ru.sevenshack;

import android.telephony.SmsManager;
import android.util.Log;

public class SmsUtil 
{
    public static void sendLong(String dst, String msg, long sleepMillis)
    {
        int chunkLength = 160;

        int indexFrom = 0;

        while (indexFrom < msg.length()) {

            int indexTo = Math.min(indexFrom + chunkLength, msg.length());

            String chunk = msg.substring(indexFrom, indexTo);

            SmsUtil.send(dst, chunk, sleepMillis);

            indexFrom += chunkLength;
        }
    }

	public static void send(String dst, String msg, long sleepMillis) 
	{
		try 
		{
			if (dst != null)
			{
                Log.d("SmsUtil", "send: " + dst + ": " + msg);

				SmsManager smsMgr = SmsManager.getDefault();
				
				if (smsMgr != null)
					smsMgr.sendTextMessage( dst, null, msg, null, null);

				if (sleepMillis > 0)
					Thread.sleep(sleepMillis); // sleep for 3 seconds to give SMS chance to get delivered
			}
		}
		catch (Exception ex)
		{
			// catch everything, since we must simply try sending, if it fails - it is not the biggest deal
		}
	}
	public static void send(String dst, String msg) 
	{
		send(dst, msg, 0);
	}
}
