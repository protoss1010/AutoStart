package com.jack.autostart.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.jack.autostart.app.BaseApplication;
import com.jack.autostart.databinding.MainActivityBinding;
import com.jack.autostart.ui.list.AppInfoListItemAdapter;
import com.jack.autostart.ui.model.AppInfo;
import com.jack.autostart.ui.model.AppInfoListViewModel;
import com.jack.autostart.utils.AppLauncherUtils;
import com.jack.autostart.utils.handler.MyHandlerThread;

import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MainActivityBinding mBinding;
    private AppInfoListViewModel mViewModel;
    private AppInfoListItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        mBinding = MainActivityBinding.inflate(LayoutInflater.from(MainActivity.this));
        setContentView(mBinding.getRoot());
        initViewModel();
        initView();
        checkDrawOverlaysPermission();
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(AppInfoListViewModel.class);
        mViewModel.fetchAllInstalledApps();
    }

    private void initView() {
        mAdapter = new AppInfoListItemAdapter(mViewModel.getAppInfos(), mOnItemListener);
        mBinding.appList.setAdapter(mAdapter);
        mBinding.appList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private final AppInfoListItemAdapter.OnItemListener mOnItemListener = new AppInfoListItemAdapter.OnItemListener() {
        @Override
        public void onItemClick(AppInfo appInfo) {
            mViewModel.addSelectItem(appInfo);
            mAdapter.update();
        }

        @Override
        public void onItemLongClick(AppInfo appInfo) {
            mViewModel.removeSelectItem(appInfo);
            mAdapter.update();
        }

        @Override
        public void onMinusClick(AppInfo appInfo) {
            mViewModel.minus(appInfo);
            mAdapter.update();
        }

        @Override
        public void onPlusClick(AppInfo appInfo) {
            mViewModel.plus(appInfo);
            mAdapter.update();
        }
    };

    private void checkDrawOverlaysPermission() {
        if (!Settings.canDrawOverlays(BaseApplication.getContext())) {
            // Initialize ActivityResultLauncher for overlay permission
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + BaseApplication.getContext().getPackageName()));
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (Settings.canDrawOverlays(BaseApplication.getContext())) {
                    startLaunchApp();
                } else {
                    Toast.makeText(BaseApplication.getContext(), "Overlay permission denied", Toast.LENGTH_LONG).show();
                }
            }).launch(intent);
        } else {
            startLaunchApp();
        }
    }

    private void startLaunchApp() {
        List<AppInfo> selectedAppsInfo = mViewModel.getSelectedAppsInfo();
        selectedAppsInfo.sort(Comparator.comparingInt(AppInfo::getOrder));
        MyHandlerThread handler = new MyHandlerThread();
        long totalDelay = 0;
        for (AppInfo appInfo : selectedAppsInfo) {
            long delayTime = appInfo.getDelaySec() * 1000L; // 將秒轉換為毫秒
            totalDelay += delayTime; // 累積延遲時間
            handler.postDelayed(() -> {
                AppLauncherUtils.launchAppWithPackageName(appInfo.getPackageName());
                Log.d(TAG, "Launching app: " + appInfo);
            }, totalDelay);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}