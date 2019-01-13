package io.rm.log4j2.android

import android.content.Context
import io.rm.log4j2.android.lookup.LogsDirPathLookup
import io.rm.log4j2.android.appender.LogcatAppender
import org.apache.logging.log4j.core.config.plugins.Plugin
import org.apache.logging.log4j.core.config.plugins.PluginAliases
import org.apache.logging.log4j.core.config.plugins.processor.PluginEntry
import org.apache.logging.log4j.core.config.plugins.util.PluginManager
import org.apache.logging.log4j.core.config.plugins.util.PluginRegistry
import org.apache.logging.log4j.core.config.plugins.util.PluginType
import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil
import org.apache.logging.log4j.core.util.Loader
import java.io.InputStream
import java.util.ArrayList
import java.util.HashMap
import java.util.concurrent.ConcurrentMap

class Log4j2Android {
    companion object {
        var applicationContext: Context? = null
        var configXmlResource: Int? = null

        fun init(applicationContext: Context, configXmlResource: Int) {
            this.applicationContext = applicationContext
            this.configXmlResource = configXmlResource
            System.setProperty("Log4jContextSelector", "io.rm.log4j2.android.selector.Log4j2ContextSelector")
            System.setProperty("log4j2.disableJmx", "true")
            injectPlugins("io.rm.log4j2.android", arrayOf(LogsDirPathLookup::class.java, LogcatAppender::class.java))
        }

        fun provideConfig(): InputStream {
            this.configXmlResource?.let { configXmlResource ->
                this.applicationContext?.let { applicationContext ->
                    return applicationContext.resources.openRawResource(configXmlResource)
                }
            }

            throw Error("Log4j2Android isn't initialized")
        }

        private fun injectPlugins(packageName: String, classes: Array<Class<*>>) {
            val reg = PluginRegistry.getInstance()

            try {
                val pluginsByCategoryByPackage = reg.javaClass.getDeclaredField("pluginsByCategoryByPackage")
                pluginsByCategoryByPackage.isAccessible = true
                try {
                    val map =
                        pluginsByCategoryByPackage.get(reg) as ConcurrentMap<String, Map<String, List<PluginType<*>>>>
                    map[packageName] = loadFromClasses(classes)
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }

            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            }

            PluginManager.addPackage(packageName)
        }

        private fun loadFromClasses(classes: Array<Class<*>>): Map<String, List<PluginType<*>>> {

            val resolver = ResolverUtil()
            val classLoader = Loader.getClassLoader()
            if (classLoader != null) {
                resolver.classLoader = classLoader
            }

            val newPluginsByCategory = HashMap<String, MutableList<PluginType<*>>>()
            for (clazz in classes) {
                val plugin: Plugin = clazz.getAnnotation(Plugin::class.java)!!
                val categoryLowerCase = plugin.category.toLowerCase()

                if (newPluginsByCategory[categoryLowerCase] == null) {
                    newPluginsByCategory[categoryLowerCase] = ArrayList()
                }

                val list: MutableList<PluginType<*>>? = newPluginsByCategory[categoryLowerCase]

                val mainEntry = PluginEntry()
                val mainElementName = if (plugin.elementType == Plugin.EMPTY)
                    plugin.name
                else
                    plugin.elementType
                mainEntry.key = plugin.name.toLowerCase()
                mainEntry.name = plugin.name
                mainEntry.category = plugin.category
                mainEntry.className = clazz.name
                mainEntry.isPrintable = plugin.printObject
                mainEntry.isDefer = plugin.deferChildren
                val mainType = PluginType(mainEntry, clazz, mainElementName)
                list!!.add(mainType)
                val pluginAliases = clazz.getAnnotation(PluginAliases::class.java)
                if (pluginAliases != null) {
                    for (alias in pluginAliases.value) {
                        val aliasEntry = PluginEntry()
                        val aliasElementName = if (plugin.elementType == Plugin.EMPTY)
                            alias.trim { it <= ' ' }
                        else
                            plugin.elementType
                        aliasEntry.key = alias.trim { it <= ' ' }.toLowerCase()
                        aliasEntry.name = plugin.name
                        aliasEntry.category = plugin.category
                        aliasEntry.className = clazz.name
                        aliasEntry.isPrintable = plugin.printObject
                        aliasEntry.isDefer = plugin.deferChildren
                        val aliasType = PluginType(aliasEntry, clazz, aliasElementName)
                        list.add(aliasType)
                    }
                }
            }

            return newPluginsByCategory
        }
    }
}