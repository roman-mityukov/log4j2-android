# log4j2-android
Используется форк [log4j2:2.3](https://github.com/romsvm/logging-log4j2) в котором, для корректной работы на Android, удалена зависимость от java.lang.management.ManagementFactory в [org.apache.logging.log4j.core.lookup.JmxRuntimeInputArgumentsLookup](https://github.com/romsvm/logging-log4j2/blob/master/log4j-core/src/main/java/org/apache/logging/log4j/core/lookup/JmxRuntimeInputArgumentsLookup.java). На Android нельзя использовать jmx, поэтому это изменение никак не повлияет на работу логгера.

Добавьте репозиторий с форком и библиотекой в build.gradle.kts
```
maven {
  setUrl("https://dl.bintray.com/roman-mityukov/android")
}
```
И следующие зависимости в app/build.gradle.kts
```
dependencies {
  implementation("org.apache.logging.log4j:log4j-api:2.3") //maven central
  implementation("org.apache.logging.log4j:log4j-core:2.3") //fork from https://dl.bintray.com/roman-mityukov/android
  implementation("io.rm.log4j2.android:log4j2-android:1.0.0") //https://dl.bintray.com/roman-mityukov/android
  annotationProcessor("org.apache.logging.log4j:log4j-core:2.3") //fork from https://dl.bintray.com/roman-mityukov/android
}
```