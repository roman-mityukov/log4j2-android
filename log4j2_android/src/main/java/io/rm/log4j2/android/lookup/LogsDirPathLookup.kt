package io.rm.log4j2.android.lookup

import io.rm.log4j2.android.Log4j2Android
import org.apache.logging.log4j.core.LogEvent
import org.apache.logging.log4j.core.config.plugins.Plugin
import org.apache.logging.log4j.core.lookup.StrLookup

@Plugin(name = "logsdirpathlookup", category = "Lookup")
internal class LogsDirPathLookup : StrLookup {

    companion object {
        const val KEY_NAME_INTERNAL_DIR: String = "internaldir"
        const val KEY_NAME_EXTERNAL_DIR: String = "externaldir"
        const val KEY_NAME_EXTERNAL_OR_INTERNAL_DIR: String = "externalorinternal"
    }

    override fun lookup(key: String): String? {
        return when (key) {
            KEY_NAME_INTERNAL_DIR -> {
                Log4j2Android.applicationContext?.filesDir?.absolutePath
            }
            KEY_NAME_EXTERNAL_DIR -> {
                Log4j2Android.applicationContext?.getExternalFilesDir(null)?.absolutePath
            }
            KEY_NAME_EXTERNAL_OR_INTERNAL_DIR -> {
                Log4j2Android.applicationContext?.getExternalFilesDir(null)?.absolutePath
                    ?: Log4j2Android.applicationContext?.filesDir?.absolutePath
            }
            else -> null
        }
    }

    override fun lookup(event: LogEvent, key: String): String? {
        return lookup(key)
    }
}