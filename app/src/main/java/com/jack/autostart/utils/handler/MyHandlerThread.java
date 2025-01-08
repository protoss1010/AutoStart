package com.jack.autostart.utils.handler;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * A custom HandlerThread class that provides a Handler associated with the thread.
 */
public class MyHandlerThread extends HandlerThread {

    public static final int THREAD_PRIORITY_MAX = -20;

    private static final String DEFAULT_THREAD_NAME = "HandlerThread";
    private final Handler mHandler;
    private boolean mIsQuit;

    /**
     * Constructs a MyHandlerThread with the default thread name.
     */
    public MyHandlerThread() {
        super(DEFAULT_THREAD_NAME);
        start();
        mHandler = new Handler(getLooper());
    }

    /**
     * Constructs a MyHandlerThread with the given thread name.
     *
     * @param threadName The thread name
     */
    public MyHandlerThread(String threadName) {
        super(threadName);
        start();
        mHandler = new Handler(getLooper());
    }

    /**
     * Constructs a MyHandlerThread with the given thread name and priority.
     *
     * @param threadName The thread name
     * @param priority   The thread priority
     */
    public MyHandlerThread(String threadName, int priority) {
        super(threadName, priority);
        start();
        mHandler = new Handler(getLooper());
    }

    /**
     * Constructs a MyHandlerThread with the given handler.
     *
     * @param handler The Handler object
     */
    public MyHandlerThread(Handler handler) {
        super(DEFAULT_THREAD_NAME);
        start();
        mHandler = handler;
    }

    /**
     * Retrieves the Handler object associated with the MyHandlerThread.
     *
     * @return The Handler object
     */
    public Handler getHandler() {
        return mHandler;
    }

    /**
     * Posts a Runnable to the message queue of the handler associated with the thread.
     *
     * @param runnable The Runnable to be executed
     */
    public void post(Runnable runnable) {
        mHandler.post(runnable);
    }

    /**
     * Posts a Runnable to the message queue of the handler associated with the thread, with a specified delay.
     *
     * @param runnable    The Runnable to be executed
     * @param delayMillis The delay (in milliseconds) before the Runnable is executed
     */
    public void postDelayed(Runnable runnable, long delayMillis) {
        mHandler.postDelayed(runnable, delayMillis);
    }

    /**
     * Remove any pending posts of callbacks and sent messages.
     */
    public void removeCallbacksAndMessages() {
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * Quits the handler thread's looper.
     *
     * @return {@code true} if the looper has quit, {@code false} otherwise
     */
    public boolean quit() {
        boolean quit = super.quit();
        mIsQuit = true;
        return quit;
    }

    /**
     * Checks if the handler thread's looper has quit.
     *
     * @return {@code true} if the looper has quit, {@code false} otherwise
     */
    public boolean isQuit() {
        return mIsQuit;
    }
}