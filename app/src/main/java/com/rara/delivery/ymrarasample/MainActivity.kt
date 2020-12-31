package com.rara.delivery.ymrarasample

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

class MainActivity : AppCompatActivity() {

    private val ticketIdTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            updateUI()
        }

    }

    private lateinit var ticketIdET: EditText
    private lateinit var viewOneTicketBtn: Button
    private lateinit var viewAllTicketBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initYellowMessenger()

        setContentView(R.layout.activity_main)

        init()

        ticketIdET.addTextChangedListener(ticketIdTextWatcher)

        val activity = this

        viewOneTicketBtn.setOnClickListener {

            val ticketId = ticketIdET.text.toString()

            startActivity(
                FlutterViewActivity.getStartIntent(
                    activity,
                    email = BuildConfig.YM_DRIVER_EMAIL,
                    password = BuildConfig.YM_DRIVER_PASSWORD,
                    ticketId = ticketId
                )
            )
        }

        viewAllTicketBtn.setOnClickListener {
            startActivity(
                FlutterViewActivity.getStartIntent(
                    activity,
                    email = BuildConfig.YM_DRIVER_EMAIL,
                    password = BuildConfig.YM_DRIVER_PASSWORD,
                    ticketId = null
                )
            )
        }

        updateUI()
    }

    private fun init() {
        ticketIdET = findViewById(R.id.ticketIdET)
        viewOneTicketBtn = findViewById(R.id.viewOneTicketBtn)
        viewAllTicketBtn = findViewById(R.id.viewAllTicketBtn)
    }

    private fun updateUI() {
        viewOneTicketBtn.isEnabled = ticketIdET.text.toString().isNotEmpty()
    }


    private fun initYellowMessenger() {
        // Instantiate a FlutterEngine.
        val flutterEngine = FlutterEngine(this)

        // Start executing Dart code to pre-warm the FlutterEngine.
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        // Cache the FlutterEngine to be used by FlutterActivity.
        FlutterEngineCache
            .getInstance()
            .put("my_engine_id", flutterEngine);
    }
}