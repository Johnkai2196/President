package com.example.president

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.president.ui.theme.PresidentTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PresidentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ListClick()
                }
            }
        }
    }
}

@Composable
fun ListClick(mainViewModel: MainViewModel = viewModel()) {
    val hit: Int? by mainViewModel.changeNotifier.observeAsState(null)
    var change by remember { mutableStateOf("") }
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        hit?.let {
            Text("${change}: Hits $hit")
            Spacer(modifier = Modifier.height(1.dp))
            Divider()
            Spacer(modifier = Modifier.height(1.dp))
        }
        DataProvider.presidents.sortedWith(compareBy { it.fullname }).forEach {
            Text(
                it.fullname,
                color = if (change == it.fullname) Color.Black else Color.Gray,
                modifier = Modifier.selectable(
                    true,
                    onClick = {
                        mainViewModel.getHits(it.fullname)
                        change = it.fullname
                    }
                )
            )
        }

    }
}

class MainViewModel : ViewModel() {
    private val repository: WikiRepository = WikiRepository()

    val changeNotifier = MutableLiveData<Int>()

    fun getHits(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val serverResp = repository.getUser(name)
            changeNotifier.postValue(serverResp.query.searchinfo.totalhits)
            Log.i("testing", "${serverResp.query.searchinfo.totalhits}")
        }
    }
}