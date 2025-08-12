package com.rahul.stocker.data.remote

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.rahul.stocker.BuildConfig
import com.rahul.stocker.domain.model.StockPriceEventModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

class StockPriceServiceImpl(
    private val wsUrl: String = BuildConfig.WS_URL,
) : StockPriceService {
    // TODO | MIGHT NEED SCOPE FROM THE ViewModel
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val httpClient =
        OkHttpClient
            .Builder()
            .pingInterval(20, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()

    private var webSocket: WebSocket? = null
    private val gson = Gson()

    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean> = _isConnected

    private val _receivingEvent =
        MutableSharedFlow<StockPriceEventModel>(
            extraBufferCapacity = 8,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )
    override val receivingEvent: SharedFlow<StockPriceEventModel> = _receivingEvent

    override fun connect() {
        if (webSocket != null) {
            return
        }

        webSocket =
            httpClient.newWebSocket(
                request =
                    Request
                        .Builder()
                        .url(wsUrl)
                        .build(),
                listener =
                    object : WebSocketListener() {
                        override fun onOpen(
                            webSocket: WebSocket,
                            response: Response,
                        ) {
                            _isConnected.value = true
                        }

                        override fun onMessage(
                            webSocket: WebSocket,
                            text: String,
                        ) {
                            try {
                                val model = gson.fromJson(text, StockPriceEventModel::class.java)
                                if (model.symbol.isNotBlank() && !model.price.isNaN()) {
                                    coroutineScope.launch { _receivingEvent.emit(value = model) }
                                }
                            } catch (jse: JsonSyntaxException) {
                                print(jse.message)
                            }
                        }

                        override fun onClosed(
                            webSocket: WebSocket,
                            code: Int,
                            reason: String,
                        ) {
                            _isConnected.value = false
                        }

                        override fun onFailure(
                            webSocket: WebSocket,
                            t: Throwable,
                            response: Response?,
                        ) {
                            _isConnected.value = false
                        }
                    },
            )
    }

    override fun sendEvent(event: StockPriceEventModel) {
        webSocket?.send(text = gson.toJson(event))
    }

    override fun disconnect() {
        webSocket?.close(
            // TODO | NEED DYNAMIC REFRESH INTERVAL
            // APP CRASH - java.lang.IllegalArgumentException: Code must be in range [1000,5000): 100 :O :O :O
            code = 1001,
            reason = "USER STOPPED/DISCONNECTED",
        )
        webSocket = null
        _isConnected.value = false
    }

    override fun cancel() {
        disconnect()
        coroutineScope.cancel()
    }
}
