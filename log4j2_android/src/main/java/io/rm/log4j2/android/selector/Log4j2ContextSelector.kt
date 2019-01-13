package io.rm.log4j2.android.selector

import io.rm.log4j2.android.Log4j2Android
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.config.ConfigurationSource
import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory
import org.apache.logging.log4j.core.selector.ContextSelector
import java.io.InputStream
import java.net.URI

internal class Log4j2ContextSelector : ContextSelector {
    private val defaultLoggerContext = LoggerContext("DefaultLog4j2ContextSelectorForAndroid")

    private fun getStartedContext(): LoggerContext {
        if (this.defaultLoggerContext.isStarted.not()) {
            val inputStream: InputStream = Log4j2Android.provideConfig()
            val configurationSource = ConfigurationSource(inputStream)
            val configurationFactory = XmlConfigurationFactory.getInstance()
            this.defaultLoggerContext.start(configurationFactory.getConfiguration(configurationSource))
        }

        return this.defaultLoggerContext
    }

    override fun getContext(fqcn: String?, loader: ClassLoader?, currentContext: Boolean): LoggerContext {
        return getStartedContext()
    }

    override fun getContext(
        fqcn: String?,
        loader: ClassLoader?,
        currentContext: Boolean,
        configLocation: URI?
    ): LoggerContext {
        return getStartedContext()
    }

    override fun removeContext(context: LoggerContext?) {

    }

    override fun getLoggerContexts(): List<LoggerContext> {
        return listOf(getStartedContext())
    }
}