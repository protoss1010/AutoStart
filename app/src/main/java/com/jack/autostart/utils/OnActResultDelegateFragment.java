package com.jack.autostart.utils;

import android.content.Intent;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class OnActResultDelegateFragment extends Fragment {

    private static final String DELEGATE_FRAGMENT_TAG = OnActResultDelegateFragment.class.getSimpleName() + "Tag";

    private int mRequestCode = 0x11;

    private final SparseArray<ResultCallback> mCallbacks = new SparseArray<>();

    /**
     * A callback for receiving the activity result.
     */
    public interface ResultCallback {
        /**
         * Called when an activity exits.
         *
         * @param data An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
         */
        void onActivityResult(Intent data);
    }

    /**
     * Returns an OnActResultDelegateFragment object.
     *
     * @param fm The manager of fragment.
     * @return OnActResultDelegateFragment.
     */
    public static OnActResultDelegateFragment request(@NonNull FragmentManager fm) {
        OnActResultDelegateFragment fragment = (OnActResultDelegateFragment) fm.findFragmentByTag(DELEGATE_FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new OnActResultDelegateFragment();
            fm.beginTransaction()
                    .add(fragment, DELEGATE_FRAGMENT_TAG)
                    .commitAllowingStateLoss();
            fm.executePendingTransactions();
        }
        return fragment;
    }

    /**
     * Launches an activity for which you would like a result when it finished.
     *
     * @param intent   The intent to start.
     * @param callback The callback to be invoked when the activity exits.
     */
    public void startForResult(Intent intent, ResultCallback callback) {
        // mRequestCode and callback need correspond one by one
        mCallbacks.put(mRequestCode, callback);
        startActivityForResult(intent, mRequestCode);
        mRequestCode++;
    }

    /**
     * Called when an activity exits.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult(),
     *                    allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ResultCallback callback = mCallbacks.get(requestCode);
        mCallbacks.remove(requestCode);

        if (callback != null) {
            callback.onActivityResult(data);
        }
    }

}