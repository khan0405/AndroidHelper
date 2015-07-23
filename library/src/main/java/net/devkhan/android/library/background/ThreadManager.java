package net.devkhan.android.library.background;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.devkhan.android.library.callback.Callback;
import net.devkhan.android.library.logging.L;
import net.devkhan.android.library.ui.UIHandler;

/**
 * Thread Management class.
 * Created by KHAN on 2015-07-14.
 */
public class ThreadManager  {
    private static final String TAG = L.getLogTag(ThreadManager.class);

    private ExecutorService cachedThreadPool;
    private ExecutorService singleThreadExecutor;
    private UIHandler handler = new UIHandler();

    public ThreadManager() {
        initialize();
    }

    public void initialize() {
        if (isTerminated()) {
            cachedThreadPool = Executors.newCachedThreadPool();
            singleThreadExecutor = Executors.newSingleThreadExecutor();
        }
    }

    public void shutdown() {
        if (isAlive(cachedThreadPool)) {
            cachedThreadPool.shutdownNow();
            cachedThreadPool = null;
        }
        if (isAlive(singleThreadExecutor)) {
            singleThreadExecutor.shutdownNow();
            singleThreadExecutor = null;
        }
    }

    private boolean isAlive(ExecutorService executor) {
        return executor != null && !executor.isShutdown() && !executor.isTerminated();
    }

    public boolean isTerminated() {
        return isTerminated(cachedThreadPool) || isTerminated(singleThreadExecutor);
    }

    private boolean isTerminated(ExecutorService executor) {
        return (executor == null || executor.isShutdown() || executor.isTerminated());
    }

    private void throwIfTerminated() {
        if (isTerminated()) {
            throw new RuntimeException("Thread Manager was Terminated!");
        }
    }

    public List<Future<?>> executeSingleThread(Runnable... tasks) {
        return executeThread(singleThreadExecutor, tasks);
    }

    public <T> List<Future<T>> executeSingleThread(Callable<T>... tasks) {
        return executeThread(singleThreadExecutor, tasks);
    }

    public Future<?> executeSingleThread(Runnable task) {
        return executeThread(singleThreadExecutor, task);
    }

    public <T> Future<T> executeSingleThread(Callable<T> task) {
        return executeThread(singleThreadExecutor, task);
    }

    public List<Future<?>> executeMultiThread(Runnable... tasks) {
        return executeThread(cachedThreadPool, tasks);
    }

    public <T> List<Future<T>> executeMultiThread(Callable<T>... tasks) {
        return executeThread(cachedThreadPool, tasks);
    }

    public Future<?> executeMultiThread(Runnable task) {
        return executeThread(cachedThreadPool, task);
    }

    public <T> Future<T> executeMultiThread(Callable<T> task) {
        return executeThread(cachedThreadPool, task);
    }

    public List<Future<?>> executeThread(ExecutorService executor, Runnable... tasks) {
        if (tasks == null) {
            return null;
        }
        throwIfTerminated();
        List<Future<?>> futures = new ArrayList<Future<?>>();
        for (Runnable task : tasks) {
            futures.add(executeThread(executor, task));
        }
        return futures;
    }

    private <T> List<Future<T>> executeThread(ExecutorService executor, Callable<T>... tasks) {
        if (tasks == null) {
            return null;
        }
        throwIfTerminated();
        List<Future<T>> futures = new ArrayList<Future<T>>();
        for (Callable<T> task : tasks) {
            futures.add(executeThread(executor, task));
        }
        return futures;
    }

    private Future<?> executeThread(ExecutorService executor, Runnable task) {
        if (task == null) {
            return null;
        }
        throwIfTerminated();
        return executor.submit(task);
    }

    private <T> Future<T> executeThread(ExecutorService executor, Callable<T> task) {
        if (task == null) {
            return null;
        }
        throwIfTerminated();
        return executor.submit(task);
    }

    public void runBackgroundThreadWithMultiTasking(final Callback<Void> callback, final Runnable... tasks) {
        Runnable backgroundTask = new Runnable() {
            public void run() {
                List<Future<?>> futures = executeMultiThread(tasks);
                for (Future<?> future : futures) {
                    try {
                        future.get();
                    }
                    catch (InterruptedException e) {
                        L.d(TAG, e.getClass().getSimpleName(), e);
                    }
                    catch (ExecutionException e) {
                        L.d(TAG, e.getClass().getSimpleName(), e);
                    }
                }
                if (callback != null) {
                    handler.post(new Runnable() {
                        public void run() {
                            callback.call(null);
                        }
                    });
                }
            }
        };
        executeSingleThread(backgroundTask);
    }

}
