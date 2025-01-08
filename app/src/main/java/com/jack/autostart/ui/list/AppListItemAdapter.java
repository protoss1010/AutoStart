package com.jack.autostart.ui.list;

import static com.jack.autostart.ui.model.AppListViewModel.NO_ORDER;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jack.autostart.ui.model.AppInfo;
import com.jack.autostart.databinding.AppItemBinding;

import java.util.ArrayList;

public class AppListItemAdapter extends RecyclerView.Adapter<AppListItemAdapter.VideoOutputViewHolder> {

    private static final String TAG = AppListItemAdapter.class.getSimpleName();

    private final ArrayList<AppInfo> mVideoOutputs;

    private final OnItemListener mOnItemListener;

    public interface OnItemListener {
        void onItemClick(AppInfo appInfo);

        void onItemLongClick(AppInfo appInfo);

        void onMinusClick(AppInfo appInfo);

        void onPlusClick(AppInfo appInfo);
    }

    public AppListItemAdapter(ArrayList<AppInfo> appInfos, OnItemListener onItemListener) {
        mVideoOutputs = appInfos;
        mOnItemListener = onItemListener;
    }

    @NonNull
    @Override
    public VideoOutputViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoOutputViewHolder(AppItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoOutputViewHolder holder, int position) {
        AppInfo appInfo = mVideoOutputs.get(position);
        holder.mBinding.icon.setImageDrawable(appInfo.getIcon());
        if (appInfo.getOrder() == NO_ORDER) {
            holder.mBinding.delayGroup.setVisibility(View.GONE);
        } else {
            holder.mBinding.delayGroup.setVisibility(View.VISIBLE);
            holder.mBinding.appLaunchOrder.setText(String.valueOf(appInfo.getOrder()));
            holder.mBinding.delayTime.setText(String.valueOf(appInfo.getDelaySec()));
        }
        holder.mBinding.appName.setText(appInfo.getAppName());
        holder.mBinding.appPackage.setText(appInfo.getPackageName());
    }

    @Override
    public int getItemCount() {
        return mVideoOutputs.size();
    }

    public void update() {
        notifyDataSetChanged();
    }

    public class VideoOutputViewHolder extends RecyclerView.ViewHolder {

        private final AppItemBinding mBinding;

        VideoOutputViewHolder(AppItemBinding itemBinding) {
            super(itemBinding.getRoot());
            mBinding = itemBinding;
            mBinding.minus.setOnClickListener(v -> {
                AppInfo appInfo = mVideoOutputs.get(getLayoutPosition());
                mOnItemListener.onMinusClick(appInfo);
            });
            mBinding.plus.setOnClickListener(v -> {
                AppInfo appInfo = mVideoOutputs.get(getLayoutPosition());
                mOnItemListener.onPlusClick(appInfo);
            });
            itemView.setOnClickListener(v -> {
                AppInfo appInfo = mVideoOutputs.get(getLayoutPosition());
                mOnItemListener.onItemClick(appInfo);
            });
            itemView.setOnLongClickListener(v -> {
                AppInfo appInfo = mVideoOutputs.get(getLayoutPosition());
                mOnItemListener.onItemLongClick(appInfo);
                return false;
            });
        }
    }

}