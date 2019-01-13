package io.rm.log4j2.android.appender

import android.util.Log
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.AbstractLifeCycle
import org.apache.logging.log4j.core.Filter
import org.apache.logging.log4j.core.Layout
import org.apache.logging.log4j.core.LogEvent
import org.apache.logging.log4j.core.appender.AbstractAppender
import org.apache.logging.log4j.core.config.plugins.Plugin
import org.apache.logging.log4j.core.config.plugins.PluginAttribute
import org.apache.logging.log4j.core.config.plugins.PluginElement
import org.apache.logging.log4j.core.config.plugins.PluginFactory
import org.apache.logging.log4j.core.layout.PatternLayout
import java.io.Serializable
import java.nio.charset.Charset

@Plugin(name = "Logcat", category = "Core", elementType = "appender", printObject = true)
internal class LogcatAppender constructor(
    name: String,
    layout: Layout<out Serializable>,
    filter: Filter,
    ignoreExceptions: Boolean
) : AbstractAppender(name, filter, layout) {

    companion object {
        @PluginFactory
        @JvmStatic
        fun createAppender(
            @PluginAttribute("name") name: String?,
            @PluginAttribute("ignoreExceptions") ignoreExceptions: Boolean,
            @PluginElement("Layout") layout: Layout<*>?,
            @PluginElement("Filters") filter: Filter
        ): LogcatAppender? {
            if (name == null) {
                AbstractLifeCycle.LOGGER.error("No name provided for LogcatAppender")
                return null
            }

            return LogcatAppender(
                name,
                layout ?: PatternLayout.createDefaultLayout(),
                filter,
                ignoreExceptions
            )
        }
    }

    override fun append(event: LogEvent) {
        val byteArray = layout.toByteArray(event)
        val message = String(byteArray, Charset.forName("UTF-8"))

        if (event.level === Level.DEBUG) {
            Log.d(event.loggerName, message)
        } else if (event.level === Level.ERROR || event.level === Level.FATAL) {
            Log.e(event.loggerName, message)
        } else if (event.level === Level.INFO) {
            Log.i(event.loggerName, message)
        } else if (event.level === Level.WARN) {
            Log.w(event.loggerName, message)
        } else {
            Log.d(event.loggerName, message)
        }
    }
}