package com.example.president

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.president.ui.theme.PresidentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            PresidentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavHost(navController, startDestination = "start") {
                        composable("start") { ListClick(navController = navController) }
                        composable("startName/{name}") {
                            StartName(it.arguments?.getString("name")!!)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListClick(navController: NavController) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        DataProvider.presidents.sortedWith(compareBy { it.fullname }).forEach {
            Text(
                it.fullname,
                modifier = Modifier.selectable(
                    true,
                    onClick = { navController.navigate("startName/${it.fullname}") })

            )
        }
    }

}

@Composable
fun StartName(userName: String) {
    Column {
        val result = DataProvider.presidents.filter { it.fullname == userName }
        Text(text = result[0].fullname)
        Text(text = result[0].title)
        Text(text = result[0].startYear.toString())
        Text(text = result[0].endYear.toString())
    }

}