package com.jack.autostart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.jack.autostart.ui.theme.AutoAtartTheme
import com.jack.autostart.utils.AppLauncherUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: AppListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AutoAtartTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppsInfoScreen(viewModel)
                }
            }
        }

        GlobalScope.launch {
            for (appInfo in viewModel.getSelectedAppsInfo()) {
                delay(3000)
                AppLauncherUtils.launchAppWithPackageName(this@MainActivity, appInfo.packageName)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppsInfoScreen(viewModel: AppListViewModel) {
    val appsInfo = viewModel.appsInfo
    val selectedAppsInfo = viewModel.selectedAppsInfo
    LaunchedEffect(Unit) {
        viewModel.getAllInstalledApps()
    }

    Column {
        if (appsInfo.isEmpty()) {
            // Show loading indicator or placeholder
            Text(text = "Loading...")
        } else {
            // Display the list of credit cards
            LazyColumn {
                itemsIndexed(items = appsInfo) { index, appInfo ->
                    Text(text = appInfo.appName)
                    Text(text = appInfo.packageName)
                    var order = -1
                    for ((selectedIndex, selected) in selectedAppsInfo.withIndex()) {
                        if (appInfo == selected) {
                            order = selectedIndex + 1
                        }
                    }
                    if (order != -1) {
                        Text(text = order.toString())
                    }
                    Image(
                        bitmap = appInfo.icon.toBitmap().asImageBitmap(),
                        "",
                        Modifier
                            .combinedClickable(
                                onClick = {
                                    viewModel.addSelectItem(appInfo)
                                },
                                onLongClick = {
                                    viewModel.removeSelectItem(appInfo)
                                }
                            )
                            .size(80.dp)
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp)) // Add a divider between items
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AutoAtartTheme {
        Greeting("Android")
    }
}