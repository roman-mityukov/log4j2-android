# log4j2-android
Простая библиотека для использования log4j2 на Android

Для инициализации добавьте в Application::onCreate
```
Log4j2Android.init(this.applicationContext, R.raw.log4j2_config)
```
Конфиг логгера описывается с помощью подобного res/raw/log4j2_config.xml. Подробнее о конфигурации [здесь](https://logging.apache.org/log4j/2.x/manual/configuration.html)
```
<?xml version="1.0" encoding="UTF-8"?>

<Configuration status="debug">
    <Appenders>
        <Logcat name="Logcat">
            <ThresholdFilter
                level="ALL"
                onMatch="ACCEPT"
                onMismatch="DENY" />
            <PatternLayout pattern="%m" />
        </Logcat>
        <RollingFile
            name="RollingFile"
            fileName="${logsdirpathlookup:externaldir}/com.exapmple.app.log"
            filePattern="${logsdirpathlookup:externaldir}/com.example.app-%i.log.zip">
            <ThresholdFilter
                level="ALL"
                onMatch="DENY"
                onMismatch="DENY" />
            <PatternLayout>
                <Pattern>%d %p %c{1.} [%t] %m%n</Pattern>
            </PatternLayout>
            <Policies>
                <SizeBasedTriggeringPolicy size="1 MB" />
            </Policies>
            <DefaultRolloverStrategy max="5" />
        </RollingFile>
    </Appenders>

    <Loggers>
        <Root level="ERROR">
            <AppenderRef ref="Logcat" />
            <AppenderRef ref="RollingFile" />
        </Root>
    </Loggers>
</Configuration>
```
С помощью ${logsdirpathlookup:externaldir} можно указать путь applicationContext.getExternalFilesDir() или с помощью ${logsdirpathlookup:internaldir} путь applicationContext.filesDir

Для работы библиотеки используется форк [log4j2:2.3](https://github.com/romsvm/logging-log4j2), в котором, для корректной работы на Android, удалена зависимость от java.lang.management.ManagementFactory в [org.apache.logging.log4j.core.lookup.JmxRuntimeInputArgumentsLookup](https://github.com/romsvm/logging-log4j2/blob/master/log4j-core/src/main/java/org/apache/logging/log4j/core/lookup/JmxRuntimeInputArgumentsLookup.java). На Android нельзя использовать jmx, поэтому это изменение никак не повлияет на работу логгера. По какой-то причине log4j2.disable.jmx=true перестало работать для Android с API>=26

Добавьте репозиторий с форком и библиотекой в build.gradle.kts
```
maven {
  setUrl("https://dl.bintray.com/roman-mityukov/android")
}
```

Можно использовать библиотеку через фасад slf4j (что позволит легко заменить логгер при необходимости, например на [logback](https://github.com/tony19/logback-android)). Для этого нужно добавить следующие зависимости
```
implementation("org.slf4j:slf4j-api:1.7.25") //simple logging facade for java
implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.3") //bridge from log4j2 to slf4j
implementation("io.rm.log4j2.android:log4j2-android:1.0.0") //this library
```
И в коде создать логгер с помощью slf4j api
```
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val logger: Logger = LoggerFactory.getLogger("MyLoggerName")
```

Или можно использовать api log4j2 напрямую
```
dependencies {
  implementation("org.apache.logging.log4j:log4j-api:2.3")
  implementation("io.rm.log4j2.android:log4j2-android:1.0.0")
}
```
Создать логгер
```
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

val logger: Logger = LogManager.getLogger("MyLoggerName")
```

Также может понадобиться следующее в app/build.gradle.kts
```
android {
  defaultConfig {
    ...
    javaCompileOptions.annotationProcessorOptions.includeCompileClasspath = false
  }
}
packagingOptions {
        exclude 'META-INF/LICENSE'
        exclude 'META-INF/NOTICE'
        exclude 'META-INF/DEPENDENCIES'
    }
```
# Credits:
- [https://loune.net/2016/05/using-log4j2-2-3-with-android/](https://loune.net/2016/05/using-log4j2-2-3-with-android/)
