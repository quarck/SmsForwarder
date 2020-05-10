package ru.iddqd

import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import java.net.HttpURLConnection
import java.net.URL
import java.io.*
import javax.net.ssl.HttpsURLConnection


class AsyncOperation(val fn: () -> Unit)
    : AsyncTask<Void?, Void?, Void?>() {
    override fun doInBackground(vararg p0: Void?): Void? {
        fn()
        return null
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun background(noinline fn: () -> Unit) {
    AsyncOperation(fn).execute();
}


object TelegramUtils {
    fun sendMessage(apiKey: String, userId: String, text: String) {

        val apiBaseUrl = "https://api.telegram.org/bot" + apiKey
        val sendMessageBaseUrl = apiBaseUrl + "/sendMessage?chat_id=" + userId
        val fullUrl = sendMessageBaseUrl + "&parse_mode=Markdown&disable_notification=true&text=" + Uri.encode(text)

        background {
            val url = URL(fullUrl)
            val urlConnection = url.openConnection() as HttpURLConnection
            try {
                readStream(BufferedInputStream(urlConnection.getInputStream()))
            } finally {
                urlConnection.disconnect()
            }
        }

    }
    private fun readStream(bufferedInputStream: BufferedInputStream) {
        while (bufferedInputStream.available() > 0) {
            val reply = bufferedInputStream.readBytes().toString()
            Log.d("SMS_FW", "reply: $reply")
        }
    }
}