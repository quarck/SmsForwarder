package ru.iddqd

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import java.util.*

/**
 * Created by spars on 28/08/2016.
 */
class Contacts(val context: Context) {

    val PREFIX_CODE = "_cd_"
    val PREFIX_CONTACT = "_cc_"

    val PREFIX_BANNED = "_bn_"

    val random by lazy { Random(System.currentTimeMillis()) }

    val chars = "0123456789ABCDEFGHJKLMNPQRSTUVWYZ"

    val randomString: String
        get () {

            val sb = StringBuilder(10)

            for (idx in 1..3)
                sb.append(chars[random.nextInt(chars.length)])

            return sb.toString()
        }

    fun isBanned(phone: String): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs?.getBoolean(PREFIX_BANNED + phone, false) ?: false
    }

    fun setBanned(phone: String, value: Boolean) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context);
        val editor = prefs?.edit() ?: null
        if (editor != null) {
            editor.putBoolean(PREFIX_BANNED + phone, value)
            editor.commit()
        }
    }

    fun storeContact(contact: String): String {

        Log.d("SMSFW", "storeContact for $contact")

        val existingCode = getString(PREFIX_CONTACT + contact)

        if (existingCode != null) {
            Log.d("SMSFW", "storeContact returning existing $existingCode")
            return existingCode;
        }

        while (true) {
            val newCode = randomString

            if (lookupContactByCode(newCode) != null)
                continue

            Log.d("SMSFW", "storeContact: new code $newCode")

            putStrings(
                    PREFIX_CONTACT + contact, newCode,
                    PREFIX_CODE + newCode, contact
            )

            return newCode
        }
    }

    fun lookupContactByCode(code: String): String? {
        val ret = getString(PREFIX_CODE + code)
        Log.d("SMSFW", "lookupContactByCode: key=${PREFIX_CODE + code}, ret=$ret")
        return ret
    }

    fun getString(key: String): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs?.getString(key, null)
    }

    fun putString(key: String, value: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context);
        val editor = prefs?.edit() ?: null
        if (editor != null) {
            editor.putString(key, value)
            editor.commit()
        }
    }

    fun putStrings(
            key1: String, value1: String,
            key2: String, value2: String
            ) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context);
        val editor = prefs?.edit() ?: null
        if (editor != null) {
            editor.putString(key1, value1)
            editor.putString(key2, value2)
            editor.commit()
        }
    }
}