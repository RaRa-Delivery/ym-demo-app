package com.rara.delivery.ymrarasample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import org.json.JSONException
import org.json.JSONObject


class FlutterViewActivity : FlutterActivity() {

    companion object {
        private const val CHANNEL = "com.yellowmessenger.support_agent/data"
        private const val BOT_ID = BuildConfig.YELLOW_MESSENGER_BOT_ID

        private val TAG = FlutterViewActivity::class.java.simpleName
        private val EXTRA_EMAIL = "$TAG.EXTRA_EMAIL"
        private val EXTRA_PASSWORD = "$TAG.EXTRA_PASSWORD"

        var ticketId: String? = null

        fun getStartIntent(
            context: Context,
            email: String,
            password: String,
            ticketId: String?
        ): Intent {
            FlutterViewActivity.ticketId = ticketId
            return Intent(context, FlutterViewActivity::class.java).apply {
                putExtra(EXTRA_EMAIL, email)
                putExtra(EXTRA_PASSWORD, password)
            }
        }
    }

    private val email: String
        get() = intent.getStringExtra(EXTRA_EMAIL) ?: throw Exception("No extra $EXTRA_EMAIL")

    private val password: String
        get() = intent.getStringExtra(EXTRA_PASSWORD) ?: throw Exception("No extra $EXTRA_PASSWORD")

    override fun provideFlutterEngine(context: Context): FlutterEngine? {
        return FlutterEngineCache.getInstance().get("my_engine_id");
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL
        ).setMethodCallHandler { call: MethodCall, result: MethodChannel.Result ->
            when (call.method) {
                "getConfig" -> {
                    val json = JSONObject()
                    try {
                        json.put("username", email)
                        json.put("password", password)
                        json.put("botId", BOT_ID)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    result.success(json.toString())
                }
                "close-module" -> {
                    finish()
                }
                "getCurrentTicket" -> {
                    result.success(ticketId)
                }
                "setCurrentTicket" -> {
                    ticketId = null
                    result.success(true)
                }
                else -> {
                    result.notImplemented()
                }
            }
        }
    }
}