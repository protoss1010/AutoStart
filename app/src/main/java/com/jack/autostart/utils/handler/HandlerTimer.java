package com.jack.autostart.utils.handler;

import android.os.Handler;
import android.os.Process;
import android.util.Log;

import com.jack.autostart.utils.ThreadUtils;

/**
 * A timer class that provides a convenient way to schedule and manage timed tasks using the Handler class in Android.
 */
public class HandlerTimer {

    private static final String TAG = HandlerTimer.class.getSimpleName();

    private static final int PERIOD_INFINITE = -1;

    private final Handler mHandler;
    private boolean mAutoQuit;
    private long mDelayMs;
    private long mCurrentPeriod;
    private long mPeriod = PERIOD_INFINITE;
    private State mState = State.STOP;
    private TimerCallback mCallback;

    /**
     * Provides a callback method for the HandlerTimer.
     */
    public interface TimerCallback {
        /**
         * Called when the timer reaches the specified time.
         *
         * @param currentPeriod The current execution count.
         */
        void onTimeUp(long currentPeriod);
    }

    /**
     * An enumeration representing the state of the HandlerTimer.
     */
    public enum State {
        START, STOP, PAUSE, QUIT
    }

    /**
     * Constructs a HandlerTimer with the given delay time in milliseconds.
     *
     * @param delayMs The delay time in milliseconds.
     */
    public HandlerTimer(long delayMs) {
        this(delayMs, null);
    }

    /**
     * Constructs a HandlerTimer with the given delay time in milliseconds and a callback.
     *
     * @param delayMs  The delay time in milliseconds.
     * @param callback The TimerCallback.
     */
    public HandlerTimer(long delayMs, TimerCallback callback) {
        this(delayMs, callback, Process.THREAD_PRIORITY_DEFAULT);
    }

    /**
     * Constructs a HandlerTimer with the given delay time in milliseconds, a callback, and a priority.
     *
     * @param delayMs  The delay time in milliseconds.
     * @param callback The TimerCallback.
     * @param priority The priority to run the thread at.
     */
    public HandlerTimer(long delayMs, TimerCallback callback, int priority) {
        mHandler = new Handler(new MyHandlerThread("HandlerTimerThread", priority).getLooper());
        mDelayMs = delayMs;
        mCallback = callback;
    }

    /**
     * Constructs a HandlerTimer with the given delay time in milliseconds, a callback, and a handler.
     *
     * @param delayMs  The delay time in milliseconds.
     * @param callback The TimerCallback.
     * @param handler  The Handler object.
     */
    public HandlerTimer(long delayMs, TimerCallback callback, Handler handler) {
        mHandler = handler;
        mDelayMs = delayMs;
        mCallback = callback;
    }

    private final Runnable mTimerRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(this, mDelayMs);
            mCurrentPeriod++;
            if (mCallback != null) {
                mCallback.onTimeUp(mCurrentPeriod);
            }
            if (isArriveTimes()) {
                if (mAutoQuit) {
                    quit();
                } else {
                    stop();
                }
            }
        }

        private boolean isArriveTimes() {
            if (mPeriod == PERIOD_INFINITE) {
                return false;
            }
            return mPeriod == mCurrentPeriod;
        }
    };

    /**
     * Sets whether the HandlerTimer should quit the Looper of the Handler when the target number of executions is reached.
     *
     * @param autoQuit {@code true} to quit the Looper, {@code false} to stop the HandlerTimer.
     */
    public void autoQuit(boolean autoQuit) {
        mAutoQuit = autoQuit;
    }

    /**
     * Sets the delay time in milliseconds.
     *
     * @param delayMs The delay time in milliseconds.
     */
    public void setDelayMs(long delayMs) {
        mDelayMs = delayMs;
    }

    /**
     * Sets the target number of executions.
     *
     * @param period The number of executions.
     */
    public void setPeriod(long period) {
        mPeriod = period;
    }

    /**
     * Sets the TimerCallback.
     *
     * @param callback The TimerCallback.
     */
    public void setCallback(TimerCallback callback) {
        mCallback = callback;
    }

    /**
     * Gets the current state of the HandlerTimer.
     *
     * @return The current state of the HandlerTimer.
     */
    public State getState() {
        return mState;
    }

    /**
     * Pauses any pending posts of the Runnable that are in the message queue.
     */
    public void pause() {
        mState = State.PAUSE;
        mHandler.removeCallbacks(mTimerRunnable);
    }

    /**
     * Removes any pending posts of the Runnable that are in the message queue.
     */
    public void stop() {
        mState = State.STOP;
        mCurrentPeriod = 0;
        mHandler.removeCallbacks(mTimerRunnable);
    }

    /**
     * Adds the Runnable to the message queue to be run after the specified amount of time elapses.
     * The Runnable will be run on the thread to which this handler is attached.
     */
    public void start() {
        mState = State.START;
        mHandler.removeCallbacks(mTimerRunnable);
        mHandler.postDelayed(mTimerRunnable, mDelayMs);
    }

    /**
     * Adds the Runnable to the message queue to be run immediately.
     * The Runnable will be run on the thread to which this handler is attached.
     */
    public void startNow() {
        mState = State.START;
        mHandler.removeCallbacks(mTimerRunnable);
        mHandler.post(mTimerRunnable);
    }

    /**
     * Quits the message loop (Looper) of the Handler.
     */
    public void quit() {
        mState = State.QUIT;
        mCallback = null;
        boolean mainThread = ThreadUtils.isMainThread();
        Log.d(TAG, "Call quit isMainThread:" + mainThread);
        if (!mainThread) {
            mHandler.getLooper().quit();
        }
    }

    /**
     * A builder class for creating an instance of HandlerTimer with optional parameters.
     */
    public static class Builder {
        private boolean mAutoQuit;
        private long mDelayMs;
        private long mPeriod = PERIOD_INFINITE;
        private TimerCallback mCallback;

        /**
         * Sets whether the HandlerTimer should quit the Looper of the Handler when the target number of executions is reached.
         *
         * @param autoQuit {@code true} to quit the Looper, {@code false} to stop the HandlerTimer.
         * @return This Builder object to allow for method chaining.
         */
        public Builder autoQuit(boolean autoQuit) {
            mAutoQuit = autoQuit;
            return this;
        }

        /**
         * Sets the delay time in milliseconds.
         *
         * @param delayMs The delay time in milliseconds.
         * @return This Builder object to allow for method chaining.
         */
        public Builder setDelayMs(long delayMs) {
            mDelayMs = delayMs;
            return this;
        }

        /**
         * Sets the target number of executions.
         *
         * @param period The number of executions.
         * @return This Builder object to allow for method chaining.
         */
        public Builder setPeriod(long period) {
            mPeriod = period;
            return this;
        }

        /**
         * Sets the TimerCallback.
         *
         * @param callback The TimerCallback.
         * @return This Builder object to allow for method chaining.
         */
        public Builder setCallback(TimerCallback callback) {
            mCallback = callback;
            return this;
        }

        /**
         * Builds and returns an instance of HandlerTimer with the specified parameters.
         *
         * @return An instance of HandlerTimer.
         */
        public HandlerTimer build() {
            HandlerTimer handlerTimer = new HandlerTimer(mDelayMs);
            handlerTimer.autoQuit(mAutoQuit);
            handlerTimer.setPeriod(mPeriod);
            handlerTimer.setCallback(mCallback);
            return handlerTimer;
        }

    }
}