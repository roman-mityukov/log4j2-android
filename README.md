# log4j2-android
Используется форк [log4j2:2.3](https://github.com/romsvm/logging-log4j2) в котором, для корректной работы на Android, удалена зависимость от java.lang.management.ManagementFactory в [org.apache.logging.log4j.core.lookup.JmxRuntimeInputArgumentsLookup](https://github.com/romsvm/logging-log4j2/blob/master/log4j-core/src/main/java/org/apache/logging/log4j/core/lookup/JmxRuntimeInputArgumentsLookup.java). На Android нельзя использовать jmx, поэтому это изменение никак не повлияет на работу логгера. По какой-то причине log4j2.disable.jmx=true перестало работать для Android с API>=26

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

val logger: Logger = LogManager.getLogger("MainActivity")
```

# Credits:
- [https://loune.net/2016/05/using-log4j2-2-3-with-android/](https://loune.net/2016/05/using-log4j2-2-3-with-android/)
