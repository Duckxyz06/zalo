package com.duckxyz.zgm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.duckxyz.zgm.data.RoomZgmRepository
import com.duckxyz.zgm.data.local.ZgmDatabase
import com.duckxyz.zgm.data.sampleGroups
import com.duckxyz.zgm.data.sampleTasks
import com.duckxyz.zgm.ui.ZgmApp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val repository by lazy {
        RoomZgmRepository(ZgmDatabase.getInstance(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repository.seedIfEmpty(sampleGroups, sampleTasks)
        }

        setContent {
            ZgmApp(repository)
        }
    }
}
