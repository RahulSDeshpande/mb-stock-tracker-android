package com.rahul.stocker.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahul.stocker.ui.stocks.StocksScreen
import com.rahul.stocker.ui.stocks.StocksViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm: StocksViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
    }

    private fun initViews() {
        setContent {
            val viewState by vm.viewState.collectAsStateWithLifecycle()
            StocksScreen(viewState = viewState)
        }
    }
}
