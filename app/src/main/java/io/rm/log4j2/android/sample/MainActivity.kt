package io.rm.log4j2.android.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.apache.logging.log4j.LogManager

class MainActivity : AppCompatActivity() {

    private val logger = LogManager.getLogger("MainActivity")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.v("MainActivity", "This is logging of - android.util.Log logging")
        logger.info("This is info logging of log4j2")
        logger.debug("This is debug logging of log4j2")
        logger.error("This is error logging of log4j2")
    }
}