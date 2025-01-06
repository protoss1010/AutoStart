package com.jack.autostart

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import com.jack.autostart.AppListViewModel.AppInfo
import com.jack.autostart.app.BaseApplication
import com.jack.autostart.ui.theme.AutoStartTheme
import com.jack.autostart.utils.AppLauncherUtils
import com.jack.autostart.utils.OnActResultDelegateFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val TAG = MainActivity::class.simpleName

class MainActivity : AppCompatActivity() {

    private val viewModel: AppListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContent {
            AutoStartTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    viewModel.getAllInstalledApps()
                    AppsInfoScreen(
                        viewModel.appsInfo,
                        { viewModel.addSelectItem(it) },
                        { viewModel.removeSelectItem(it) })
                }
            }
        }
        checkDrawOverlaysPermission()
    }

    private fun checkDrawOverlaysPermission() {
        val context = BaseApplication.context
        if (!Settings.canDrawOverlays(context)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.setData(Uri.parse("package:" + context.packageName))
            OnActResultDelegateFragment.request(supportFragmentManager)
                .startForResult(intent) {
                    if (Settings.canDrawOverlays(context)) {
                        startLaunchApp()
                    } else {
                        Toast.makeText(context, "Overlay permission denied", Toast.LENGTH_LONG).show()
                    }
                }
        } else {
            startLaunchApp()
        }
    }

    private fun startLaunchApp() {
        GlobalScope.launch {
            for (appInfo in viewModel.getSelectedAppsInfo()) {
                AppLauncherUtils.launchAppWithPackageName(appInfo.packageName)
                Log.d(TAG, "appInfo $appInfo")
                delay(5000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppsInfoScreen(
    appsInfo: List<AppInfo>,
    onClick: (AppInfo) -> Unit,
    onLongClick: (AppInfo) -> Unit,
) {
    Column {
        if (appsInfo.isEmpty()) {
            Text(text = "Loading...")
        } else {
            LazyColumn {
                items(appsInfo) { appInfo ->
                    Column {
                        Row(Modifier
                            .fillMaxSize()
                            .combinedClickable(
                                onClick = {
                                    onClick(appInfo)
                                },
                                onLongClick = {
                                    onLongClick(appInfo)
                                }
                            )
                            .padding(10.dp)) {
                            val bitmap: ImageBitmap = try {
                                appInfo.icon!!.toBitmap().asImageBitmap()
                            } catch (_: Exception) {
                                ImageBitmap(100, 100)
                            }
                            Image(
                                bitmap = bitmap,
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .size(80.dp)
                            )
                            Column {
                                Text(text = appInfo.appName)
                                Text(text = appInfo.packageName)
                            }
                            Spacer(Modifier.weight(1f))
                            if (appInfo.order != 9999) {
                                Text(
                                    text = appInfo.order.toString(),
                                    modifier = Modifier.align(CenterVertically)
                                )
                            }
                        }
                        Divider()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppsInfoScreenPreview() {
    AutoStartTheme {
        AppsInfoScreen(
            listOf(
                AppInfo(
                    "Settings",
                    "com.setting",
                    icon = R.drawable.ic_launcher.toDrawable()
                ),
                AppInfo(
                    "Camera",
                    "com.google.camera",
                    icon = R.drawable.ic_launcher.toDrawable()
                )
            ),
            {},
            {})
    }
}